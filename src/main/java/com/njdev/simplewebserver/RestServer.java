package com.njdev.simplewebserver;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RestServer extends WebEngine {

    private static final Logger log = LoggerFactory.getLogger(RestServer.class);

    public RestServer() {
        super();
    }

    public HttpHandler initHandlers() {
        return new RoutingHandler()
                .get("/get", this::handleGet)
                .put("/orders/{orderUUID}", this::handlePut)
                .post("/orders/{orderUUID}/checkout", this::handlePut)
                .setFallbackHandler(this::handleNotFound);
    }

    void handleNotFound(HttpServerExchange exchange) {
        log.warn(exchange.getRequestPath() + " Page not found");
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, ResponseHandler.PLAIN_TEXT);
        exchange.getResponseSender().send("Error 404 - Page Not Found");
    }

    private void handleGet(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullBytes((exch, bytes) -> {
            String msg = new String(bytes);
            log.info("Request Method : " + exch.getRequestMethod() +
                    " :: " + exchange.getRequestPath() + " RequestBody : " + msg
            );
            System.out.println(exchange.getRequestHeaders());
            new ResponseHandler(msg, ResponseHandler.JSON).handleRequest(exchange);
        });
    }

    private void handlePut(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullBytes((exch, bytes) -> {
            String msg = new String(bytes);
            log.info("Request Method : " + exch.getRequestMethod() +
                            " :: " + exchange.getRequestPath() + " RequestBody : " + msg
            );
            System.out.println(exchange.getRequestHeaders());
            new ResponseHandler(msg, ResponseHandler.JSON).handleRequest(exchange);
        });
    }
}