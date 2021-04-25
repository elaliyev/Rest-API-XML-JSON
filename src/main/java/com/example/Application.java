package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.example.util.Response;

import java.io.*;
import java.net.*;

import java.util.*;

import static com.example.util.Constants.*;
import static com.example.util.Helper.getRequestParameters;


public class Application {

    public static void main(String[] args) {
        try {
            InputStream inputStream = getFileFromResourceAsStream(PROPERTIES_FILE);
            Properties appProps = new Properties();
            appProps.load(inputStream);
            inputStream.close();

            String serviceUrl = appProps.getProperty(SERVICE_URL);
            String serverPort = appProps.getProperty(SERVER_PORT);

            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(serverPort)), 0);
            server.createContext(ENDPOINT, (exchange -> {
                final Headers headers = exchange.getResponseHeaders();

                if (GET_METHOD.equals(exchange.getRequestMethod())) {

                    Map<String, String> params = getRequestParameters(exchange.getRequestURI().getRawQuery());
                    String title = params.get("title");
                    Response response = new Response();
                    if (title == null) {
                        response.setSuccess(false);
                        response.setStatusCode(HTTP_STATUS_400);
                        response.setData("Request should contain title parameter");
                    } else {
                        response = sendRequest(serviceUrl, title);
                    }
                    headers.set(CONTENT_TYPE, APPLICATION_JSON);
                    byte[] responseByte = new ObjectMapper().writeValueAsBytes(response);
                    exchange.sendResponseHeaders(response.getStatusCode(), responseByte.length);
                    exchange.getResponseBody().write(responseByte);
                }
                exchange.close();
            }));
            server.setExecutor(null);
            server.start();
            System.out.println("Server started..");
        } catch (IOException ex) {
            System.out.println("Could not read properties file");

        }

    }

    private static Response sendRequest(String api, String title) {

        URL url = null;
        try {
            url = new URL(api.concat("?title=").concat(title));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println(connection.getResponseCode());

            BufferedReader bufferedReader;
            if (HTTP_STATUS_100 <= connection.getResponseCode() && connection.getResponseCode() < HTTP_STATUS_400) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode data = xmlMapper.readTree(stringBuilder.toString().getBytes());

            return new Response(true, HTTP_STATUS_OK, data.toString());
        } catch (IOException ex) {
            return new Response(false, HTTP_STATUS_503, "Server Unavailable");
        }

    }

    private static InputStream getFileFromResourceAsStream(String fileName) {

        ClassLoader classLoader = Application.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
