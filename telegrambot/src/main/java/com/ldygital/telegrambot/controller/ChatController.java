package com.ldygital.telegrambot.controller;

import com.ldygital.telegrambot.agents.AiAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.lang.management.MemoryType;

@RestController
public class ChatController {
    private final AiAgent aiAgent;

    public ChatController(AiAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam String query){
        return aiAgent.askAgent(query);
    }
}
