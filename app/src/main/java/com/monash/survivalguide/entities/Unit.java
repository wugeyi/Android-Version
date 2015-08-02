package com.monash.survivalguide.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import entity.Faculty;

/**
 * Created by xiaoduo on 7/19/15.
 */

@ParseClassName("Unit")
public class Unit extends ParseObject {

    public String getCode() {
        return getString("code");
    }


    public void setCode(String code) {
        put("code", code);
    }

    public void setName(String name) {

        put("name", name);
    }

    public void setPrefix(String prefix) {
        put("prefix", prefix);
    }

    public void setFaculty(Faculty faculty) {
        put("faculty", faculty);
    }

    public Faculty getFaculty() {

        return (Faculty) getParseObject("faculty");
    }

    public String getName() {
        return getString("name");
    }

    public String getPrefix() {
        return getString("prefix");
    }


}
