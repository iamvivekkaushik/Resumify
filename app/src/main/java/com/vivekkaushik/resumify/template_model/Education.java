package com.vivekkaushik.resumify.template_model;

public class Education {
    private String mCourse;
    private String mInstitute;
    private String mStartYear;
    private String mEndYear;

    public Education(String course, String institute, String startYear, String endYear) {
        mCourse = course;
        mInstitute = institute;
        mStartYear = startYear;
        mEndYear = endYear;
    }

    public String getCourse() {
        return mCourse;
    }

    public String getInstitute() {
        return mInstitute;
    }

    public String getStartYear() {
        return mStartYear;
    }

    public String getEndYear() {
        return mEndYear;
    }
}
