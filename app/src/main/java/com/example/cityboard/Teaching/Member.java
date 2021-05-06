package com.example.cityboard.Teaching;

public class Member {
    String uid, uName, uEmail, pId, name, pDescription, image, pTime, price;

    public Member() {
    }

    public Member(String uid, String uName, String uEmail, String pId, String name, String pDescription, String image, String pTime, String price) {
        this.uid = uid;
        this.uName = uName;
        this.uEmail = uEmail;
        this.pId = pId;
        this.name = name;
        this.pDescription = pDescription;
        this.image = image;
        this.pTime = pTime;
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
