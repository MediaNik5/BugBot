package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

public class faq implements Cmd {
    @Override
    public String getName() {
        return "/faq";
    }

    @Override
    public String getDescription() {
        return "FAQ of all what happening here";
    }

    @Override
    public String getHelp() {
        return "Usage: /faq";
    }

    @Override
    public void execute(Update e, BugBot b) {
        b.sendMessage(e.getMessage().getChatId(), "Create your frimeware: command '/addrom [name]' in pm. Get your token.\n" +
                "Go to your group and leave /install. Send to bot that token, That's it!", 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        return true;
    }
}
