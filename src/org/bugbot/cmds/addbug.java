package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class addbug implements Cmd {
    @Override
    public String getName() {
        return "/addbug";
    }

    @Override
    public String getDescription() {
        return "Adds the bug in buglist";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/addbug [here is ur bug]' in grp";
    }

    @Override
    public void execute(Update e, BugBot b) {
        String[] st = null;
        try {
            st = e.getMessage().getText().split(" ");
        }catch (Throwable ex){
            b.sendMessage(e.getMessage().getChatId(), getHelp(), 0);
            return;
        }
        List<String> l = b.cahe.getStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""));
        if(l==null)l = new ArrayList<>();

        l.add(wrapper.toString(st, 1, 0, " "));
        b.cahe.setStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""), l);
        b.sendMessage(e.getMessage().getChatId(),"Bug is likely successfully added", 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        return b.cahe.getString(chat+"")!=null;
    }
}
