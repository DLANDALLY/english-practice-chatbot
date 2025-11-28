package com.ldygital.telegrambot.telegram;


import com.ldygital.telegrambot.agents.AiAgent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final String telegramBotToken;
    private final AiAgent aiAgent;

    public TelegramBot(String telegramBotToken, AiAgent aiAgent){
        this.telegramBotToken = telegramBotToken;
        this.aiAgent = aiAgent;
    }

    /**
     * Subcribe in Telegram API some a Broker
     */
    @PostConstruct
    public void registerTelegramBot(){
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update telegramRequest) {
        try {
            if (!telegramRequest.hasMessage()) return;

            //recupere les text
            String messageText = telegramRequest.getMessage().getText();
            Long chatId = telegramRequest.getMessage().getChatId();

            sendTypingQuestion(chatId);
            String answer = aiAgent.askAgent(messageText);

            sendTextMessage(chatId, answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "EnglishtutorsBot";
    }

    @Override
    public String getBotToken(){
        return telegramBotToken;
    }

    private void sendTextMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        execute(sendMessage);
    }

    private void sendTypingQuestion(Long chatId) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(ActionType.TYPING);
    }


}
