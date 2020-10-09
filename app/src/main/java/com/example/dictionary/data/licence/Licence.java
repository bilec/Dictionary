package com.example.dictionary.data.licence;

public class Licence {

    private String name;
    private String terms;

    public Licence(String name, String terms)
    {
        this.name = name;
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public String getTerms() {
        return terms;
    }

    public void updateTerms(String year, String copyrightHolder)
    {
        this.terms = terms.replace("<year>", year);
        this.terms = terms.replace("<copyright holders>", copyrightHolder);
    }

}
