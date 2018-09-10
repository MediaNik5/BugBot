package org.bugbot.tools;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.IOException;
import java.util.List;

public class Contains {

    ContainType type;
    String text = "";
    String id;
    InlineKeyboardMarkup kb = null;
    public Contains(ContainType type, String text){
        this(type, text, "");
    }

    public void setKeyboard(InlineKeyboardMarkup kb){
        this.kb = kb;
    }
    public boolean isKeyboard(){
        return kb != null;
    }
    /**
     *
     * @param type Type of content
     * @param text if document/image/document/video has text
     * @param id id of document/image/document/video if needed
     */
    public Contains(ContainType type, String text, String id){
        if(type==null)
            throw new IllegalArgumentException("type cannot be null");
        this.type = type;
        if(type == ContainType.Text){
            if(text != null & !text.isEmpty()) {
                this.text = text;
            } else throw new IllegalArgumentException("text cannot be empty for this ContainType: Text");
        }else{
            if(id != null & !id.isEmpty())
                this.id = id;
            else throw new IllegalArgumentException("id cannot be empty for this ContainType: " + type.name());

            if(text != null & !text.isEmpty())
                this.text = text;
        }
    }

    public ContainType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public enum ContainType{
        Text,
        Image,
        Document,
        Video,
        Sticker,
        Audio
    }
}
