package org.bugbot;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class Cfg{

    File fle;
    HashMap<String, String> cahe = new HashMap();
    @Deprecated
    public Cfg(String name){
        fle = new File(BugBot.class.getProtectionDomain().getCodeSource().getLocation().getPath() + name);
        if(!fle.exists()){
            try {
                fle.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        write("");
    }
    @Deprecated
    public void write(String tag, String text){
        cahe.put(tag, text);
        save();
    }
    @Deprecated
    public String get(String key){
        load();
        return cahe.get(key);
    }
    @Deprecated
    public HashMap<String, List<String>> updateFromRoms(){
        load();
        HashMap<String, List<String>> hm = new HashMap<>();

        return hm;
    }
    @Deprecated
    public HashMap<String, HashMap<String, List<String>>> updateFromGrps(){
        load();
        HashMap<String, HashMap<String, List<String>>> hm = new HashMap<>();
        return hm;
    }
    @Deprecated
    public void update(HashMap<String, List<String>> roms, HashMap<String, HashMap<String, List<String>>> grps){
        load();
        for(String key : roms.keySet()){
            List<String> l = roms.get(key);
            cahe.put("tkn."+key, "");

            for(String s : l){
                cahe.put(key+"."+s, "");
                cahe.put(s+"."+key, "");
            }
        }
        int i=0;
        for(String key : grps.keySet()){
            HashMap<String, List<String>> hm = grps.get(key);
            for(String k : hm.keySet()){
                for(String s : hm.get(k)){
                    cahe.put(k+"."+hm.get("tkn").get(0)+"."+i, s);
                    i++;
                }
            }
        }
        save();
    }
    private void load() {
        try(FileReader reader = new FileReader(fle.getAbsoluteFile())){
            int c;
            StringBuilder cache = new StringBuilder();
            while((c=reader.read())!=-1){
                cache.append((char)c);
            }
            String[] s = cache.toString().split("\n");
            for(String q : s){
                cahe.put(q.split(":")[0], q.split(":")[1].replaceAll("nnnreplacennn", "\n"));
            }
        }catch(IOException ex){}
    }
    private void save(){
        StringBuilder sb = new StringBuilder();
        for(String key : cahe.keySet()){
            if(!cahe.get(key).isEmpty())
                sb.append((key+":"+cahe.get(key)).replaceAll("\n", "nnnreplacennn"));
                sb.append("\n");
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
    @Deprecated
    public void write(@NotNull String text) {
        try(FileWriter writer = new FileWriter(fle.getAbsoluteFile(), true)){
            writer.write(text + '\n');
            writer.flush();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

}
