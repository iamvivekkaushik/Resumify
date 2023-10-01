package com.vivekkaushik.resumify.model;

public class Template {
    private int id;
    private int image;

    public Template(int id, int image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }
}
