package com.telstra.androidjsonparsing.model;

public class Country {


    private String title;
    private CountryDetails[] rows;

    public CountryDetails[] getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

}
