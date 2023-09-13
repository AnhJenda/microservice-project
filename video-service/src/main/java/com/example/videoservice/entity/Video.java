package com.example.videoservice.entity;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {
    private int id;
    private List<Object> user;

    public Video(){}

    public Video(int videoId){
        this.id = videoId;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public List<Object> getUser(){
        return user;
    }
    public void setUser(List<Object> user){
        this.user = user;
    }
}
