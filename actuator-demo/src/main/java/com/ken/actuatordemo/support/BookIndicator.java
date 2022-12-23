package com.ken.actuatordemo.support;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BookIndicator implements HealthIndicator {
    @Override
    public Health health() {
        Health heath;
        long currentMillis = System.currentTimeMillis();
        if(currentMillis % 2 == 0)
        {
            heath = Health
                    .up()
                    .withDetail("currentMillis", currentMillis)
                    .build();
        }
        else{
            heath = Health
                    .down()
                    .withDetail("currentMillis", currentMillis)
                    .build();
        }
        return heath;
    }
}
