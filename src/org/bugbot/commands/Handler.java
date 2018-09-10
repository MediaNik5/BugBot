package org.bugbot.commands;

import org.bugbot.BugBot;
import org.bugbot.tools.Ann;
import org.bugbot.tools.exception.InvalidNameException;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Handler {


    public static HashMap<String, CMD> cmds = new HashMap<>();


    public static void handle(BugBot b, Update e) {


        if (e.hasChannelPost() && e.getChannelPost().getChat().getUserName().equalsIgnoreCase("@s2_roms")) {
            String[] s = null;
            try {
                s = e.getChannelPost().getText().split("\n");
            } catch (Throwable ex) {
                return;
            }

            String[] st = null;
            try {
                st = s[0].split(" ");
            } catch (Throwable ex) {
                return;
            }

            if (st.length >= 2 && s.length >= 2) {
                if (!st[0].equals("#download"))
                    return;
                if (st[1].startsWith("#"))
                    sendAll(s, st, b, e);
            }
            return;
        }
        if (e.hasCallbackQuery()) {
            long chatid = e.getCallbackQuery().getMessage().getChatId();
            String q = e.getCallbackQuery().getData();
            int userid = e.getCallbackQuery().getFrom().getId();
            if (!q.endsWith(userid + "")) {
                reply(e.getCallbackQuery().getId(), b, b.getStringTyped(chatid + "", "instead"));
                return;
            }
            if (q.startsWith("nnnstepnnn.")) {
                String tkn = b.cfg.getString(e.getCallbackQuery().getMessage().getChatId() + "");
                List<Ann> anns = Ann.getListOfAnns(b, tkn);
                int i = Integer.parseInt(q.split(Pattern.quote("."))[1]);

                if (anns.size() >= i) {
                    reply(e.getCallbackQuery().getId(), b, b.getStringTyped(chatid + "", "nextanns"));
                    b.editMessage(chatid, e.getCallbackQuery().getMessage().getMessageId(), e.getCallbackQuery().getMessage().getText(),
                            Ann.getInlineKeyboard(b, tkn, userid, i / 2, (i + 18) / 2));
                    return;
                } else {
                    reply(e.getCallbackQuery().getId(), b, b.getStringTyped(chatid + "", "noanns"));
                    return;
                }
            }
            Ann n = Ann.load(b, b.cfg.getString(e.getCallbackQuery().getMessage().getChatId() + ""), q.split(Pattern.quote("."))[0]);
            b.sendMessage(chatid, n);
            b.deleteMessage(chatid, e.getCallbackQuery().getMessage().getMessageId());
        }
        if (!e.hasMessage())
            return;
        long chatid = e.getMessage().getChatId();
        String[] s = null;

        if (e.getMessage().getText() != null)
            s = e.getMessage().getText().split(" ");

        else if (e.getMessage().getCaption() != null)
            s = e.getMessage().getCaption().split(" ");
        else return;

        String cmd = getCmd(s[0]);
        if (cmds.containsKey(cmd))
            if (cmds.get(cmd).hasRights(e.getMessage().getFrom().getId(), chatid, b)) {
                cmds.get(cmd).handle(b, e);
                return;
            }

        if (cmd.startsWith("#")) {
            Ann a;
            try {
                a = Ann.load(b, b.cfg.getString(chatid + ""), cmd.substring(1));
            } catch (InvalidNameException e1) {
                return;
            }
            b.sendMessage(chatid, a);
            return;
        }

        String group;
        if ((group = b.cfg.getString(e.getMessage().getFrom().getId() + "")) != null && chatid == e.getMessage().getFrom().getId()) {
            if (b.cfg.getStringList("tkn").contains(s[0])) {
                b.cfg.setStringList(s[0], b.cfg.getStringList(s[0]), s[0]);
                b.cfg.setString(group, s[0]);
                b.cfg.removeString(e.getMessage().getChatId() + "");
                b.sendMessage(chatid, b.getStringTyped(chatid + "", "doneinstall"));
                return;
            }
        }

        List<String> l = getNamesFromText(e.getMessage().getText(), " #", b);
        if (!l.isEmpty()) {
            for (int i = 0; i < 2 && i < l.size(); i++) {
                Ann a;
                try {
                    a = Ann.load(b, b.cfg.getString(chatid + ""), l.get(i));
                } catch (InvalidNameException e1) {
                    continue;
                }
                b.sendMessage(chatid, a);
            }
            return;
        }
    }

    public static List<String> getNamesFromText(String text, String startName, BugBot b) {

        List<String> l = new ArrayList<>();
        String[] t = text.split(startName);

        if (t.length == 1)
            return l;

        for (int i = 1; i < t.length; i++) {
            if (i == 1) {
                if (!b.annConfig.contains(t[0].toLowerCase())) l.add(t[i].split(" ", 2)[0].toLowerCase());

            } else if (!b.annConfig.contains(t[i - 1].split(" ", 2)[1].toLowerCase()))
                l.add(t[i].split(" ", 2)[0].toLowerCase());
        }

        return l;
    }

    private static void reply(String id, BugBot b, String text) {
        AnswerCallbackQuery cbq = new AnswerCallbackQuery();
        cbq.setCallbackQueryId(id);
        cbq.setText(text);

        try {
            b.execute(cbq);
        } catch (TelegramApiException e1) {
            e1.printStackTrace();
        }
    }

    private static String getCmd(String s) {
        try {
            return s.split("@")[0].toLowerCase();
        } catch (Throwable e) {
            return s.toLowerCase();
        }
    }

    private static void sendAll(String[] s, String[] st, BugBot b, Update e) {
        StringBuilder sb = new StringBuilder();
        sb.append("/ann ");
        sb.append(st[1].substring(1) + " ");
        sb.append(s[1]);
        for (int i = 2; i < s.length; i++)
            sb.append("\n" + s[i]);
        String[] some = sb.toString().split(" ");
        List<String> l = b.cfg.getStringList("tkn");
        for (String q : l) {
            List<String> a = b.cfg.getStringList(q);
            for (String w : a)
                if (b.cfg.getString("follow." + w) == null)
                    ann.exe(b, Long.parseLong(w), some, e);
        }
    }


}
