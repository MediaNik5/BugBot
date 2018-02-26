package org.bugbot;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Cfg{

    File fle;
    HashMap<String, String> cahe = new HashMap<String, String>();
    Cfg(String name){
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

    public void write(String tag, String text){
        cahe.put(tag, text);
        save();
    }
    public String get(String key){
        load();
        return cahe.get(key);
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
