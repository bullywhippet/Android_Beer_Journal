package com.kellydwalters.thebeerjournal;

/**
 * Models a list item for found beers
 */

public class ListItem {
    private String name, abv, description, image;

    public ListItem() {
    }

    public ListItem(String name, String abv, String description, String image) {
        this.name = name;
        this.abv = abv;
        this.description = description;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
