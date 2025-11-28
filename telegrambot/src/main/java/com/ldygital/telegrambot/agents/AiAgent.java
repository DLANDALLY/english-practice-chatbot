package com.ldygital.telegrambot.agents;

import com.ldygital.telegrambot.services.interfaces.IGemini;
import org.springframework.stereotype.Component;

@Component
public class AiAgent {
    private final IGemini geminiService;

    public AiAgent(IGemini geminiService) {
        this.geminiService = geminiService;
    }

    public String askAgent(String query){
        return geminiService.generateContent(query);
    }

}
