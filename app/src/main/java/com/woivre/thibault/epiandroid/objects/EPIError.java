package com.woivre.thibault.epiandroid.objects;

/**
 * Created by Thibault on 26/11/2015.
 */
public class EPIError extends EPIJSONObject {

    @Override
    public String toString() {
        String desc;

        desc = "Code : " + this.error.code + " message : " + this.error.message;
        return (desc);
    }

    public ErrorInfos error = new ErrorInfos();

    public class ErrorInfos
    {
        public String message;
        public Double code;
    }
}
