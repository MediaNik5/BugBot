package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class lang implements CMD {
    @Override
    public String getName() {
        return "/lang";
    }

    @Override
    public void handle(BugBot b, Update e) {

        long chatid = e.getMessage().getChatId();
        String tkn = b.cfg.getString(chatid + "");
        if (tkn != null) {
            String state = b.cfg.getString("language." + chatid);
            if (state == null || state.equals("en")) {
                b.cfg.setString("language." + chatid, "ru");
                b.sendMessage(chatid, "Язык установлен на русский");
            } else {
                b.cfg.setString("language." + chatid, "en");
                b.sendMessage(chatid, "Language is English now");
            }
        }
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return user == chat || b.isAdmin(chat, user);
    }
}
