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

    }
    @Override
    public Boolean hasRights(int user, long chat) {
        return chat==user;
    }
}
