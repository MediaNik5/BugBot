package org.bugbot.tools;

import org.bugbot.tools.exception.InvalidNameException;

import java.util.HashMap;

public class Directive {


    public static Directive ru = new Directive(Language.RU);
    public static Directive en = new Directive(Language.EN);

    HashMap<String, String> strings = new HashMap<>();
    private Language lang;

    public static void init() {
        ru.strings.put("2ndaddchat", "Здесь должен быть второй аргумент(название группы)");
        en.strings.put("2ndaddchat", "There got to be second argument(group name)");

        ru.strings.put("lengthaddchat", "Название группы должно быть меньше 18 символов");
        en.strings.put("lengthaddchat", "Length of group name got to be less than 18 letters");

        ru.strings.put("addchat", "Готово! Вы только что создали ЧАТ-токен, сохраните его и не говорите его никому:");
        en.strings.put("addchat", "Done! You've just created the CHAT token, save it and tell it to nobody:");

        ru.strings.put("install", "Пожалуйста, отправьте ваш токен, чтобы завершить привязку бота");
        en.strings.put("install", "Please, send me your token to finish the linking of bot in group");

        ru.strings.put("alinstall", "Группа уже привязана к ЧАТ-токену");
        en.strings.put("alinstall", "Group is already linked to CHAT-token");

        ru.strings.put("doneinstall", "Удачно привязан к ЧАТ-токену");
        en.strings.put("doneinstall", "Successfully linked to CHAT-token");

        ru.strings.put("faq", "Введите /anns, чтобы увидеть список всех аннотаций, чтобы вызвать одну из них, нажмите на кнопку или введите #название");
        en.strings.put("faq", "Leave /anns to view annotation list, to call one, press button or type #name");

        ru.strings.put("2ndann", "Нужно ввести ещё имя аннотации и её содержимое(видео, текст, фото)");
        en.strings.put("2ndann", "You need enter also name of annotation and its contains(video, text, photo)");

        ru.strings.put("3rdann", "Нужно указать ещё и содержимое аннотации(видео, текст, фото)");
        en.strings.put("3rdann", "You need enter also ann's contains(video, text, photo)");

        ru.strings.put("anndelete", "Аннотация была удалена");
        en.strings.put("anndelete", "The ann has been removed");

        ru.strings.put("ann", "Аннотация добавлена");
        en.strings.put("ann", "The ann has been added");

        ru.strings.put("anns", "Список аннотаций:");
        en.strings.put("anns", "The list of anns:");

        ru.strings.put("instead", "Ты не можешь выбрать не за себя");
        en.strings.put("instead", "You cant choose instead of someone");

        ru.strings.put("noanns", "Больше нет аннотаций");
        en.strings.put("noanns", "You cant choose instead of someone");

        ru.strings.put("kbfalse", "Теперь вы будете получать аннотации в виде списка");
        en.strings.put("kbfalse", "Since now you get anns with list");

        ru.strings.put("kbtrue", "Теперь вы будете получать аннотации в виде клавиатуры");
        en.strings.put("kbtrue", "Since now you get anns with keyboard");

        ru.strings.put("followoff", "Теперь вы не будете получать аннотации с канала @s2_roms");
        en.strings.put("followoff", "Since now you do not get anns from @s2_roms");

        ru.strings.put("followon", "Теперь вы будете получать аннотации с канала @s2_roms");
        en.strings.put("followon", "Since now you get anns from @s2_roms");

        ru.strings.put("2ndno", "Нужно так же вписать строку, на которую бот не будет отвечать при сообщении с #(хештегом)");
        en.strings.put("2ndno", "You need to enter string what bot wont answer to with #(hashtag)");

        ru.strings.put("no", "Исклюлчение добавлено");
        en.strings.put("no", "The exception has been added");

        ru.strings.put("wrongnumber", "Неправильное количество валюты");
        en.strings.put("wrongnumber", "Incorrect value of currency");

        ru.strings.put("wrongcurrency", "Неправильный/ые коды валют. Правильные, к примеру: Usd_RUB, dzd-egp");
        en.strings.put("wrongcurrency", "Incorrect codes of currencies, correct are Usd_RUB, dzd-egp");

        ru.strings.put("ammcurrency", "Правильное использование: '/curr USD-rub' или '/curr usd_eur 10'");
        en.strings.put("ammcurrency", "Correct usage: '/curr USD-rub' or '/curr usd_eur 10'");

        ru.strings.put("currency", "%a %f будет %r %t\nПереведено с помощью currencyconverterapi.com");
        en.strings.put("currency", "%a %f is %r %t\nDone with currencyconverterapi.com");
    }

    private Directive(Language language) {
        lang = language;
    }

    public static Language getLanguage(String chatid, Config cfg) {
        String s = cfg.getString("language." + chatid);
        if (s == null)
            return Language.EN;
        if (s.equalsIgnoreCase(Language.RU.name()))
            return Language.RU;
        //if there will be more languages = more if
        return Language.EN;
    }

    public static Directive getDirective(Language lang) {
        return lang == Language.RU ? ru : en;
    }

    public static Directive getDirective(String chatid, Config cfg) {
        return getDirective(getLanguage(chatid, cfg));
    }

    public String getString(String key) throws InvalidNameException {
        if (key != null)
            return strings.get(key);
        else throw new InvalidNameException(key);
    }

    public enum Language {
        RU(Directive.ru),
        EN(Directive.en);

        Directive dir;

        Language(Directive dir) {
            this.dir = dir;
        }

        public Directive getDir() {
            return dir;
        }
    }
}
