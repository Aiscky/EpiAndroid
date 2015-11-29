package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

/**
 * Created by dylan on 29/11/2015.
 */

public class EPIProject extends EPIJSONObject {
        public String codemodule;
        public String project;
        public String end_acti;
        public String acti_title;
        public Double num_event;
        public String seats;
        public String title_module;
        public String begin_event;
        public String[] rights;
        public String num;
        public String begin_acti;
        public String scolaryear;
        public String code_loaction;
        public String end_event;
        public String type_acti_code;
        public String codeacti;
        public String info_creneau;
        public Integer registered;
        public String codeinstance;
        public String type_acti;

        @Override
        public String toString() {
                String display;

                display = new Gson().toJson(this);
                return display;
        }
}
