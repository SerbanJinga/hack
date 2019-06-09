package com.roh44x.medlinker.components;

public class Medicine {

    public String medName;
    public String warnings;

    public Medicine(){

    }

    public Medicine(String medName, String warnings)
    {
        this.medName = medName;
        this.warnings = warnings;
    }
}
