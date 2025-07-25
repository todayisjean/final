package org.example;

class Data{
    public int id;
    public String email;
    public String first_name;
    public String last_name;
    public String avatar;
}

public class UserData{
    public Data data;
    public Support support;
}

class Support{
    public String url;
    public String text;
}

