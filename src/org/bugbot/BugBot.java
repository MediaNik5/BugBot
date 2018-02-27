package org.bugbot;

import org.bugbot.cmds.wrapper;
import org.bugbot.config.Config;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BugBot extends TelegramLongPollingBot {

    public Config cahe = new Config();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BugBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public String addRom(String name){

        String tkn = name+generateNewToken();
        List<String> l = cahe.getStringList("tkn");
        while(l.contains(tkn)){
            tkn = name+generateNewToken();
        }
        l.add(tkn);
        cahe.setStringList("tkn", l);
        return tkn;
    }
    public boolean addGroup(String id, String tkn){
        if(cahe.getString(id)==null && cahe.getStringList("tkn").contains(tkn)){
            List<String> l = cahe.getStringList(tkn)==null?new ArrayList<>(): cahe.getStringList(tkn);
            l.add(id);
            cahe.setStringList(tkn, l);
            cahe.setString(id, tkn);
            return true;
        }
        return false;
    }


    private double generateNewToken() {
        return (Math.random()*1000000);
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
    public void onUpdateReceived(Update e) {
        wrapper.proceed(e, this);
    }
    public String getBotUsername() {
        return settings.name; // Create ur class settings.java and add 'public static final String name'
    }
    public String getBotToken() {
        return settings.token; // Create ur class settings.java and add 'public static final String token'
    }
}


