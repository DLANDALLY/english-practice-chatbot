package com.ldygital.telegrambot.services;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.ldygital.telegrambot.services.interfaces.IGemini;
import org.springframework.stereotype.Service;

@Service
public class AiGeminiService implements IGemini {
    private final String googleApiKey;
    private final ConversationMemory memory;

    public AiGeminiService(String googleApiKey, ConversationMemory memory) {
        this.googleApiKey = googleApiKey;
        this.memory = memory;
    }

    @Override
    public String generateContent(String prompt){
        memory.add("User: " + prompt);
        String finalPrompt = formatPrompt(prompt);

        String response = callGemini(finalPrompt);
        memory.add("Agent: " + response);

        return response;
    }

    private String callGemini(String finalPrompt){
        if (finalPrompt.length() > 2500) { throw new IllegalArgumentException("Prompt trop long"); }
        try {
            Client client = Client.builder()
                    .apiKey(googleApiKey)
                    .build();
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    finalPrompt,
                    config());

            return response.text();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel au modèle Gemini", e);
        }
    }

    private GenerateContentConfig config() {
        return GenerateContentConfig.builder()
                .temperature(0.3F)
                .maxOutputTokens(2500)
                .topK(40.0f)
                .topP(0.9f)
                .build();
    }

    private String formatPrompt(String userQuestion){
        String safeQuestion = (userQuestion == null) ? "" : userQuestion.trim();
        String history = memory.formatHistory();

        return """
             You are an English conversation tutor based in Toronto, Canada.
             Keep responses short: **maximum 3–4 sentences and never exceed 500 characters**.
             If the user asks something complex, give only the most essential information.
    
             Objectives:
             - Speak in English only.
             - Keep the tone casual and friendly.
             - Provide gentle corrections only when useful.
    
             Conversation history:
             %s
    
             User message:
             %s
    
             Provide your answer as the next message.
            """.formatted(history, safeQuestion);
    }
}
