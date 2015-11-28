package com.woivre.thibault.epiandroid.objects;

import com.google.gson.annotations.SerializedName;

import java.security.acl.Group;

/**
 * Created by Thibault on 28/11/2015.
 */
public class EPIUser extends EPIJSONObject {
    public String title;
    public String internal_email;
    public String lastname;
    public String firstname;
    public UserInfo userinfo;

    public class UserInfo
    {
        public Email email;
        public Job job;
        public Website website;
        public Poste poste;

        public class Email
        {
            public String value;
            public Boolean adm;
            @SerializedName("public")
            public Boolean isPublic;
        }

        public class Job
        {
            public String value;
            public Boolean adm;
            @SerializedName("public")
            public Boolean isPublic;
        }

        public class Website
        {
            public String value;
            @SerializedName("public")
            public Boolean isPublic;
        }

        public class Poste
        {
            public String value;
            public Boolean adm;
            @SerializedName("public")
            public Boolean isPublic;
        }
    }

    public Boolean referent_used;
    public String picture;
    public String picture_fun;
    public Double promo;
    public Double semester;
    public Double uid;
    public Double gid;
    public String location;
    public String documents;
    public String userdocs;
    public String shell;
    public Boolean close;
    public String ctime;
    public String mtime;
    public String id_promo;
    public String id_history;
    public String course_code;
    public String school_code;
    public String school_title;
    public String old_id_promo;
    public String old_id_location;
    public Rights rights;

    public class Rights
    {

    }

    public Boolean invited;
    public Double studentyear;
    public Boolean admin;
    public Boolean editable;
    public Group[] groups;

    public class Group
    {
        public String title;
        public String name;
        public Double count;
    }

    public Event[] events;

    public class Event
    {

    }

    public Double credits;
    public GPA[] gpa;

    public class GPA
    {
        public String gpa;
        public String cycle;
    }


    public AverageGPA[] averageGPA;

    public class AverageGPA
    {
        public String cycle;
        public String gpa_average;
    }

    public Spice spice;

    public class Spice
    {
        public String available_spice;
        public Double comsumed_spice;
    }

    public NSSTAT nsstat;

    public class NSSTAT
    {
        public Double active;
        public Double idle;
        public Double out_active;
        public Double out_idle;
        public Double nslog_norm;
    }
}
