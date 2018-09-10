package org.bugbot.tools;

import com.google.gson.Gson;
import org.bugbot.BugBot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {

    File fle;
    HashMap<String, String> strings = new HashMap<>();
    HashMap<String, List<String>> lists = new HashMap<>();

    public Config(String name){
        fle = new File(BugBot.class.getProtectionDomain().getCodeSource().getLocation().getPath()+dflt.cfg);
        try {
            if(!fle.exists()) fle.createNewFile();
        } catch (IOException e) {e.printStackTrace();}
        load();

    }

    /**@param key The key
     * @return may return null
     */
    public String getString(String key){
        return strings.get(key);
    }
    /**@param key The key
     * @return may return empty list
     */
    public List<String> getStringList(String key){
        List<String> l = lists.get(key);
        return l == null ? new ArrayList<>() : l;
    }
    public List<String> getStringListAndAdd(String key, String... args){
        List l = getStringList(key);
        for(String a : args) {
            if(l.contains(a))
                l.remove(a);
            l.add(a);
        }
        return l;
    }

    public void toJson(){

    }

    public static Config load() {
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get((BugBot.class.getProtectionDomain().getCodeSource().getLocation().getPath()+dflt.cfg))));
        } catch (IOException e) {e.printStackTrace(); json = new String();}

        return new Gson().fromJson(json, Config.class);
//        String keyOfString = "";
//        List<String> l = null;
//        for(String q : json){
//            if(!q.contains(":") && q.endsWith("{") && keyOfString.equals("")){
//                keyOfString = q;
//                l = new ArrayList<>();
//                continue;
//            }
//            if(q.startsWith("}")){
//                setStringList(keyOfString.substring(0, keyOfString.length()-1), l);
//                keyOfString = "";
//                continue;
//            }
//            try {
//                if (!keyOfString.equals("")) l.add(revert(q.replace("\t", "")));
//                else if(q != null && !q.isEmpty())setString(revert(q.split(":")[0]), revert(q.split(":")[1]));
//            }catch (Throwable e){e.printStackTrace();}
//        }
    }
    public void save(){

        String json = new Gson().toJson(this);

//        StringBuilder sb = new StringBuilder();
//
//        for(String key : strings.keySet()){
//            sb.append(key+":"+optimize(strings.get(key)));
//            sb.append("\n");
//        }
//        for(String key : lists.keySet()){
//            sb.append(key+"{\n");
//            for(String s : lists.get(key))
//                sb.append("\t"+optimize(s)+"\n");
//            sb.append("}\n");
//        }
        try {
            BufferedWriter r = new BufferedWriter(new FileWriter(
                    new File((BugBot.class.getProtectionDomain().getCodeSource().getLocation().getPath()+dflt.cfg))));
            r.write(json);
            r.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    @Deprecated
    public String optimize(String s){
        s = s.replaceAll("\n", "nnnreplacennn");
        return s.replaceAll(":", "colonreplacecolon");
    }
    @Deprecated
    public String revert(String s){
        s = s.replaceAll("nnnreplacennn", "\n");
        return s.replaceAll("colonreplacecolon", ":");
    }

    public void setStringList(String key, List<String> l, String... args){
        if(strings.keySet().contains(key))
            strings.remove(key);
        for(String s : args)
            l.add(s);
        lists.put(key, l);
    }
    public void setString(String key, String s){
        if(lists.keySet().contains(key))
            lists.remove(key);
        strings.put(key, s);
    }
    public void removeString(String key){
        if(strings.keySet().contains(key))
            strings.remove(key);
    }

}
