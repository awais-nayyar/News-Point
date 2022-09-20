package com.example.newspoint;

public class SliderItems {
    private String image;
    private String title;

    public SliderItems() {
    }

    public SliderItems(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
