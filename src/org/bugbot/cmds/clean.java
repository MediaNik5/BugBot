package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

public class clean implements Cmd {
    @Override
    public String getName() {
        return "/clean";
    }

    @Override
    public String getDescription() {
        return "Cleans the bugs in group";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/clean' in group[requires admin else ignores]";
    }

    @Override
    public void execute(Update e, BugBot b) {
        List<String> l = b.cahe.getStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""));
        if(l==null){
            b.sendMessage(e.getMessage().getChatId(), "There aren't bugs!", 0);
            return;
        }
        if(l.size()==0){
            b.sendMessage(e.getMessage().getChatId(), "There aren't bugs!", 0);
            return;
        }
        l.clear();
        b.cahe.setStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""), l);
        b.sendMessage(e.getMessage().getChatId(), "Buglist was cleared", 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        List<ChatMember> adm = b.getAdmins(chat);
        for(ChatMember u : adm) {
            if (u.getUser().getId() == user || user == 436010673) {
                return b.cahe.getString(chat + "") != null;
            }
        }
        return false;
    }
}
