package me.chenyi.sitemind.mailgun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import me.chenyi.sitemind.mailgun.controller.ProviderMailgunController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${provider.register.address}")
    private String providerRegisterUrl;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.address}")
    private String serverAddress;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            String serverRoot = serverPort == null || "".equals(serverPort) ?
                    "http://" + serverAddress : "http://" + serverAddress + ":" + serverPort;

            HttpResponse<JsonNode> response = Unirest.post(providerRegisterUrl)
                    .queryString("provider_id", ProviderMailgunController.ID_MAILGUN)
                    .queryString("provider_url", serverRoot + ProviderMailgunController.URL_MAILGUN)
                    .asJson();

            int status = response.getStatus();
            if (status != HttpStatus.OK.value()){
                logger.error("Failed to register the provider: " + ProviderMailgunController.ID_MAILGUN
                        + "/" + serverRoot + ProviderMailgunController.URL_MAILGUN);
            }

        };
    }

}