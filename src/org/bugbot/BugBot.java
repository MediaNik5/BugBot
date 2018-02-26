package org.bugbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BugBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BugBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "BugHelper";
    }

    public void onUpdateReceived(Update e) {

    }

    public String getBotToken() {

        return "nononon";
    }


//    private void reWriteCache(String text) {
//        try(FileWriter writer = new FileWriter(botTelegram.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "cache.cfg", false)){
//            writer.write(text + '\n');
//            writer.flush();
//        }catch(IOException ex){System.out.println(ex.getMessage());}
//    }
//    private String readCahe() {
//        try(FileReader reader = new FileReader(botTelegram.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "cache.cfg")){
//            int c;
//            StringBuilder cache = new StringBuilder();
//            while((c=reader.read())!=-1){
//                cache.append((char)c);
//            }
//            return cache.toString();
//        }catch(IOException ex){return "";}
}


