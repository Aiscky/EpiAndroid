package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;

import java.lang.reflect.Member;

/**
 * Created by Thibault on 07/12/2015.
 */
public class EPIProjectSubscribeResponse extends EPIJSONObject {
    public String code;
    public String title;
    public String url_repository;
    public Member[] members;
    public class Member
    {
        public String login;
        public String title;
        public String picture;
        public String status;
        public Boolean master;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
