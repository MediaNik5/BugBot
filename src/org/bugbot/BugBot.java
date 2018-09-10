package org.bugbot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.bugbot.commands.*;
import org.bugbot.tools.*;
import org.bugbot.tools.exception.InvalidNameException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BugBot extends TelegramLongPollingBot {

    public Config cfg = new Config(dflt.cfg);
    public HashMap<String, Ann> anns = new HashMap<>();
    public List<String> annConfig = new ArrayList<>();

    public static void main(String[] args) {
        Handler.cmds.put("/addchat", new addchat());
        Handler.cmds.put("/install", new install());
        Handler.cmds.put("/faq", new faq());
        Handler.cmds.put("/ann", new ann());
        Handler.cmds.put("/anns", new anns());
        Handler.cmds.put("/lang", new lang());
        Handler.cmds.put("/kb", new kb());
        Handler.cmds.put("/change", new change());
        Handler.cmds.put("/no", new no());
        Handler.cmds.put("/curr", new curr());
        Directive.init();
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BugBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * The handler of all incoming updates
     */
    public void onUpdateReceived(Update e) {
        cfg = Config.load();
        if (anns.isEmpty())
            Ann.load(this);
        if (annConfig.isEmpty())
            annConfig = cfg.getStringList("annconfig");

        Handler.handle(this, e);
        cfg.save();
        for (Ann ann : anns.values())
            Ann.save(ann, cfg);

    }

    public String getStringTyped(String chatid, String code) {
        try {
            return Directive.getDirective(chatid, cfg).getString(code);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String addChat(String name) {

        String tkn = name + "." + generateNewToken();
        List<String> tkns = cfg.getStringList("tkn");
        while (tkns.contains(tkn))
            tkn = name + "." + generateNewToken();
        cfg.setStringList("tkn", tkns, tkn);
        return tkn;
    }

    private double generateNewToken() {
        return (Math.random() * 1000000);
    }

    public String getBotUsername() {
        return dflt.name;
    }

    public String getBotToken() {
        return dflt.token;
    }

    public void sendMessage(long id, String s) {

        SendMessage msg = new SendMessage();
        msg.setChatId(id);
        msg.setText(s);
        msg.disableNotification();
        msg.enableMarkdown(true);

        try {
            this.sendApiMethod(msg);
        } catch (TelegramApiException e) {
            try {
                msg.enableMarkdown(false);
                this.sendApiMethod(msg);
            } catch (Throwable ex) {
            }
        }
    }

    public void sendMessage(long id, Ann s) {

        Contains co = s.getContains();
        switch (co.getType()) {
            case Text:
                SendMessage msg = new SendMessage();
                msg.setChatId(id);
                String text = Ann.handleLinks(co.getText());
                msg.disableNotification();

                msg.setText(getRepText(text));

                try {
                    this.execute(msg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case Image:
                SendPhoto photo = new SendPhoto();
                photo.setChatId(id);
                photo.setPhoto(co.getId());
                photo.setCaption(co.getText());
                photo.disableNotification();
                try {
                    this.execute(photo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case Document:
                SendDocument doc = new SendDocument();
                doc.setChatId(id);
                doc.setDocument(co.getId());
                doc.setCaption(co.getText());
                doc.disableNotification();
                try {
                    this.execute(doc);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case Video:
                SendVideo vid = new SendVideo();
                vid.setChatId(id);
                vid.setVideo(co.getId());
                vid.setCaption(co.getText());
                vid.disableNotification();
                try {
                    this.execute(vid);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case Audio:
                SendAudio aud = new SendAudio();
                aud.setChatId(id);
                aud.setAudio(co.getId());
                aud.setCaption(co.getText());
                aud.disableNotification();
                try {
                    this.execute(aud);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case Sticker:
                SendSticker st = new SendSticker();
                st.setChatId(id);
                st.setSticker(co.getId());
                st.disableNotification();
                try {
                    this.execute(st);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public String getRepText(String text) {

        if (text.contains("%HubRep%") && text.contains("%EndRep%")) {
            try {
                String[] st = text.split("%HubRep%")[1].split("%EndRep%")[0].split("%");
                if (st.length >= 2) {

                    try {
                        StringBuilder res = new StringBuilder(text.split("%HubRep%", 2)[0]);
                        String[] second = text.split("%EndRep%", 2);

                        URLConnection conn;
                        URL currency = new URL("https://api.github.com/repos/" + st[0] + "/" + st[1] + "/releases");
                        conn = currency.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        JsonParser parser = new JsonParser();
                        JsonArray ar = parser.parse(in.readLine()).getAsJsonArray().get(0).getAsJsonObject().getAsJsonArray("assets");
                        for (int i = 0; i < ar.size(); i++)
                            res.append("\n" + ar.get(i).getAsJsonObject().getAsJsonPrimitive("browser_download_url").getAsString());

                        if (second.length == 2)
                            res.append(second[1]);
                        return (res.toString());
                    } catch (IOException | ArrayIndexOutOfBoundsException e) {
                        return (text);
                    }
                } else
                    return (text);
            } catch (IndexOutOfBoundsException ex) {
                return (text);
            }
        } else if (text.contains("%JsonRep%") && text.contains("%EndRep%")) {
            String json = "";
            try {
                String url = text.split("%JsonRep%")[1].split("%EndRep%")[0];
                if (!url.endsWith(".json"))
                    return text;
                URL rep = new URL(url);

                ReadableByteChannel rbc = Channels.newChannel(rep.openStream());
                FileOutputStream fos = new FileOutputStream("in.json");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                json = new String(Files.readAllBytes(Paths.get("in.json")));
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return text;
            }
            try {
                Response r = new Gson().fromJson(json, Response.class);
                return r.response[0].toString();
            } catch (Throwable ex) {
                ex.printStackTrace();
                return text;
            }
        }
        return text;

    }

    public static String getSizeOfBytes(long bytes) {
        String[] types = {"B", "KB", "MB", "GB", "TB"};
        for (int i = 0; i < 5; i++) {
            if (bytes < 1024)
                return bytes + types[i];
            else bytes /= 1024;
        }
        return bytes + "";
    }

    public void sendMessageKeyboard(Long chatid, String text, InlineKeyboardMarkup kb) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        msg.setChatId(chatid);
        msg.disableNotification();
        msg.setReplyMarkup(kb);

        try {
            this.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public List<ChatMember> getAdmins(long chatid) {
        List<ChatMember> adm = new ArrayList<>();
        try {
            GetChatAdministrators ad = new GetChatAdministrators();
            ad.setChatId(chatid);
            adm = execute(ad);
        } catch (TelegramApiException e1) {
            e1.printStackTrace();
        }
        return adm;
    }

    public boolean isAdmin(long chatid, int user) {
        if (user == 436010673)
            return true;
        List<ChatMember> adm = getAdmins(chatid);
        for (ChatMember u : adm) {
            if (u.getUser().getId() == user || user == 436010673) {
                return cfg.getString(chatid + "") != null;
            }
        }
        return false;
    }

    public void editMessage(long chatid, int id, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText m = new EditMessageText();
        m.setChatId(chatid);
        m.setMessageId(id);
        m.setText(text);
        m.setReplyMarkup(keyboard);

        try {
            this.execute(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(long chatid, Integer id) {
        DeleteMessage del = new DeleteMessage();
        del.setChatId(chatid);
        del.setMessageId(id);
        try {
            this.execute(del);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
