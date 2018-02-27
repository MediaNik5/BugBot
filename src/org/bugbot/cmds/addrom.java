package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

public class addrom implements Cmd {
    @Override
    public String getName() {
        return "/addrom";
    }

    @Override
    public String getDescription() {
        return "Creates a token for your rom";
    }

    @Override
    public String getHelp() {
        return "Usage: send in pm '/addrom [rom_name]' and get the token.";
    }

    @Override
    public void execute(Update e, BugBot b) {
        if (e.getMessage().getText().split(" ").length == 1) {
            b.sendMessage(e.getMessage().getFrom().getId(), getHelp(), 0);
            return;
        }
        b.sendMessage(e.getMessage().getChatId() , "Nice! You've just created the ROM token, save it and tell it to nobody(Plox):\n\n" +
                b.addRom(e.getMessage().getText().split(" ")[1]), 0);
    }
    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        return chat==user;
    }
}
