package org.example.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BrowserLauncher {

    @Bean
    public ApplicationRunner openBrowser() {
        return args -> {
            try {
                Thread.sleep(2000); // запуск сервера
                String url = "http://localhost:8080";
                String os = System.getProperty("os.name").toLowerCase();

                if (os.contains("win")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec("open " + url);
                } else if (os.contains("nix") || os.contains("nux")) {
                    Runtime.getRuntime().exec("xdg-open " + url);
                } else {
                    System.out.println("Откройте браузер вручную: " + url);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
