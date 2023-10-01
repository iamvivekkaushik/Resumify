package com.vivekkaushik.resumify.helper;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.vivekkaushik.resumify.database.EducationContract;
import com.vivekkaushik.resumify.database.ExperienceContract;
import com.vivekkaushik.resumify.database.ProjectContract;
import com.vivekkaushik.resumify.database.ReferenceContract;
import com.vivekkaushik.resumify.database.ResumeContract;
import com.vivekkaushik.resumify.database.SkillContract;
import com.vivekkaushik.resumify.model.Resume;
import com.vivekkaushik.resumify.template_model.Education;
import com.vivekkaushik.resumify.template_model.Experience;
import com.vivekkaushik.resumify.template_model.Project;
import com.vivekkaushik.resumify.template_model.Reference;
import com.vivekkaushik.resumify.template_model.Skill;

import java.util.ArrayList;

/**
 * This is a helper class. It contains method to query and delete data from the table.
 */
public class QueryHelper {
    private static final String TAG = "QueryHelper";

    /**
     * This method query all the rows from all the table that contain the same resumeId
     *
     * @param contentResolver content Resolver required to access the database
     * @param id              resumeId for the rows
     * @return returns a Resume object
     */
    public static Resume queryEverything(ContentResolver contentResolver, long id) {
        Resume resume = new Resume(new ArrayList<Education>(), new ArrayList<Experience>(),
                new ArrayList<Skill>(), new ArrayList<Reference>(), new ArrayList<Project>(), 0);

        //Add resume About
        String[] projection = {ResumeContract.Columns.RESUME_FIRST_NAME,
                ResumeContract.Columns.RESUME_LAST_NAME,
                ResumeContract.Columns.RESUME_JOB,
                ResumeContract.Columns.RESUME_OBJECTIVE,
                ResumeContract.Columns.RESUME_MOBILE,
                ResumeContract.Columns.RESUME_EMAIL,
                ResumeContract.Columns.RESUME_WEBSITE,
                ResumeContract.Columns.RESUME_ADDRESS,
                ResumeContract.Columns.RESUME_LANGUAGE,
                ResumeContract.Columns.RESUME_HOBBY,
                ResumeContract.Columns.RESUME_TEMPLATE,
                ResumeContract.Columns.RESUME_IMG_PATH};

        Cursor cursor = contentResolver.query(ResumeContract.buildResumeUri(id),
                projection,
                null,
                null,
                null);

        if (cursor != null) {
            Log.d(TAG, "onCreate: Resume number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    switch (cursor.getColumnName(i)) {
                        case ResumeContract.Columns.RESUME_FIRST_NAME:
                            resume.setFirstName(cursor.getString(i));
                            Log.d(TAG, "queryEverything: FirstName: " + cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_LAST_NAME:
                            resume.setLastName(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_JOB:
                            resume.setJob(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_OBJECTIVE:
                            resume.setObjective(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_MOBILE:
                            resume.setMobile(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_EMAIL:
                            resume.setEmail(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_WEBSITE:
                            resume.setWebsite(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_ADDRESS:
                            resume.setAddress(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_LANGUAGE:
                            resume.setLanguage(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_HOBBY:
                            resume.setHobby(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_TEMPLATE:
                            resume.setTemplateId(Integer.valueOf(cursor.getString(i)));
                            break;
                        case ResumeContract.Columns.RESUME_IMG_PATH:
                            resume.setImgLocation(cursor.getString(i));
                        default:
                            //Nothing to do here
                    }
                }

            }
            cursor.close();
        }

        //Add Education Items to the list
        projection = new String[]{EducationContract.Columns.EDUCATION_COURSE,
                EducationContract.Columns.EDUCATION_INSTITUTE,
                EducationContract.Columns.EDUCATION_START,
                EducationContract.Columns.EDUCATION_END};

        cursor = contentResolver.query(EducationContract.CONTENT_URI,
                projection,
                EducationContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(id)},
                EducationContract.Columns._ID);

        if (cursor != null) {
            Log.d(TAG, "onCreate: Education number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String course = "";
                String institute = "";
                String start = "";
                String end = "";

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    switch (cursor.getColumnName(i)) {
                        case EducationContract.Columns.EDUCATION_COURSE:
                            course = cursor.getString(i);
                            break;
                        case EducationContract.Columns.EDUCATION_INSTITUTE:
                            institute = cursor.getString(i);
                            break;
                        case EducationContract.Columns.EDUCATION_START:
                            start = cursor.getString(i);
                            break;
                        case EducationContract.Columns.EDUCATION_END:
                            end = cursor.getString(i);
                            break;
                        default:
                            //Nothing to do here
                    }
                }
                resume.getEduList().add(new Education(course, institute, start, end));
            }
            cursor.close();
        }


        //Add Experience item to the list

        projection = new String[]{ExperienceContract.Columns.EXPERIENCE_JOB,
                ExperienceContract.Columns.EXPERIENCE_COMPANY,
                ExperienceContract.Columns.EXPERIENCE_START,
                ExperienceContract.Columns.EXPERIENCE_END,
                ExperienceContract.Columns.EXPERIENCE_JOB_DESC};

        cursor = contentResolver.query(ExperienceContract.CONTENT_URI,
                projection,
                ExperienceContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(id)},
                ExperienceContract.Columns._ID);

        if (cursor != null) {
            Log.d(TAG, "onCreate: Experience number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String jobTitle = "";
                String company = "";
                String start = "";
                String end = "";
                String jobDesc = "";

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    switch (cursor.getColumnName(i)) {
                        case ExperienceContract.Columns.EXPERIENCE_JOB:
                            jobTitle = cursor.getString(i);
                            break;
                        case ExperienceContract.Columns.EXPERIENCE_COMPANY:
                            company = cursor.getString(i);
                            break;
                        case ExperienceContract.Columns.EXPERIENCE_START:
                            start = cursor.getString(i);
                            break;
                        case ExperienceContract.Columns.EXPERIENCE_END:
                            end = cursor.getString(i);
                            break;
                        case ExperienceContract.Columns.EXPERIENCE_JOB_DESC:
                            jobDesc = cursor.getString(i);
                            break;
                        default:
                            //Nothing to do here
                    }
                }
                resume.getExpList().add(new Experience(jobTitle, company, start, end, jobDesc));
            }
            cursor.close();
        }

        //Query skill
        projection = new String[]{SkillContract.Columns.SKILL,
                SkillContract.Columns.SKILL_LEVEL};

        cursor = contentResolver.query(SkillContract.CONTENT_URI,
                projection,
                SkillContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(id)},
                SkillContract.Columns._ID);

        if (cursor != null) {
            Log.d(TAG, "onCreate: Skill number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String skill = "";
                float skillLevel = 0;

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    switch (cursor.getColumnName(i)) {
                        case SkillContract.Columns.SKILL:
                            skill = cursor.getString(i);
                            break;
                        case SkillContract.Columns.SKILL_LEVEL:
                            skillLevel = Float.parseFloat(cursor.getString(i));
                            break;
                        default:
                            //Nothing to do here
                    }
                }
                resume.getSkillList().add(new Skill(skill, skillLevel));
            }
            Log.d(TAG, "queryEverything: Skill" + resume.getSkillList().toString());
            cursor.close();
        }

        //Query References
        projection = new String[]{ReferenceContract.Columns.REFERENCE_NAME,
                ReferenceContract.Columns.REFERENCE_DESIGNATION,
                ReferenceContract.Columns.REFERENCE_PHONE,
                ReferenceContract.Columns.REFERENCE_EMAIL};

        cursor = contentResolver.query(ReferenceContract.CONTENT_URI,
                projection,
                ReferenceContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(id)},
                ReferenceContract.Columns._ID);

        if (cursor != null) {
            Log.d(TAG, "onCreate: Reference number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String name = "";
                String designation = "";
                String phone = "";
                String email = "";

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    switch (cursor.getColumnName(i)) {
                        case ReferenceContract.Columns.REFERENCE_NAME:
                            name = cursor.getString(i);
                            break;
                        case ReferenceContract.Columns.REFERENCE_DESIGNATION:
                            designation = cursor.getString(i);
                            break;
                        case ReferenceContract.Columns.REFERENCE_PHONE:
                            phone = cursor.getString(i);
                            break;
                        case ReferenceContract.Columns.REFERENCE_EMAIL:
                            email = cursor.getString(i);
                            break;
                        default:
                            //Nothing to do here
                    }
                }
                resume.getReferenceList().add(new Reference(name, designation, email, phone));
            }
            cursor.close();
        }

        //Query Project
        projection = new String[]{ProjectContract.Columns.PROJECT_NAME,
                ProjectContract.Columns.PROJECT_ROLE,
                ProjectContract.Columns.PROJECT_DESCRIPTION};

        cursor = contentResolver.query(ProjectContract.CONTENT_URI,
                projection,
                ProjectContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(id)},
                ProjectContract.Columns._ID);

