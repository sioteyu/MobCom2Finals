package com.jograt.atenatics.wordplay_offlinedictionary.utility;

/**
 * Created by it.admin on 9/16/2017.
 */

public class Word {
    private String description;
    private String part;

    public Word(String part, String description){
        this.part = part;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
