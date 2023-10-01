package com.vivekkaushik.resumify.template_model;

public class Skill {
    private String mSkill;
    private float mSkillLevel;

    public Skill(String skill, float skillLevel) {
        mSkill = skill;
        mSkillLevel = skillLevel;
    }

    public String getSkill() {
        return mSkill;
    }

    public float getSkillLevel() {
        return mSkillLevel;
    }
}
