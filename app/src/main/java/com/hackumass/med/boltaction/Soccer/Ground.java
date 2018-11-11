package com.hackumass.med.boltaction.Soccer;

/**
 * Created by Aryan Singh on 11/10/2018.
 */

public class Ground {

    public String name;
    public String time;
    public String capacity;
    public String id;


    public Ground(){

    }

    public Ground(String name, String time, String capacity, String id) {
        this.name = name;
        this.time = time;
        this.capacity = capacity;
        this.id = id;
    }
}
