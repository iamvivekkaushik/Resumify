package com.vivekkaushik.resumify.model;

import com.vivekkaushik.resumify.template_model.Education;
import com.vivekkaushik.resumify.template_model.Experience;
import com.vivekkaushik.resumify.template_model.Project;
import com.vivekkaushik.resumify.template_model.Reference;
import com.vivekkaushik.resumify.template_model.Skill;

import java.util.ArrayList;

public class Resume {
    private String imgLocation;
    private String firstName;
    private String lastName;
    private String job;
    private String objective;
    private String mobile;
    private String email;
    private String website;
    private String address;
    private String language;
    private String hobby;
    public int templateId;
    private ArrayList<Education> eduList;
    private ArrayList<Experience> expList;
    private ArrayList<Skill> skillList;
    private ArrayList<Reference> referenceList;
    private ArrayList<Project> projectList;

    public Resume(ArrayList<Education> eduList, ArrayList<Experience> expList, ArrayList<Skill> skillList, ArrayList<Reference> referenceList, ArrayList<Project> projectList, int templateId) {
        this.eduList = eduList;
        this.expList = expList;
        this.skillList = skillList;
        this.referenceList = referenceList;
        this.projectList = projectList;

        //initialize variables
        imgLocation = "";
        firstName = "";
        lastName = "";
        job = "";
        objective = "";
        mobile = "";
        email = "";
        website = "";
        address = "";
        language = "";
        hobby = "";
        this.templateId = templateId;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getImgLocation() {
        return imgLocation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJob() {
        return job;
    }

    public String getObjective() {
        return objective;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }

    public String getLanguage() {
        return language;
    }

    public String getHobby() {
        return hobby;
    }

    public int getTemplateId() {
        return templateId;
    }

    public ArrayList<Education> getEduList() {
        return eduList;
    }

    public ArrayList<Experience> getExpList() {
        return expList;
    }

    public ArrayList<Skill> getSkillList() {
        return skillList;
    }

    public ArrayList<Reference> getReferenceList() {
        return referenceList;
    }

    public ArrayList<Project> getProjectList() {
        return projectList;
    }


    @Override
    public String toString() {
        return "Resume{" +
                "imgLocation='" + imgLocation + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", job='" + job + '\'' +
                ", objective='" + objective + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", address='" + address + '\'' +
                ", language='" + language + '\'' +
                ", hobby='" + hobby + '\'' +
                ", eduList=" + eduList +
                ", expList=" + expList +
                ", skillList=" + skillList +
                ", referenceList=" + referenceList +
                ", projectList=" + projectList +
                '}';
    }
}
