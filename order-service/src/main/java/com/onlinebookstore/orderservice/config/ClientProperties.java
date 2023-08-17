package com.onlinebookstore.orderservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.net.URI;

@ConfigurationProperties(prefix = "bookshop")
public record ClientProperties(

        @NotNull
        URI catalogServiceUri
) {}
