package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class addchat implements CMD {
    @Override
    public String getName() {
        return "/addchat";
    }

    @Override
    public void handle(BugBot b, Update e) {

        String[] c = e.getMessage().getText().split(" ");
        long chatid = e.getMessage().getChatId();
        if (c.length == 1) {
            b.sendMessage(chatid, b.getStringTyped(chatid + "", "2ndaddchat"));
        } else if (c[1].length() >= 18) {
            b.sendMessage(chatid, b.getStringTyped(chatid + "", "lengthaddchat"));
        } else {
            String tkn = b.addChat(c[1]);
            b.cfg.setStringList("tkn", b.cfg.getStringList("tkn"), tkn);
            b.sendMessage(chatid, b.getStringTyped(chatid + "", "addchat") + "\n\n```" + tkn + "```");
        }
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return (long) user == chat;
    }
}
