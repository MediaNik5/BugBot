package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class delbug implements Cmd {
    @Override
    public String getName() {
        return "/delbug";
    }

    @Override
    public String getDescription() {
        return "Deletes a bug by its number";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/delbug [id]' in grp[requires admin else ignores]";
    }

    @Override
    public void execute(Update e, BugBot b) {
        List<String> l = b.cahe.getStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""));
        if(l==null)l = new ArrayList<>();

        try{
            l.remove(Integer.parseInt(e.getMessage().getText().split(" ")[1])-1);
            b.cahe.setStringList("bugs."+b.cahe.getString(e.getMessage().getChatId()+""), l);
            b.sendMessage(e.getMessage().getChatId(), "The bug is likely was removed", 0);
        }catch (Throwable ex){
            b.sendMessage(e.getMessage().getChatId(), "Something went wrong, check the number or existing of the bug.", 0);
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
