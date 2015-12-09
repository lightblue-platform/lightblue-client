package com.redhat.lightblue.client.integration.test.pojo;

public class Country {

    public static final String objectType = "country",
            objectVersion = "0.1.0-SNAPSHOT";

    private String name, iso2Code, iso3Code, optionalField;

    public Country() {}

    public Country(String name, String iso2Code, String iso3Code, String optionalField) {
        this.name = name;
        this.iso2Code = iso2Code;
        this.iso3Code = iso3Code;
        this.optionalField = optionalField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso2Code() {
        return iso2Code;
    }

    public void setIso2Code(String iso2code) {
        iso2Code = iso2code;
    }

    public String getIso3Code() {
        return iso3Code;
    }

    public void setIso3Code(String iso3code) {
        iso3Code = iso3code;
    }

    public String getOptionalField() {
        return optionalField;
    }

    public void setOptionalField(String optionalField) {
        this.optionalField = optionalField;
    }

}
