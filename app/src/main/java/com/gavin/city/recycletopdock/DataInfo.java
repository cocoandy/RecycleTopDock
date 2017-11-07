package com.gavin.city.recycletopdock;

/**
 * Created by Administrator on 2017/11/7.
 */

public class DataInfo {
    public String topDock;
    public String name;
    public String gender;
    public String profession;

    public DataInfo(String topDock, String name, String gender, String profession) {
        this.topDock = topDock;
        this.name = name;
        this.gender = gender;
        this.profession = profession;
    }

    public void setName(String name) {
        this.name = name;
    }
}
