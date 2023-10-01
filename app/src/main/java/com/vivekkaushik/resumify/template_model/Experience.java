package com.vivekkaushik.resumify.template_model;

public class Experience {
    private String mJobTitle;
    private String mCompanyName;
    private String mStarted;
    private String mEnded;
    private String mJobDesc;

    public Experience(String jobTitle, String companyName, String started, String ended, String jobDesc) {
        mJobTitle = jobTitle;
        mCompanyName = companyName;
        mStarted = started;
        mEnded = ended;
        mJobDesc = jobDesc;
    }

    public String getJobTitle() {
        return mJobTitle;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getStarted() {
        return mStarted;
    }

    public String getEnded() {
        return mEnded;
    }

    public String getJobDesc() {
        return mJobDesc;
    }
}
