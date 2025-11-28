package com.ldygital.telegrambot.services;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ConversationMemory {
    private final List<String> history = new ArrayList<>();

    public void add(String message) {
        history.add(message);
    }

    public String formatHistory() {
        return String.join("\n", history);
    }
}
