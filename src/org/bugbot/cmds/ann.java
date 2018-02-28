package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ann implements Cmd {
    @Override
    public String getName() {
        return "/ann";
    }

    @Override
    public String getDescription() {
        return "Adds the annotation under [name] name and with [description] description.";
    }

    @Override
    public String getHelp() {
        return "Usage: leave '/ann [name] [description]' in grp[requires admin else ignores]. Supports reply to message.";
    }

    @Override
    public void execute(Update e, BugBot b) {
        String[] s = new String[2];
        try{
            String[] st = e.getMessage().getText().split(" ");
            if(st.length>2) {
                s[0] = st[1];
                s[1] = wrapper.toString(st, 2, 0, " ");
            }else {
                if (b.cahe.getString("#" + st[1]+"." + b.cahe.getString(e.getMessage().getChatId() + "")) != null) {
                    b.cahe.removeString("#" + st[1] +"."+ b.cahe.getString(e.getMessage().getChatId() + ""));
                    List<String> l = b.cahe.getStringList("anns."+b.cahe.getString(e.getMessage().getChatId()+""));
                    l.remove(st[1]);
                    b.cahe.setStringList("anns."+b.cahe.getString(e.getMessage().getChatId()+""), l);
                    b.sendMessage(e.getMessage().getChatId(), "The ann was deleted likely successfully", 0);
                    return;
                }
                b.sendMessage(e.getMessage().getChatId(), getHelp(), 0);
                return;
            }
        }catch (Throwable ex){
            b.sendMessage(e.getMessage().getChatId(), getHelp(), 0);
            return;
        }
        Message m;
        int reply = 0;
        if((m = e.getMessage().getReplyToMessage())!=null)
            reply = m.getMessageId();
        b.cahe.setString("#"+s[0]+"."+b.cahe.getString(e.getMessage().getChatId()+""), s[1]+":"+reply);
        List<String> l = b.cahe.getStringList("anns."+b.cahe.getString(e.getMessage().getChatId()+""));
        if(l==null)
            l = new ArrayList<>();
        l.remove(s[0]);
        l.add(s[0]);
        b.cahe.setStringList("anns."+b.cahe.getString(e.getMessage().getChatId()+""), l);
        b.sendMessage(e.getMessage().getChatId(), "The ann was added likely successfully", 0);
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
