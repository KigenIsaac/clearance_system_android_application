package com.nola.clearancesystem;

public class Student {
    private String regno,name;

    public Student(String regno, String name) {
        this.regno = regno;
        this.name = name;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
