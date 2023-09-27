package com.carlosmarin.webflux.app;

import com.carlosmarin.webflux.app.handlers.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler handler) {
        return route(GET("/api/v2/products").or(GET("/api/v3/products")), handler::list)
                .andRoute(GET("/api/v2/products/{id}"), handler::see)
                .andRoute(POST("/api/v2/products"), handler::create)
                .andRoute(PUT("/api/v2/products/{id}"), handler::edit)
                .andRoute(DELETE("/api/v2/products/{id}"), handler::delete)
                .andRoute(POST("/api/v2/products/upload/{id}"), handler::upload)
                .andRoute(POST("/api/v2/products/createWithPhoto"), handler::createWithPhoto)
                ;
    }
}
