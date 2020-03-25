package com.example.triplist;

import android.graphics.Bitmap;

public class ImgSrc {
    private String place;
    private Bitmap img;
    private String description;

    public ImgSrc(String place, Bitmap img, String description) {
        this.place = place;
        this.img = img;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

}
