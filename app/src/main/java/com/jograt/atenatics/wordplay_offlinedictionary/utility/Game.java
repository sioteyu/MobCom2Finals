package com.jograt.atenatics.wordplay_offlinedictionary.utility;

/**
 * Created by it.admin on 9/16/2017.
 */

public class Game {
    private int image;
    private String title;
    private String description;

    public Game(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
