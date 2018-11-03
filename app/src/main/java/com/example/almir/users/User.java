package com.example.almir.users;

import java.util.Set;

enum EyeColor{ BLUE, GREEN, BROWN }
enum Fruit{ BANANA, APPLE, STRAWBERRY }

public class User {
    private int id;
    private int age;
    private boolean isActive;
    private String name;
    private String company;
    private String email;
    private String address;
    private String phone;
    private String about;
    private String registered;
    private double latitude;
    private double longitude;
    private EyeColor eyeColor;
    private Fruit favoriteFruit;
    private Set<Integer> friends;

    static EyeColor getEyeColor(String eyeColor) {
        switch (eyeColor) {
            case "blue": {
                return EyeColor.BLUE;
            }
            case "green": {
                return EyeColor.GREEN;
            }
            case "brown": {
                return EyeColor.BROWN;
            }
            default: { throw new IllegalArgumentException("wrong color"); }
        }
    }

    static Fruit getFruit(String fruit) {
        switch (fruit) {
            case "banana": {
                return Fruit.BANANA;
            }
            case "apple": {
                return Fruit.APPLE;
            }
            case "strawberry": {
                return Fruit.STRAWBERRY;
            }
            default: { throw new IllegalArgumentException("wrong fruit"); }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Fruit getFavoriteFruit() {
        return favoriteFruit;
    }

    public void setFavoriteFruit(Fruit favoriteFruit) {
        this.favoriteFruit = favoriteFruit;
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public void setFriends(Set<Integer> friends) {
        this.friends = friends;
    }
}