package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.telegram.telegrambots.api.objects.Update;

public class help implements Cmd {
    @Override
    public String getName() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "Helps you";
    }

    @Override
    public String getHelp() {
        return "/help [command] - help you about [command]./help - help you at all";
    }

    @Override
    public void execute(Update e, BugBot b) {
        String[] a = null;
        try {
            a = e.getMessage().getText().split(" ");
            if(wrapper.cmds.keySet().contains("/"+a[1])){
                b.sendMessage(e.getMessage().getChatId(), a[1]+": "+wrapper.cmds.get("/"+a[1]).getDescription()+"\n"+wrapper.cmds.get("/"+a[1]).getHelp(), 0);
                return;
            }
            try {
                fullHelp(e, b);
            }catch (Throwable ex1){}

        }catch (Throwable ex){
            try {
                fullHelp(e, b);
            }catch (Throwable ex1){}
        }
    }

    private void fullHelp(Update e, BugBot b) {


        StringBuilder sb = new StringBuilder();

        for (Cmd c : wrapper.cmds.values()){
            sb.append(c.getName().substring(1)+": "+c.getDescription());
            sb.append('\n');
            sb.append(c.getHelp());
            sb.append('\n');
            sb.append('\n');
        }
        b.sendMessage(e.getMessage().getChatId(), sb.toString(), 0);
    }

    @Override
    public Boolean hasRights(int user, long chat, BugBot b) {
        return true;
    }
}
