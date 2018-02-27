package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

public class install implements Cmd{
    @Override
    public String getName() {
        return "/install";
    }

    @Override
    public String getDescription() {
        return "Starting the install of grp for current rom. DO NOT INSTALL TWICE OR MORE TIMES";
    }

    @Override
    public String getHelp() {
        return "Usage: send '/install' to the group and send to bot in pm token";
    }

    @Override
    public void execute(Update e, BugBot b) {
        b.cahe.setString(e.getMessage().getFrom().getId()+"", e.getMessage().getChatId()+"");
        b.sendMessage(e.getMessage().getFrom().getId(),
                "Please, send me ur token to finish the installing of bot in grp", 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        List<ChatMember> adm = b.getAdmins(chat);
        for(ChatMember u : adm){
            if(u.getUser().getId()==user || user==436010673){
                return user!=chat;
            }
        }
        return false;
    }
}
