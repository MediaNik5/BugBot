package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

public class change implements Cmd {
    @Override
    public String getName() {
        return "/change";
    }

    @Override
    public String getDescription() {
        return "Changes the following state";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/change'";
    }

    @Override
    public void execute(Update e, BugBot b) {
        long chat = e.getMessage().getChatId();
        if(b.cahe.getString("follow."+chat)==null){
            b.cahe.setString("follow."+chat, "sell");
            b.sendMessage(chat, "Following state is off now", 0);
        }else {
            b.cahe.removeString("follow."+chat);
            b.sendMessage(chat, "Following state is on now", 0);
        }
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
