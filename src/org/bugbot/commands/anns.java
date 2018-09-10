package org.bugbot.commands;

import org.bugbot.BugBot;
import org.bugbot.tools.Ann;
import org.telegram.telegrambots.meta.api.objects.Update;

public class anns implements CMD {
    @Override
    public String getName() {
        return "/anns";
    }

    @Override
    public void handle(BugBot b, Update e) {
        String tkn = b.cfg.getString(e.getMessage().getChatId()+"");
        if(tkn == null)
            return;
        String s = b.cfg.getString("kb."+e.getMessage().getChatId());
        if(s == null || s.equals("true"))
            b.sendMessageKeyboard(e.getMessage().getChatId(),
                    b.getStringTyped(e.getMessage().getChatId()+"", "anns"),
                    Ann.getInlineKeyboard(b, tkn, e.getMessage().getFrom().getId(), 0, 9));
        else
            b.sendMessage(e.getMessage().getChatId(), b.getStringTyped(e.getMessage().getChatId()+"", "anns") + "\n" + Ann.getListOfAnnsStringList(b.cfg, tkn, "/"));
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return chat != user;
    }
}