        if (cursor != null) {
            Log.d(TAG, "onCreate: Project number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String name = "";
                String role = "";
                String desc = "";

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    switch (cursor.getColumnName(i)) {
                        case ProjectContract.Columns.PROJECT_NAME:
                            name = cursor.getString(i);
                            break;
                        case ProjectContract.Columns.PROJECT_ROLE:
                            role = cursor.getString(i);
                            break;
                        case ProjectContract.Columns.PROJECT_DESCRIPTION:
                            desc = cursor.getString(i);
                            break;
                        default:
                            //Nothing to do here
                    }
                }
                resume.getProjectList().add(new Project(name, role, desc));
            }
            cursor.close();
        }

        return resume;
    }


    /**
     * This method delete all the rows from all the tables that has the same resumeId
     *
     * @param contentResolver content Resolver required to access the database
     * @param resumeId        resumeId for the rows
     */
    public static void deleteEverything(ContentResolver contentResolver, long resumeId) {
        //Delete Resume Row
        contentResolver.delete(ResumeContract.buildResumeUri(resumeId), null, null);

        //Delete Education Rows
        contentResolver.delete(EducationContract.CONTENT_URI, EducationContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});

        //Delete Experience Rows
        contentResolver.delete(ExperienceContract.CONTENT_URI, ExperienceContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});

        //Delete Skill Rows
        contentResolver.delete(SkillContract.CONTENT_URI, SkillContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});

        //Delete Reference Rows
        contentResolver.delete(ReferenceContract.CONTENT_URI, ReferenceContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});

        //Delete Project Rows
        contentResolver.delete(ProjectContract.CONTENT_URI, ProjectContract.Columns.RESUME_ID + " = ?",
                new String[]{String.valueOf(resumeId)});
    }

    public static boolean checkImageInUse(ContentResolver contentResolver, String imagePath) {
        String[] projection = {ResumeContract.Columns.RESUME_IMG_PATH};

        Cursor cursor = contentResolver.query(ResumeContract.CONTENT_URI,
                projection,
                ResumeContract.Columns.RESUME_IMG_PATH + " = ?",
                new String[]{imagePath},
                null);

        if (cursor != null) {
            boolean inUse = cursor.getCount() > 1;
            cursor.close();
            return inUse;
        }
        return false;
    }

    public static String queryImagePath(ContentResolver contentResolver, long resumeId) {
        String[] projection = {ResumeContract.Columns.RESUME_IMG_PATH};

        Cursor cursor = contentResolver.query(ResumeContract.buildResumeUri(resumeId),
                projection,
                null,
                null,
                null);

        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();

        return path;
    }
}
