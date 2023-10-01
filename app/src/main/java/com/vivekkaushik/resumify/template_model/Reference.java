package com.vivekkaushik.resumify.template_model;

public class Reference {
    private String name;
    private String designation;
    private String email;
    private String phone;

    public Reference(String name, String designation, String email, String phone) {
        this.name = name;
        this.designation = designation;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
