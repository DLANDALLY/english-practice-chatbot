package com.ldygital.telegrambot.services.interfaces;

import reactor.core.publisher.Flux;

public interface IGemini {
    String generateContent(String prompt);
}
