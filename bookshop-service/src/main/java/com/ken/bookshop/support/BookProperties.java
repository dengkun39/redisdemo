package com.ken.bookshop.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties("order")
@RefreshScope
@Data
@Component
public class BookProperties {
    private Integer discount = 100;
}
