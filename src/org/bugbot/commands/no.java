package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class no implements CMD {
    @Override
    public String getName() {
        return "/no";
    }

    @Override
    public void handle(BugBot b, Update e) {
        long chatid = e.getMessage().getChatId();
        if (b.cfg.getString(chatid + "") == null)
            return;
        String[] t = e.getMessage().getText().split(" ", 2);
        if (t.length == 1) {
            b.sendMessage(e.getMessage().getChatId(), b.getStringTyped(e.getMessage().getChatId() + "", "2ndno"));
            return;
        }
        b.annConfig.add(t[1].toLowerCase());
        b.cfg.setStringList("annconfig", b.annConfig);
        b.sendMessage(chatid, b.getStringTyped(chatid + "", "no"));
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return chat != user && b.isAdmin(chat, user);
    }
}
