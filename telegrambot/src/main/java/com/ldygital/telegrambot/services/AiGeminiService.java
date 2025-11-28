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
                .build();
    }

    private String formatPrompt(String userQuestion){
        String safeQuestion = (userQuestion == null) ? "" : userQuestion.trim();
        String history = memory.formatHistory();

        return """
             You are a friendly AI living in Toronto, Canada. You are having a casual, informal conversation
             with a person who is learning English and wants to improve their speaking skills.
             The goal is to practice English in a relaxed and enjoyable way, with friendly, constructive
             corrections when needed. All interactions will be in English, and the corrections should focus on
             grammar, pronunciation, and natural phrasing. The tone should be supportive and encouraging, and
             all responses should remain in English, with explanations and feedback given in a friendly,
             non-formal manner.
            
             Conversation memory: %s
             User's question: %s
            """.formatted(history, safeQuestion);
    }
}
