package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class kb implements CMD {
    @Override
    public String getName() {
        return "/kb";
    }

    @Override
    public void handle(BugBot b, Update e) {
        long chatid = e.getMessage().getChatId();
        String tkn = b.cfg.getString(chatid+"");
        if(tkn != null) {
            String state = b.cfg.getString("kb."+chatid);
            if(state == null || state.equals("true")) {
                b.cfg.setString("kb." + chatid, "false");
                b.sendMessage(chatid, b.getStringTyped(chatid+"", "kbfalse"));
            } else{
                b.cfg.setString("kb." + chatid, "true");
                b.sendMessage(chatid, b.getStringTyped(chatid+"", "kbtrue"));
            }
        }
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return b.isAdmin(chat, user);
    }
}
