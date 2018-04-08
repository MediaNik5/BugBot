package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class wrapper {



    public static HashMap<String, Cmd> cmds = new HashMap<String, Cmd>();

    public  static void proceedChannel(Update e, BugBot b){
        String[] s = null;
        try {
            s = e.getChannelPost().getText().split("\n");
        }catch (Throwable ex){return;}

        String[] st = null;
        try {
            st = s[0].split(" ");
        }catch (Throwable ex){return;}

        if(st.length>=2&&s.length>=2){
            if(!st[0].equals("#download"))
                return;
            if(st[1].startsWith("#"))
                sendAll(s, st, b);
        }
    }

    private static void sendAll(String[] s, String[] st, BugBot b) {
        StringBuilder sb = new StringBuilder();
        sb.append("/ann ");
        sb.append(st[1].substring(1)+" ");
        sb.append(s[1]);
        for (int i = 2; i<s.length; i++)
            sb.append("\n"+s[i]);
        String[] some = sb.toString().split(" ");
        List<String> l = b.cahe.getStringList("tkn");
        for (String q : l){
            List<String> a = b.cahe.getStringList(q);
            if(a==null)a = new ArrayList<>();
            for(String w : a) {
                if(b.cahe.getString("follow."+w)==null)
                    ann.exe(Long.parseLong(w), some, null, b);
            }
        }
    }

    public static void proceed(@NotNull Update e, BugBot b){
        String[] s = new String[1];
        try {
            s = e.getMessage().getText().split(" ");
        }catch (Throwable ex){s[0] = e.getMessage().getText();}
        if (cmds.containsKey(s[0].toLowerCase()) || check(s[0])) {
            if (e.getMessage().getFrom().getId() == 436010673 ||
                    cmds.get(gte(s[0])).hasRights(e.getMessage().getFrom().getId(), e.getMessage().getChatId(), b)) {
                cmds.get(gte(s[0])).execute(e, b);
            } else b.sendMessage(e.getMessage().getChatId(), "You don't have rights to use this command here", 0);
        }
        if(s[0].contains(".")){
            inst(e, b);
        }
    }

    private static String gte(String s) {
        try {
            return s.split("@")[0].toLowerCase();
        }catch (Exception e){return s.toLowerCase();}
    }
    private static boolean check(String s) {
        try {
            return cmds.containsKey(s.split("@")[0].toLowerCase());
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


    public static void proceedQuery(Update e, BugBot b) {

    }
}
