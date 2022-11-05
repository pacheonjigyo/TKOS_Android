package com.example.tkos;

import android.app.Application;

public class SavedID extends Application {
    private String tkos_id;

    public String getID()
    {
        return tkos_id;
    }

    public void setID(String globalString)
    {
        this.tkos_id = globalString;
    }
}
