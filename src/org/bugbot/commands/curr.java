package org.bugbot.commands;

import org.bugbot.BugBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class curr implements CMD {
    @Override
    public String getName() {
        return "/curr";
    }

    @Override
    public void handle(BugBot b, Update e) {

        long chatid = e.getMessage().getChatId();
        String[] text = e.getMessage().getText().split(" ");

        if(text.length>=2){
            if(text[1].length() == 7){
                String s = text[1].substring(0, 3).toUpperCase() + "_" + text[1].substring(4, 7).toUpperCase();
                URLConnection conn;
                String summ = null;

                try {
                    URL currency = new URL("http://free.currencyconverterapi.com/api/v5/convert?q="+s+"&compact=y");
                    conn = currency.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    summ = in.readLine();
                    in.close();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if(summ == null || summ.equals("{}")){
//                    try {
//                        URL currency = new URL("http://free.currencyconverterapi.com/api/v5/convert?q="+s.substring(0, 3)+"_USD&compact=y");
//                        conn = currency.openConnection();
//                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                        String inUsd = in.readLine();
//                        in.close();
//
//                        currency = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=USD_"+s.substring(4, 7)+"&compact=y");
//                        conn = currency.openConnection();
//                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                        String inRes = in.readLine();
//                        in.close();
//
//                        try {
//                            summ = (Float.parseFloat(inUsd.substring(18, summ.length() - 2)) * Float.parseFloat(inRes.substring(18, summ.length() - 2)))+ "";
//                        }catch (Throwable ex){
//                            ex.printStackTrace();
//                            b.sendMessage(chatid, b.getStringTyped(chatid + "", "wrongcurrency"));
//                            return;
//                        }
//
//                    } catch (MalformedURLException e1) {
//                        e1.printStackTrace();
//                        b.sendMessage(chatid, b.getStringTyped(chatid + "", "wrongcurrency"));
//                        return;
//
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                        b.sendMessage(chatid, b.getStringTyped(chatid + "", "wrongcurrency"));
//                        return;
//                    }
                    b.sendMessage(chatid, b.getStringTyped(chatid + "", "wrongcurrency"));
                    return;
                }

                float f = Float.parseFloat(summ.substring(18, summ.length() - 2));
                try {
                    if(text.length>=3)
                        f *= Float.parseFloat(text[2]);
                }catch(Throwable ex){
                    b.sendMessage(chatid, b.getStringTyped(chatid + "", "wrongnumber"));
                }

                b.sendMessage(chatid, b.getStringTyped(chatid+"", "currency")
                        .replaceAll("%a", text.length>=3 ? text[2] : "1")
                        .replaceAll("%f", s.substring(0, 3))
                        .replaceAll("%t", s.substring(4, 7))
                        .replaceAll("%r", ""+f));
                return;
            }else {
                b.sendMessage(chatid, b.getStringTyped(chatid + "", "wrongcurrency"));
                return;
            }
        }else{
            b.sendMessage(chatid, b.getStringTyped(chatid + "", "ammcurrency"));
            return;
        }
    }

    @Override
    public boolean hasRights(int user, long chat, BugBot b) {
        return true;
    }
}
