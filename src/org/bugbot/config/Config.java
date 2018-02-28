package org.bugbot.config;

import org.bugbot.BugBot;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {



    File fle;
    HashMap<String, String> strings = new HashMap<>();
    HashMap<String, List<String>> lists = new HashMap<>();

    public Config(String name){
        fle = new File(BugBot.class.getProtectionDomain().getCodeSource().getLocation().getPath() + name);

        if(!fle.exists()){
            try {
                fle.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        load();

    }
    @Nullable
    public String getString(String key){
        load();
        return strings.get(key);
    }
    public List<String> getStringList(String key){
        load();
        return lists.get(key);
    }
    private void load() {
        try(FileReader reader = new FileReader(fle.getAbsoluteFile())){
            int c;
            StringBuilder cache = new StringBuilder();
            while((c=reader.read())!=-1){
                cache.append((char)c);
            }
            String[] s = cache.toString().split("\n");
            String keyOfString = "";
            List<String> l = null;
            for(String q : s){
                if(!q.contains(":") && q.endsWith("{") && keyOfString.equals("")){
                    keyOfString = q;
                    l = new ArrayList<>();
                    continue;
                }
                if(q.startsWith("}")){
                    setStringList(keyOfString.substring(0, keyOfString.length()-1), l);
                    keyOfString = "";
                    continue;
                }
                try {
                    if (!keyOfString.equals("")) l.add(revert(q.replace("\t", "")));
                    else setString(revert(q.split(":")[0]), revert(q.split(":")[1]));
                }catch (Throwable e){}
            }
        }catch(IOException ex){}
    }
    private void save(){
        StringBuilder sb = new StringBuilder();

        for(String key : strings.keySet()){
            sb.append(key+":"+optimize(strings.get(key)));
            sb.append("\n");
        }
        for(String key : lists.keySet()){
             sb.append(key+"{\n");
             for(String s : lists.get(key))
                sb.append("\t"+optimize(s)+"\n");
             sb.append("}\n");
        }
        try(FileWriter writer = new FileWriter(fle.getAbsoluteFile(), false)){
            writer.write(sb.toString());
            writer.flush();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    public String optimize(String s){
        s = s.replaceAll("\n", "nnnreplacennn");
        s = s.replaceAll(":", "colonreplacecolon");
        return s;
    }
    public String revert(String s){
        s = s.replaceAll("nnnreplacennn", "\n");
        s = s.replaceAll("colonreplacecolon", ":");
        return s;
    }
    public void setStringList(String key, List<String> l){
        if(strings.keySet().contains(key))
            strings.remove(key);
        lists.put(key, l);
        save();
    }
    public void setString(String key, String s){
        if(lists.keySet().contains(key))
            lists.remove(key);
        strings.put(key, s);
        save();
    }
    public void removeString(String key){
        if(strings.keySet().contains(key))
            strings.remove(key);
        save();
    }
}
