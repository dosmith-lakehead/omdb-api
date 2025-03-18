package com.dosmith.omdb_api.models;

public class Rating {
    private String Source;
    private String Value;

    public Rating(){
    }

    public String getValue() {
        return Value;
    }
    public String getSource() {
        return Source;
    }
}
