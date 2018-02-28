package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

public class anns implements Cmd {
    @Override
    public String getName() {
        return "/anns";
    }

    @Override
    public String getDescription() {
        return "Sends u annlist";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/anns' in grp.";
    }

    @Override
    public void execute(Update e, BugBot b) {
        List<String> l = b.cahe.getStringList("anns."+b.cahe.getString(e.getMessage().getChatId()+""));
        if(l==null){
            b.sendMessage(e.getMessage().getChatId(), "There aren't anns!", 0);
            return;
        }
        if(l.size()==0){
            b.sendMessage(e.getMessage().getChatId(), "There aren't anns!", 0);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(String s : l)
            sb.append(s+"\n");

        b.sendMessage(e.getMessage().getChatId(), "List of existing anns:\n"+sb.toString(), 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        return true;
    }
}
