package com.dayslar.mytalk.Models;

public class Manager {

    //для таблицы менеджеров
    public static final String MANAGER_ID = "_id";
    public static final String MANAGER_PHOTO_PATCH = "patch";
    public static final String MANAGER_NAME = "name";

    private int id;
    private String name;
    private String photoPatch;

    public Manager(String name, String photoPatch){
        this.name = name;
        this.photoPatch = photoPatch;
    }

    public String getName(){
        return name;
    }

    public String getPhotoPatch(){
        return photoPatch;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPhotoPatch(String photoPatch){
        this.photoPatch = photoPatch;
    }

    public int getID(){
        return id;
    }

    public void setID(int id){
        this.id = id;
    }
}
