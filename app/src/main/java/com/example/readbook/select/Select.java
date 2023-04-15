package com.example.readbook.select;

public class Select {

    private int img;
    private String description;

    public Select(int img, String description) {
        this.img = img;
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
