package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class change implements CMD {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void handle(BugBot b, Update e) {


        long chatid = e.getMessage().getChatId();
        String tkn = b.cfg.getString(chatid+"");
        if(tkn != null) {
            String state = b.cfg.getString("follow."+chatid);
            if(state == null){
                b.cfg.setString("follow." + chatid, "false");
                b.sendMessage(chatid, b.getStringTyped(chatid+"", "followoff"));
            } else{
                b.cfg.setString("follow." + chatid, null);
                b.sendMessage(chatid, b.getStringTyped(chatid+"", "followon"));
            }
        }
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return false;
    }
}
