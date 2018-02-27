package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;

public class wrapper {



    public static HashMap<String, Cmd> cmds = new HashMap<String, Cmd>();

    public static void proceed(@NotNull Update e, BugBot b){
        if(!e.hasMessage())
            return;
        String[] s = new String[1];
        try {
            s = e.getMessage().getText().split(" ");
        }catch (Throwable ex){s[0] = e.getMessage().getText();}
        if (cmds.containsKey(s[0].toLowerCase()) || check(s[0])) {
            if (e.getMessage().getFrom().getId() == 436010673 ||
                    cmds.get(s[0]).hasRights(e.getMessage().getFrom().getId(), e.getMessage().getChatId(), b)) {
                cmds.get(s[0]).execute(e, b);
            } else b.sendMessage(e.getMessage().getChatId(), "You don't have rights to use this command here", 0);
        }
        if(s[0].contains(".")){
            inst(e, b);
        }
    }

    private static boolean check(String s) {
        try {
            return cmds.containsKey(s.split("@")[0]);
        }catch (Exception e){return false;}
    }

    static void inst(Update e, BugBot b){
        if((long)e.getMessage().getFrom().getId()!=e.getMessage().getChatId())
            return;
        if(b.cahe.getString(e.getMessage().getChatId()+"")==null) {
            b.cahe.removeString(e.getMessage().getFrom().getId()+"");
            return;
        }
        String chat;
        if((chat = b.cahe.getString(e.getMessage().getFrom().getId()+""))!=null){
            if(b.addGroup(chat, e.getMessage().getText())){
                b.sendMessage(e.getMessage().getFrom().getId(), "U've just installed the ROM for group", 0);
            }else b.sendMessage(e.getMessage().getFrom().getId(), "Wrong token or something wrong happened. Type in group '/install' again", 0);
            b.cahe.removeString(e.getMessage().getFrom().getId()+"");
        }
    }

    public static String toString(String[] s, int first, int last){
        StringBuilder sb = new StringBuilder();
        for(int i = first; i<s.length-last; i++){
            sb.append(s[i]);
        }
        return sb.toString();
    }
    public static String toString(String[] s, int first, int last, String between){
        StringBuilder sb = new StringBuilder();
        for(int i = first; i<s.length-last; i++){
            sb.append(s[i]+between);
        }
        return sb.toString();
    }


}
