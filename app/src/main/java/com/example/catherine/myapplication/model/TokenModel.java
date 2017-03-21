package com.example.catherine.myapplication.model;


import com.example.catherine.myapplication.utills.Target;

import java.io.Serializable;

/**
 * Created catherine
 */

public class TokenModel implements Serializable {
    private String mToken;
    private Target mTarget;

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public Target getTarget() {
        return mTarget;
    }

    public void setTarget(Target mTarget) {
        this.mTarget = mTarget;
    }

    @Override
    public String toString() {
        return "TokenModel{" +
                "mToken='" + mToken + '\'' +
                ", mTarget=" + mTarget +
                '}';
    }
}
