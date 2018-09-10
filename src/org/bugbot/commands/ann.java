package org.bugbot.commands;

import org.bugbot.BugBot;
import org.bugbot.tools.Ann;
import org.bugbot.tools.Contains;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ann implements CMD {
    @Override
    public String getName() {
        return "/ann";
    }

    @Override
    public void handle(BugBot b, Update e) {
        try {
            exe(b, e.getMessage().getChatId(), e.getMessage().getText().split(" ", 3), e);
        }catch (Throwable ex){
            try {
                exe(b, e.getMessage().getChatId(), e.getMessage().getCaption().split(" ", 3), e);
            }catch (Throwable ex1){
                b.sendMessage(e.getMessage().getChatId(), b.getStringTyped(""+e.getMessage().getChatId(), "2ndann"));
                return;
            }
        }
    }

    public static void exe(BugBot b, long chatid, String[] s, Update e){
//        String[] s = e.getMessage().getText().split(" ");
//        long chatid = e.getMessage().getChatId();
        String tkn = b.cfg.getString(chatid+"");
        if(tkn != null){
            boolean text = s.length>2;
            if(e.getMessage().hasDocument())
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Document, text ? s[2] : "", e.getMessage().getDocument().getFileId()));

            else if(e.getMessage().isReply() && e.getMessage().getReplyToMessage().hasDocument())
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Document, text ? s[2] : "", e.getMessage().getReplyToMessage().getDocument().getFileId()));

            else if(e.getMessage().hasPhoto())
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Image, text ? s[2] : "", e.getMessage().getPhoto().get(0).getFileId()));

            else if(e.getMessage().isReply() && e.getMessage().getReplyToMessage().hasPhoto())
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Image, text ? s[2] : "", e.getMessage().getReplyToMessage().getPhoto().get(0).getFileId()));

            else if(e.getMessage().hasVideo())
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Video, text ? s[2] : "", e.getMessage().getVideo().getFileId()));

            else if(e.getMessage().isReply() && e.getMessage().getReplyToMessage().hasVideo())
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Video, text ? s[2] : "", e.getMessage().getReplyToMessage().getVideo().getFileId()));

            else if(e.getMessage().getAudio() != null)
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Audio, text ? s[2] : "", e.getMessage().getAudio().getFileId()));

            else if(e.getMessage().isReply() && e.getMessage().getReplyToMessage().getAudio() !=null)
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Audio, text ? s[2] : "", e.getMessage().getReplyToMessage().getAudio().getFileId()));

            else if(e.getMessage().isReply() && e.getMessage().getReplyToMessage().getSticker() != null) {
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Sticker, text ? s[2] : "", e.getMessage().getReplyToMessage().getSticker().getFileId()));
            } else {
                if(!text){
                    if(b.anns.containsKey(s[1]) || b.cfg.getString("#" + s[1] + "." + tkn) != null) {
                        b.anns.remove("#"+s[1]+"."+tkn);
                        b.cfg.removeString("#" + s[1] + "." + tkn);
                        List<String> l = b.cfg.getStringList("anns." + tkn);
                        l.remove(s[1]);
                        b.cfg.setStringList("anns." + tkn, l);
                        b.sendMessage(chatid, b.getStringTyped("" + chatid, "anndelete"));
                        return;
                    }
                        b.sendMessage(chatid, b.getStringTyped("" + chatid, "3rdann"));
                        return;
                }
                Ann.create(b.anns, b.cfg, tkn, s[1].toLowerCase(), new Contains(Contains.ContainType.Text, s[2]));
            }
            b.sendMessage(chatid, b.getStringTyped(chatid+"", "ann"));
        }
    }
    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return user != chat && b.isAdmin(chat, user);
    }
}
