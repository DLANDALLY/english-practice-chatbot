package com.ldygital.telegrambot.telegram;

import com.ldygital.telegrambot.agents.AiAgent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
            Long chatId = telegramRequest.getMessage().getChatId();
            sendTypingQuestion(chatId);
            
            if (telegramRequest.getMessage().hasText()){
                messageTextuel(chatId, telegramRequest.getMessage().getText());}

            else if (telegramRequest.getMessage().hasVoice()) {
                messageVoice(chatId, telegramRequest.getMessage().getVoice().getFileId());
            }


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

    /**
     * Envoi un message au format textuel a l'API telegram
     * @param chatId identifiant de l'utilisateur
     * @param text reponses de AI
     * @throws TelegramApiException
     */
    private void sendTextMessage(Long chatId, String text) throws TelegramApiException {
        String safe = (text == null || text.isBlank())
                ? "Sorry, I couldn’t generate a response"
                : text;
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        execute(sendMessage);
    }

    private void sendTypingQuestion(Long chatId) {
        SendChatAction action = new SendChatAction();
        action.setChatId(chatId);
        action.setAction(ActionType.TYPING);
        try {
            execute(action);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reception des message textuel du user
     * @param chatId id ueser
     * @param messageText contenue du text
     * @throws TelegramApiException
     */
    private void messageTextuel(Long chatId, String messageText) throws TelegramApiException {
        String answer = aiAgent.askAgent(messageText);
        sendTextMessage(chatId, answer);
    }

    /**
     * Reception les messages vocals
     * @param chatId ID user
     * @param voiceFileId ID fichier vocal
     * @throws TelegramApiException
     */
    private void messageVoice(Long chatId, String voiceFileId) throws TelegramApiException {
        String transcription = processVoiceMessage(voiceFileId);
        String answer = aiAgent.askAgent(transcription);
        sendTextMessage(chatId, answer);
    }

    /**
     * telechargement du fichier vocal via l'API Telegram
     * @param fileId ID fichier
     * @return l'url du fichier
     * @throws TelegramApiException
     */
    private String downloadVoiceFile(String fileId) throws TelegramApiException {
        // Téléchargement du fichier vocal via l'API Telegram
        String filePath = execute(new org.telegram.telegrambots.meta.api.methods.GetFile(fileId)).getFilePath();
        return "https://api.telegram.org/file/bot" + getBotToken() + "/" + filePath;
    }

    private String processVoiceMessage(String voiceFileId) {
//        try {
//            // Télécharger le fichier vocal
//            String voiceFileUrl = downloadVoiceFile(voiceFileId);
//
//            // Utiliser une API externe pour la transcription du fichier vocal
//            // (Exemple avec Google Speech-to-Text ou autre service)
//            String transcription = transcribeAudio(voiceFileUrl);
//
//            return transcription;
//        } catch (Exception e) {
//            // Gestion des erreurs si la transcription échoue
//            return "Sorry, I couldn't transcribe the audio.";
//        }
        return null;
    }

    private void sendVoiceMessage(Long chatId, String voiceFileUrl) throws TelegramApiException {
        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(chatId);
        sendVoice.setVoice(new InputFile(voiceFileUrl));  // L'URL ou le fichier à envoyer
        execute(sendVoice);
    }
}
