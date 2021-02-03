package com.delta.zulu.poc.rest;

import com.delta.zulu.poc.rest.models.Todo;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        restConfiguration()
            .component("servlet")
            .enableCORS(true)
            .port(System.getenv().getOrDefault("server.port", "8080"))
            .contextPath("/api/v1")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Todo API")
            .apiProperty("api.version", "1.0.0");

        rest()
            .post("rest-openapi:openapi.yml#createTodo")
                .to("direct:post-user");

        from("direct:post-user")
            .unmarshal().json(JsonLibrary.Jackson, Todo.class)
            .log(simple("${in.body.title}").toString());
    }

}
