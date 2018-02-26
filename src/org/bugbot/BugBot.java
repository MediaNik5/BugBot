package org.bugbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BugBot extends TelegramLongPollingBot {

    Cfg cahe = new Cfg("config.cfg");

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BugBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update e) {

    }



    public void sendMessage(long chat, String text, int reply){
        SendMessage sm = new SendMessage();
        sm.setChatId(chat);
        sm.setText(text);
        try {
            if(reply!=0)
                sm.setReplyToMessageId(reply);
        }catch (Exception e){}
        try {
            sendMessage(sm);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "BugHelper";
    }
    public String getBotToken() {
        return "nononon";
    }
}


