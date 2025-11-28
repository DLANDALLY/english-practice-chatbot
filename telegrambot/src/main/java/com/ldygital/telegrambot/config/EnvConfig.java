package com.ldygital.telegrambot.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }

    @Bean
    public String telegramBotToken(Dotenv dotenv) {
        return dotenv.get("TELEGRAM_BOT_TOKEN", "default-token-if-not-found");
    }

    @Bean
    public String googleApiKey(Dotenv dotenv) {
        return dotenv.get("GOOGLE_API_KEY", "default-google-api-key");
    }
}
