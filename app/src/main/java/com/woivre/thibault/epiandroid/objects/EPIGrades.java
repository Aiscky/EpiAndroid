package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

/**
 * Created by Thibault on 29/11/2015.
 */
public class EPIGrades extends EPIJSONObject {
    public Grade[] notes;

    public class Grade
    {
        public Double scolaryear;
        public String codemodule;
        public String titlemodule;
        public String codeinstance;
        public String codeacti;
        public String title;
        public String date;
        public String correcteur;
        public Double final_note;
        public String comment;

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
