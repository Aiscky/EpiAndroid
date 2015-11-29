package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

/**
 * Created by Thibault on 28/11/2015.
 */
public class EPIEventPlanning extends EPIJSONObject {
    public ProfInst[] prof_inst;

    public class ProfInst
    {
        public class Prof
        {
            public Prof1 prof1;
            public Prof2 prof2;

            public class Prof1
            {
                public String type;
                public String login;
                public String title;
                public String picture;
            }

            public class Prof2
            {
                public String type;
                public String login;
                public String title;
            }
        }
    }

    public String title;
    public String rdv_indiv_registered;
    public String allowed_planning_end;
    public Double nb_group;
    public String start;
    public String register_month;
    public String allowed_planning_start;
    public String project;
    public Boolean event_registered;
    public Double total_students_registered;
    public Boolean allow_register;
    public String codemodule;
    public String rdv_group_registered;
    public Double semester;
    public String type_code;
    public String is_rdv;
    public Boolean allow_token;
    public String title_module;
    public Boolean in_more_than_one_month;
    public String acti_title;
    public String instance_location;
    public String nb_hours;
    public String register_prof;
    public String nb_max_students_projet;
    public Room room;

    public class Room
    {
        public String type;
        public Double seats;
        public String code;
    }

    public String codeacti;
    public String codeevent;
    public String codeinstance;
    public String dates;
    public Boolean register_student;
    public String type_title;
    public Double num_event;
    public String end;
    public String scolaryear;
    public Boolean module_registered;
    public Boolean past;
    public Boolean module_available;

    @Override
    public String toString() {
        String display;

        display = new Gson().toJson(this);
        return display;
    }
}
