package org.bugbot.tools;

import org.bugbot.BugBot;
import org.bugbot.tools.exception.InvalidNameException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ann {


    private String tkn;
    private String name;
    private Contains contains;


    public Ann(String tkn, String name, Contains contains) {
        this.tkn = tkn;
        this.name = name;
        this.contains = contains;
    }

    public static String handleLinks(String text) {

        return text;
    }

    public String getName() {
        return name;
    }

    public Contains getContains() {
        return contains;
    }

    public String getToken() {
        return tkn;
    }

    /**
     * @return the created ann and saves it in config
     */
    public static Ann create(HashMap<String, Ann> anns, Config cfg, String tkn, String name, Contains contains) {
        Ann ann = new Ann(tkn, name, contains);
        cfg.setStringList("anns." + tkn, cfg.getStringListAndAdd("anns." + tkn, name));
        save(ann, cfg);
        anns.put("#" + ann.getName() + "." + ann.getToken(), ann);
        return ann;
    }

    public static List<Ann> getListOfAnns(BugBot b, String tkn) {
        List<Ann> l = new ArrayList<>();
        for (Ann ann : b.anns.values())
            if (ann.tkn.equals(tkn))
                l.add(ann);
        return l;
    }

    public static List<Ann> getListOfAnnsFromConfig(BugBot b, String tkn) {
        List<String> l = getListOfAnnsString(b.cfg, tkn);
        List<Ann> anns = new ArrayList<>();

        for (String name : l) {
            try {
                anns.add(load(b, tkn, name));
            } catch (InvalidNameException e) {
            }
        }
        return anns;
    }

    /**
     * @return the list of anns in string format
     */
    public static List<String> getListOfAnnsString(Config cfg, String tkn) {
        return cfg.getStringList("anns." + tkn);
    }

    public static String getListOfAnnsStringList(Config cfg, String tkn, String s) {
        List<String> l = getListOfAnnsString(cfg, tkn);
        if (l.size() > 0) {
            StringBuilder sb = new StringBuilder(l.get(0));
            for (int i = 1; i < l.size(); i++)
                sb.append("\n" + s + l.get(i));
            return sb.toString();
        }
        return "";
    }

    public static void load(BugBot b) {

        List<String> tkns = b.cfg.getStringList("tkn");
        for (String tkn : tkns) {
            List<Ann> as = getListOfAnnsFromConfig(b, tkn);
            for (Ann ann : as)
                b.anns.put("#" + ann.getName() + "." + tkn, ann);
        }
    }

    public static InlineKeyboardMarkup getInlineKeyboard(BugBot b, String tkn, int userid, int start, int end) {
        List<Ann> anns = Ann.getListOfAnns(b, tkn);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (int i = start; i < end && i * 2 < anns.size(); i++) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            for (int a = 0; a < 2 && i * 2 + a < anns.size(); a++)
                button.add(new InlineKeyboardButton(anns.get(i * 2 + a).getName()).setCallbackData(anns.get(i * 2 + a).getName() + "." + userid));
            buttons.add(button);
        }
        List<InlineKeyboardButton> arrows = new ArrayList<>();

        arrows.add(new InlineKeyboardButton("<").setCallbackData("nnnstepnnn." + (start - 9) + "." + userid));
        arrows.add(new InlineKeyboardButton(">").setCallbackData("nnnstepnnn." + (end + 9) + "." + userid));

        buttons.add(arrows);
        return new InlineKeyboardMarkup().setKeyboard(buttons);
    }

    /**
     * @param tkn requires valid token
     * @return ann
     * @throws InvalidNameException if there is no name as 'name' throws
     */
    public static Ann load(BugBot b, String tkn, String name) throws InvalidNameException {

        Ann n;
        if ((n = b.anns.get("#" + name + "." + tkn)) != null) return n;

        String s;
        if ((s = b.cfg.getString("#" + name + "." + tkn)) != null) {

            if (s.startsWith("nnntextnnn:")) {
                Ann a = new Ann(tkn, name, new Contains(Contains.ContainType.Text, s.substring("nnntextnnn:".length())));
                b.anns.put("#" + name + "." + tkn, a);
                return a;
            }

            for (int i = 1; i < Contains.ContainType.values().length; i++) {
                Contains.ContainType t;

                if (s.startsWith("nnn" + (t = Contains.ContainType.values()[i]).name().toLowerCase() + "nnn.")) {
                    String[] cont = s.substring(("nnn" + t.name() + "nnn.").length()).split(":", 2);
                    try {
                        Ann a = new Ann(tkn, name, new Contains(t, cont[1], cont[0]));
                        b.anns.put("#" + name + "." + tkn, a);
                        return a;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        Ann a = new Ann(tkn, name, new Contains(t, "", cont[0]));
                        b.anns.put("#" + name + "." + tkn, a);
                        return a;
                    }
                }
            }
            Ann a = new Ann(tkn, name, new Contains(Contains.ContainType.Text, s));
            b.anns.put("#" + name + "." + tkn, a);
            return a;
        } else throw new InvalidNameException(name);

    }

    public static void save(Ann ann, Config cfg) {

        Contains t = ann.getContains();
        if (t.type == Contains.ContainType.Text)
            cfg.setString("#" + ann.getName() + "." + ann.getToken(), "nnntextnnn:" + t.getText());
        else
            cfg.setString("#" + ann.getName() + "." + ann.getToken(),
                    "nnn" + t.getType().name().toLowerCase() + "nnn." + t.getId()
                            + (t.getText() == null ? "" : ":" + t.getText()));
    }
}
