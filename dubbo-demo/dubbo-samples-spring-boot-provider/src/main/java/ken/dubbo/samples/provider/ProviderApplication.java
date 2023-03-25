package ken.dubbo.samples.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ProviderApplication {
    public static void main(String[] args) throws Exception {

        SpringApplication.run(ProviderApplication.class, args);
        System.out.println("dubbo service started");
//        new CountDownLatch(1).await();
    }
}
