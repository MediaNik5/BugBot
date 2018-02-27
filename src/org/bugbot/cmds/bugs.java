package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

public class bugs implements Cmd {
    @Override
    public String getName() {
        return "/bugs";
    }

    @Override
    public String getDescription() {
        return "Shows u the buglist";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/bugs' in group";
    }

    @Override
    public void execute(Update e, BugBot b) {
        List<String> l = b.cahe.getStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""));
        if(l==null){
            b.sendMessage(e.getMessage().getChatId(), "There isn't bugs!", 0);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<l.size();i++)
            sb.append((i+1)+") "+l.get(i)+"\n");

        b.sendMessage(e.getMessage().getChatId(), sb.toString(), 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        return b.cahe.getString(chat+"")!=null;
    }
}
