package org.bugbot;

import org.bugbot.cmds.*;
import org.bugbot.config.Config;
import org.bugbot.config.settings;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class BugBot extends TelegramLongPollingBot {

    public Config cahe = new Config("config.cfg");

    public static void main(String[] args) {
        wrapper.cmds.put("/faq", new faq());
        wrapper.cmds.put("/addrom", new addrom());
        wrapper.cmds.put("/install", new install());
        wrapper.cmds.put("/addbug", new addbug());
        wrapper.cmds.put("/bugs", new bugs());
        wrapper.cmds.put("/clean", new clean());
        wrapper.cmds.put("/delbug", new delbug());
        wrapper.cmds.put("/ann", new ann());
        wrapper.cmds.put("/anns", new anns());
        wrapper.cmds.put("/help", new help());
        wrapper.cmds.put("/change", new change());
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BugBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param name Enter a name of ROM u want to add
     * @return The token of created ROM
     */
    public String addRom(String name){

        String tkn = name+"."+generateNewToken();
        List<String> l = cahe.getStringList("tkn")==null?new ArrayList<>():cahe.getStringList("tkn");
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
        cahe.load();
        if(e.hasInlineQuery()){
            wrapper.proceedQuery(e, this);
            cahe.save();
            return;
        }
        if(e.hasChannelPost()){
            wrapper.proceedChannel(e, this);
            cahe.save();
            return;
        }
        if(!e.hasMessage())return;
        if(e.getMessage().getText().startsWith("#")){
            String name = null;
            try{
                name = e.getMessage().getText().split(" ")[0].toLowerCase();
            }catch (Throwable ex){
                name = e.getMessage().getText().toLowerCase();
            }
            onAnn(name, e.getMessage().getChatId());
            cahe.save();
            return;
        }
        wrapper.proceed(e, this);
        cahe.save();
    }

    public void onAnn(String name, long chat) {
        String a;
        if((a=cahe.getString(name+"."+cahe.getString(chat+"")))!=null){
            String aq [] = a.split(":");
            int reply = Integer.parseInt(aq[aq.length-1]);
            a = wrapper.toString(aq, 0, 1, ":");
            a = a.substring(0, a.length()-1);
            sendMessage(chat, a, reply);
        }
    }

    public String getBotUsername() {
        return settings.name; // Create ur class settings.java and add 'public static final String name'
    }
    public String getBotToken() {
        return settings.token; // Create ur class settings.java and add 'public static final String token'
    }

    public List<ChatMember> getAdmins(long chatId) {
        List<ChatMember> adm = new ArrayList<>();
        try {
            GetChatAdministrators ad = new GetChatAdministrators();
            ad.setChatId(chatId);
            adm = getChatAdministrators(ad);
        } catch (TelegramApiException e1) {e1.printStackTrace();}
        return adm;
    }
}


