package com.vivekkaushik.resumify;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vivekkaushik.resumify.database.EducationContract;
import com.vivekkaushik.resumify.database.ExperienceContract;
import com.vivekkaushik.resumify.database.ProjectContract;
import com.vivekkaushik.resumify.database.ReferenceContract;
import com.vivekkaushik.resumify.database.ResumeContract;
import com.vivekkaushik.resumify.database.SkillContract;
import com.vivekkaushik.resumify.helper.QueryHelper;
import com.vivekkaushik.resumify.model.Resume;
import com.vivekkaushik.resumify.template_model.Education;
import com.vivekkaushik.resumify.template_model.Experience;
import com.vivekkaushik.resumify.template_model.Project;
import com.vivekkaushik.resumify.template_model.Reference;
import com.vivekkaushik.resumify.template_model.Skill;

import java.util.ArrayList;
import java.util.Objects;

public class FormActivity extends AppCompatActivity {
    private static final String TAG = "FormActivity";
    private ViewPager mViewPager;
    long resumeId;
    boolean isEdit = false;
    public static Resume resume;
    int templateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        isEdit = extras.getBoolean("isEdit");
        resumeId = extras.getLong("resumeId");

        if (!isEdit) {
            templateId = extras.getInt("templateId");
            resume = new Resume(new ArrayList<Education>(), new ArrayList<Experience>(),
                    new ArrayList<Skill>(), new ArrayList<Reference>(), new ArrayList<Project>(), templateId);
        } else {
            resume = QueryHelper.queryEverything(getContentResolver(), resumeId);
        }

