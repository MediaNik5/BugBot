package org.bugbot;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class cfg{

    File fle;
    HashMap<String, String> cahe = new HashMap<String, String>();
    cfg(String name){
        fle = new File(BugBot.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "cache.cfg");
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
        return cahe.get(key);
    }
    private void save(){
        StringBuilder sb = new StringBuilder();
        for(String key : cahe.keySet()){
            if(!cahe.get(key).isEmpty())
                sb.append(key+":"+cahe.get(key));
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
