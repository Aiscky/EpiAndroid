package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

import java.util.Objects;

public class EPIAllModules extends EPIJSONObject {
    public Object[][] preload;

    public Item[] items;

    public class Item
    {
        public Double id;
        public String title_cn;
        public Double semester;
        public String num;
        public String begin;
        public String end;
        public String end_register;
        public Double scolaryear;
        public String code;
        public String codeinstance;
        public String location_title;
        public String instance_location;
        public String flags;
        public String credits;
        public EPIUser.Rights[] rights;
        public String status;
        public Boolean waiting_grades;
        public String active_promo;
        public String open;
        public String title;

        @Override
        public String toString() {
            String display;

            display = new Gson().toJson(this);
            return display;
        }
    }

    @Override
    public String toString() {
        String display;

        display = new Gson().toJson(this);
        return display;
    }
}
