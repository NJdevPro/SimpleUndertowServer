package com.njdev.simplewebserver;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.File;
import java.io.IOException;

public class ResponseHandler implements HttpHandler {

    public static final String PLAIN_TEXT = "text/plain";
    public static final String JSON = "application/json";

    private final String value;
    private final String contentType;

    public ResponseHandler(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public ResponseHandler(File file, String contentType) throws IOException {
        StringBuilder json = JsonUtil.readTextFile(file);
        this.value = json.toString();
        this.contentType = contentType;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
        exchange.getResponseHeaders().put(Headers.ACCEPT, contentType);
        exchange.getResponseSender().send(value + "\n");
    }
}