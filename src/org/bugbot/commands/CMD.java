package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CMD {

    public String getName();

    public void handle(BugBot b, Update e);

    public boolean hasRights(int user, long chat, BugBot b);
}
