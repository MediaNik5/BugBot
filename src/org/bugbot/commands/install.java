package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class install implements CMD {
    @Override
    public String getName() {
        return "/install";
    }

    @Override
    public void handle(BugBot b, Update e) {

        if(b.cfg.getString(e.getMessage().getChatId()+"")==null) {
            b.cfg.setString(e.getMessage().getFrom().getId() + "", e.getMessage().getChatId() + "");
            b.sendMessage(e.getMessage().getFrom().getId(),
                    b.getStringTyped(e.getMessage().getChatId() + "", "install"));
        }else
            b.sendMessage(e.getMessage().getChatId(),
                    b.getStringTyped(e.getMessage().getChatId() + "", "alinstall"));
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return user != chat && b.isAdmin(chat, user);
    }
}
