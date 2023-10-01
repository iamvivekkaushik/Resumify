package com.vivekkaushik.resumify.model;

/**
 * Model class for resumeList on the main screen of the app
 * This class contains some variable
 * resumeId: row Id from the Resume Table
 * mFileName: file name stored on the internal memory of the device
 * mTemplate: template used for the resume
 * mResumeImage: image of the template
 */
public class ResumeList {
    private long resumeId;
    //ToDo: change this to name
    private String mFileName;
    private int mTemplateId;
    private int mResumeImage;

    public ResumeList(long resumeId, String mFileName, int templateId, int mResumeImage) {
        this.resumeId = resumeId;
        this.mFileName = mFileName;
        this.mTemplateId = templateId;
        this.mResumeImage = mResumeImage;
    }

    public long getResumeId() {
        return resumeId;
    }

    public String getFileName() {
        return mFileName;
    }

    public int getTemplateId() {
        return mTemplateId;
    }

    public int getResumeImage() {
        return mResumeImage;
    }
}
