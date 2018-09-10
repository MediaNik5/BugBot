package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class faq implements CMD {
    @Override
    public String getName() {
        return "/faq";
    }

    @Override
    public void handle(BugBot b, Update e) {
        if ((long) e.getMessage().getChatId() == e.getMessage().getFrom().getId())
            b.sendMessage(e.getMessage().getChatId(), "Create your frimeware: command '/addrom [name]' in pm. Get your token.\n" +
                    "Go to your group and leave /install. Send to bot that token, That's it!");
        else
            b.sendMessage(e.getMessage().getChatId(), b.getStringTyped(e.getMessage().getChatId() + "", "faq"));
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return true;
    }
}
