package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

/**
 * Created by Thibault on 29/11/2015.
 */
public class EPIModule extends EPIJSONObject {
    public String codemodule;
    public String codeinstance;
    public Double semester;
    public String scolaryear_template;
    public String title;
    public String begin;
    public String end_register;
    public String end;
    public String past;
    public String closed;
    public String opened;
    public Object user_credits;
    public Double credits;
    public String description;
    public String competence;
    public String flags;
    public String instance_flags;
    public Double max_ins;
    public String instance_location;
    public String hidden;
    public Object old_acl_backup;
    public User[] resp;
    public User[] assistant;

    public class User
    {
        public String type;
        public String login;
        public String title;
        public String picture;
    }

    public Object rights;
    public User[] template_resp;
    public String allow_register;
    public String date_ins;
    public Double student_registered;
    public String student_grade;
    public Double student_credits;
    public String color;
    public String student_flags;
    public Boolean current_resp;
    public Object[] activities;

    @Override
    public String toString() {
        String display;

        display = new Gson().toJson(this);
        return display;
    }
}
