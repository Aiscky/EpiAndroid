package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

public class EPIEventDetail extends EPIJSONObject {
    public String scolaryear;
    public String codemodule;
    public String codeinstance;
    public String codeacti;
    public String codevent;
    public Double semester;
    public String instance_location;
    public String module_title;
    public String acti_title;
    public String acti_description;
    public String type_title;
    public String type_code;
    public String allowed_planning_start;
    public String allowed_planning_end;
    public String nb_hours;
    public Double nb_group;
    public Boolean has_exam_subject;
    public String begin;
    public String start;
    public String end;
    public Double num_event;
    public String title;
    public String description;
    public Double nb_registered;
    public String id_dir;
    public EPIEventPlanning.Room room;
    public Double seats;
    public String desc_webservice;
    public String name_bocal;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
