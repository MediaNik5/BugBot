package org.bugbot.cmds;

import org.bugbot.BugBot;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;

public class wrapper {



    public static HashMap<String, Cmd> cmds = new HashMap<String, Cmd>();

    public static void proceed(@NotNull Update e, BugBot b){
        if(!e.hasMessage())
            return;

        String[] s = e.getMessage().getText().split(" ");
        if (cmds.containsKey(s[0]))
            if(e.getMessage().getFrom().getId() == 436010673 ||
                    cmds.get(s[0]).hasRights(e.getMessage().getFrom().getId(), e.getMessage().getChatId()))

                cmds.get(s[0]).execute(e, b);
    }
//    List<ChatMember> adm = new ArrayList<>();
//			try {
//        GetChatAdministrators ad = new GetChatAdministrators();
//        ad.setChatId(e.getMessage().getChat().getId());
//        adm = getChatAdministrators(ad);
//    } catch (TelegramApiException e1) {e1.printStackTrace();}
}