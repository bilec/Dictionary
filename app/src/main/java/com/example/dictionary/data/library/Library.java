package com.example.dictionary.data.library;

import com.example.dictionary.data.licence.Licence;

public class Library {

    private String name;
    private Licence licence;

    public Library(String name, Licence licence) {
        this.name = name;
        this.licence = licence;
    }

    public String getName() {
        return name;
    }

    public Licence getLicence() {
        return licence;
    }
}
