package com.kkp.demo.config;

import com.kkp.demo.handler.FallingMoneyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
@EnableWebFlux
public class RoutingConfig {
    @Bean
    public RouterFunction<ServerResponse> router(FallingMoneyHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/falling/money"), handler::startFallingMoney)
                .andRoute(RequestPredicates.GET("/falling/money"), handler::getFallingMoney)
                .andRoute(RequestPredicates.GET("/falling/money/history"), handler::getFallingMoneyHistory);
    }
}
