package com.example.newspoint;

public class News {

    private String title;
    private String image;
    private String link;
    private String description;

    public News() {
    }

    public News(String title, String image, String link, String description) {
        this.title = title;
        this.image = image;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
