package com.example.newspoint;

public class Adds {

    private int id;
    private String bannerAdd;
    private String interstitialAdd;
    private int addInterval;

    public Adds() {
    }

    public Adds(int id, String bannerAdd, String interstitialAdd, int addInterval) {
        this.id = id;
        this.bannerAdd = bannerAdd;
        this.interstitialAdd = interstitialAdd;
        this.addInterval = addInterval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBannerAdd() {
        return bannerAdd;
    }

    public void setBannerAdd(String bannerAdd) {
        this.bannerAdd = bannerAdd;
    }

    public String getInterstitialAdd() {
        return interstitialAdd;
    }

    public void setInterstitialAdd(String interstitialAdd) {
        this.interstitialAdd = interstitialAdd;
    }

    public  void setAddInterval(int addInterval) {
        this.addInterval = addInterval;
    }
    public int getAddInterval() {
        return addInterval;
    }
}
