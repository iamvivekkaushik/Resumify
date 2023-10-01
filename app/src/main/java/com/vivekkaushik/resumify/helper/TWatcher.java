package com.vivekkaushik.resumify.helper;

import android.text.Editable;
import android.text.TextWatcher;

import static com.vivekkaushik.resumify.FormActivity.resume;

public class TWatcher implements TextWatcher {
    private int mCase;
    public static final int RESUME_FIRST_NAME = 1;
    public static final int RESUME_LAST_NAME = 2;
    public static final int RESUME_JOB = 3;
    public static final int RESUME_OBJECTIVE = 4;
    public static final int RESUME_MOBILE = 5;
    public static final int RESUME_EMAIL = 6;
    public static final int RESUME_WEBSITE = 7;
    public static final int RESUME_ADDRESS = 8;
    public static final int RESUME_LANGUAGE = 9;
    public static final int RESUME_HOBBY = 10;

    public TWatcher(int mCase) {
        this.mCase = mCase;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (mCase) {
            case RESUME_FIRST_NAME:
                resume.setFirstName(s.toString());
                break;
            case RESUME_LAST_NAME:
                resume.setLastName(s.toString());
                break;
            case RESUME_JOB:
                resume.setJob(s.toString());
                break;
            case RESUME_OBJECTIVE:
                resume.setObjective(s.toString());
                break;
            case RESUME_MOBILE:
                resume.setMobile(s.toString());
                break;
            case RESUME_EMAIL:
                resume.setEmail(s.toString());
                break;
            case RESUME_WEBSITE:
                resume.setWebsite(s.toString());
                break;
            case RESUME_ADDRESS:
                resume.setAddress(s.toString());
                break;
            case RESUME_LANGUAGE:
                resume.setLanguage(s.toString());
                break;
            case RESUME_HOBBY:
                resume.setHobby(s.toString());
                break;
            default:
                //Nothing to be done here
        }
    }
}
