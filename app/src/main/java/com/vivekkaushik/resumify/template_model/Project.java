package com.vivekkaushik.resumify.template_model;

public class Project {
    private String projectName;
    private String projectRole;
    private String projectDescription;

    public Project(String projectName, String projectRole, String projectDescription) {
        this.projectName = projectName;
        this.projectRole = projectRole;
        this.projectDescription = projectDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public String getProjectDescription() {
        return projectDescription;
    }
}
