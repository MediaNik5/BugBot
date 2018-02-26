package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

public interface Cmd {
    public String getName();
    public String getDescription();
    public String getHelp();
    public void execute(Update e, BugBot b);
    public Boolean hasRights(int user, long chat);
}