        // Add back arrow button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(sectionPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));
        tabLayout.addTab(tabLayout.newTab().setText("EDUCATION"));
        tabLayout.addTab(tabLayout.newTab().setText("EXPERIENCE"));
        tabLayout.addTab(tabLayout.newTab().setText("SKILLS"));
        tabLayout.addTab(tabLayout.newTab().setText("REFERENCES"));
        tabLayout.addTab(tabLayout.newTab().setText("PROJECTS"));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveButton) {
            if (isEdit) updateDatabase();
            else saveToDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to leave?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void updateDatabase() {
        ContentResolver contentResolver = getContentResolver();

        boolean finishActivity = false;

        if (!resume.getFirstName().isEmpty() && !resume.getLastName().isEmpty()) {
            updateResumeTable(contentResolver);
            finishActivity = true;
        }

        if (resume.getEduList().size() > 0 && !(resumeId < 0)) {
            updateEducationTable(contentResolver);
        }

        if (resume.getExpList().size() > 0 && !(resumeId < 0)) {
            updateExperienceTable(contentResolver);
        }

        if (resume.getSkillList().size() > 0 && !(resumeId < 0)) {
            updateSkillTable(contentResolver);
        }

        if (resume.getReferenceList().size() > 0 && !(resumeId < 0)) {
            updateReferenceTable(contentResolver);
        }

        if (resume.getProjectList().size() > 0 && !(resumeId < 0)) {
            updateProjectTable(contentResolver);
        }

        if (finishActivity) finish();
    }

    private void updateResumeTable(ContentResolver contentResolver) {
        ContentValues values = new ContentValues();
        values.put(ResumeContract.Columns.RESUME_FIRST_NAME, resume.getFirstName());
        values.put(ResumeContract.Columns.RESUME_LAST_NAME, resume.getLastName());
        values.put(ResumeContract.Columns.RESUME_JOB, resume.getJob());
        values.put(ResumeContract.Columns.RESUME_OBJECTIVE, resume.getObjective());
        values.put(ResumeContract.Columns.RESUME_MOBILE, resume.getMobile());
        values.put(ResumeContract.Columns.RESUME_EMAIL, resume.getEmail());
        values.put(ResumeContract.Columns.RESUME_WEBSITE, resume.getWebsite());
        values.put(ResumeContract.Columns.RESUME_ADDRESS, resume.getAddress());
        //AboutFragment.saveChips();
        values.put(ResumeContract.Columns.RESUME_LANGUAGE, resume.getLanguage());
        values.put(ResumeContract.Columns.RESUME_HOBBY, resume.getHobby());
        values.put(ResumeContract.Columns.RESUME_TEMPLATE, resume.getTemplateId());
        values.put(ResumeContract.Columns.RESUME_IMG_PATH, resume.getImgLocation());

        int rows = contentResolver.update(ResumeContract.buildResumeUri(resumeId), values, null, null);

        Log.d(TAG, "updateResume: Resume Number of rows updated: " + rows);

        //return ResumeContract.getResumeId(uri);
    }

    private void updateEducationTable(ContentResolver contentResolver) {
        contentResolver.delete(EducationContract.CONTENT_URI, EducationContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});
        insertIntoEducationTable(contentResolver);
    }

    private void updateExperienceTable(ContentResolver contentResolver) {
        contentResolver.delete(ExperienceContract.CONTENT_URI, ExperienceContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});
        insertIntoExperienceTable(contentResolver);
    }

    private void updateSkillTable(ContentResolver contentResolver) {
        contentResolver.delete(SkillContract.CONTENT_URI, SkillContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});
        insertIntoSkillTable(contentResolver);
    }

    private void updateReferenceTable(ContentResolver contentResolver) {
        contentResolver.delete(ReferenceContract.CONTENT_URI, ReferenceContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});
        insertIntoReferenceTable(contentResolver);
    }

    private void updateProjectTable(ContentResolver contentResolver) {
        contentResolver.delete(ProjectContract.CONTENT_URI, ProjectContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});
        insertIntoProjectTable(contentResolver);
    }

    private void saveToDatabase() {
        ContentResolver contentResolver = getContentResolver();

        boolean finishActivity = false;

        if (!resume.getFirstName().isEmpty() && !resume.getLastName().isEmpty()) {
            resumeId = insertIntoResumeTable(contentResolver);
            finishActivity = true;
        }

        if (resume.getEduList().size() > 0 && !(resumeId < 0)) {
            insertIntoEducationTable(contentResolver);
        }

        if (resume.getExpList().size() > 0 && !(resumeId < 0)) {
            insertIntoExperienceTable(contentResolver);
        }

        if (resume.getSkillList().size() > 0 && !(resumeId < 0)) {
            insertIntoSkillTable(contentResolver);
        }

        if (resume.getReferenceList().size() > 0 && !(resumeId < 0)) {
            insertIntoReferenceTable(contentResolver);
        }

        if (resume.getProjectList().size() > 0 && !(resumeId < 0)) {
            insertIntoProjectTable(contentResolver);
        }

        if (finishActivity) finish();
    }

    private long insertIntoResumeTable(ContentResolver contentResolver) {
        ContentValues values = new ContentValues();
        values.put(ResumeContract.Columns.RESUME_FIRST_NAME, resume.getFirstName());
        values.put(ResumeContract.Columns.RESUME_LAST_NAME, resume.getLastName());
        values.put(ResumeContract.Columns.RESUME_JOB, resume.getJob());
        values.put(ResumeContract.Columns.RESUME_OBJECTIVE, resume.getObjective());
        values.put(ResumeContract.Columns.RESUME_MOBILE, resume.getMobile());
        values.put(ResumeContract.Columns.RESUME_EMAIL, resume.getEmail());
        values.put(ResumeContract.Columns.RESUME_WEBSITE, resume.getWebsite());
        values.put(ResumeContract.Columns.RESUME_ADDRESS, resume.getAddress());
        //AboutFragment.saveChips();
        values.put(ResumeContract.Columns.RESUME_LANGUAGE, resume.getLanguage());
        values.put(ResumeContract.Columns.RESUME_HOBBY, resume.getHobby());
        values.put(ResumeContract.Columns.RESUME_TEMPLATE, templateId);
        values.put(ResumeContract.Columns.RESUME_IMG_PATH, resume.getImgLocation());

        Uri uri = contentResolver.insert(ResumeContract.CONTENT_URI, values);

        return ResumeContract.getResumeId(uri);
    }

    private void insertIntoEducationTable(ContentResolver contentResolver) {
        for (int i = 0; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            ContentValues values = new ContentValues();
            values.put(EducationContract.Columns.EDUCATION_COURSE, edu.getCourse());
            values.put(EducationContract.Columns.EDUCATION_INSTITUTE, edu.getInstitute());
            values.put(EducationContract.Columns.EDUCATION_START, edu.getStartYear());
            values.put(EducationContract.Columns.EDUCATION_END, edu.getEndYear());
            values.put(EducationContract.Columns.RESUME_ID, resumeId);

            contentResolver.insert(EducationContract.CONTENT_URI, values);
        }
    }

    private void insertIntoExperienceTable(ContentResolver contentResolver) {
        for (int i = 0; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            ContentValues values = new ContentValues();
            values.put(ExperienceContract.Columns.EXPERIENCE_JOB, exp.getJobTitle());
            values.put(ExperienceContract.Columns.EXPERIENCE_COMPANY, exp.getCompanyName());
            values.put(ExperienceContract.Columns.EXPERIENCE_START, exp.getStarted());
            values.put(ExperienceContract.Columns.EXPERIENCE_END, exp.getEnded());
            values.put(ExperienceContract.Columns.EXPERIENCE_JOB_DESC, exp.getJobDesc());
            values.put(ExperienceContract.Columns.RESUME_ID, resumeId);

            contentResolver.insert(ExperienceContract.CONTENT_URI, values);
        }
    }

    private void insertIntoSkillTable(ContentResolver contentResolver) {
        for (int i = 0; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            ContentValues values = new ContentValues();
            values.put(SkillContract.Columns.SKILL, skill.getSkill());
            values.put(SkillContract.Columns.SKILL_LEVEL, skill.getSkillLevel());
            values.put(SkillContract.Columns.RESUME_ID, resumeId);

            contentResolver.insert(SkillContract.CONTENT_URI, values);
        }
    }

    private void insertIntoReferenceTable(ContentResolver contentResolver) {
        for (int i = 0; i < resume.getReferenceList().size(); i++) {
            Reference reference = resume.getReferenceList().get(i);

            ContentValues values = new ContentValues();
            values.put(ReferenceContract.Columns.REFERENCE_NAME, reference.getName());
            values.put(ReferenceContract.Columns.REFERENCE_DESIGNATION, reference.getDesignation());
            values.put(ReferenceContract.Columns.REFERENCE_EMAIL, reference.getEmail());
            values.put(ReferenceContract.Columns.REFERENCE_PHONE, reference.getPhone());
            values.put(ReferenceContract.Columns.RESUME_ID, resumeId);

            contentResolver.insert(ReferenceContract.CONTENT_URI, values);
        }
    }

    private void insertIntoProjectTable(ContentResolver contentResolver) {
        for (int i = 0; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            ContentValues values = new ContentValues();
            values.put(ProjectContract.Columns.PROJECT_NAME, project.getProjectName());
            values.put(ProjectContract.Columns.PROJECT_ROLE, project.getProjectRole());
            values.put(ProjectContract.Columns.PROJECT_DESCRIPTION, project.getProjectDescription());
            values.put(ProjectContract.Columns.RESUME_ID, resumeId);

            contentResolver.insert(ProjectContract.CONTENT_URI, values);
        }
    }
}
