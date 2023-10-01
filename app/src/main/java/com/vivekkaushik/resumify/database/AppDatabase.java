package com.vivekkaushik.resumify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "ResumeMaker.db";
    public static final int DATABASE_VERSION = 1;

    //Implement app database as a singleton
    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     * This method make sure that there is only one instance of AppDatabase class.
     *
     * @param context context required to create the instance
     * @return AppDatabase object
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");

        String sSQL;
        sSQL = "CREATE TABLE " + ResumeContract.TABLE_NAME + "("
                + ResumeContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + ResumeContract.Columns.RESUME_FIRST_NAME + " TEXT, "
                + ResumeContract.Columns.RESUME_LAST_NAME + " TEXT, "
                + ResumeContract.Columns.RESUME_JOB + " TEXT, "
                + ResumeContract.Columns.RESUME_OBJECTIVE + " TEXT, "
                + ResumeContract.Columns.RESUME_MOBILE + " TEXT, "
                + ResumeContract.Columns.RESUME_EMAIL + " TEXT, "
                + ResumeContract.Columns.RESUME_WEBSITE + " TEXT, "
                + ResumeContract.Columns.RESUME_ADDRESS + " TEXT, "
                + ResumeContract.Columns.RESUME_LANGUAGE + " TEXT, "
                + ResumeContract.Columns.RESUME_HOBBY + " TEXT, "
                + ResumeContract.Columns.RESUME_TEMPLATE + " INTEGER NOT NULL, "
                + ResumeContract.Columns.RESUME_IMG_PATH + " TEXT);";

        Log.d(TAG, "onCreate: Resume Table: " + sSQL);
        db.execSQL(sSQL);

        sSQL = "CREATE TABLE " + EducationContract.TABLE_NAME + "("
                + EducationContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + EducationContract.Columns.EDUCATION_COURSE + " TEXT NOT NULL, "
                + EducationContract.Columns.EDUCATION_START + " DATETIME NOT NULL, "
                + EducationContract.Columns.EDUCATION_END + " DATETIME NOT NULL, "
                + EducationContract.Columns.EDUCATION_INSTITUTE + " TEXT NOT NULL, "
                + EducationContract.Columns.RESUME_ID + " INTEGER NOT NULL);";

        Log.d(TAG, "onCreate: Education Table: " + sSQL);
        db.execSQL(sSQL);

        sSQL = "CREATE TABLE " + ExperienceContract.TABLE_NAME + "("
                + ExperienceContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + ExperienceContract.Columns.EXPERIENCE_JOB + " TEXT NOT NULL, "
                + ExperienceContract.Columns.EXPERIENCE_START + " DATETIME NOT NULL, "
                + ExperienceContract.Columns.EXPERIENCE_END + " DATETIME NOT NULL, "
                + ExperienceContract.Columns.EXPERIENCE_COMPANY + " TEXT NOT NULL, "
                + ExperienceContract.Columns.EXPERIENCE_JOB_DESC + " TEXT NOT NULL, "
                + ExperienceContract.Columns.RESUME_ID + " INTEGER NOT NULL);";

        Log.d(TAG, "onCreate: Experience Table: " + sSQL);
        db.execSQL(sSQL);

        sSQL = "CREATE TABLE " + SkillContract.TABLE_NAME + "("
                + SkillContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + SkillContract.Columns.SKILL + " TEXT NOT NULL, "
                + SkillContract.Columns.SKILL_LEVEL + " INTEGER NOT NULL, "
                + SkillContract.Columns.RESUME_ID + " INTEGER NOT NULL);";
        Log.d(TAG, "onCreate: Skill Table: " + sSQL);
        db.execSQL(sSQL);

        sSQL = "CREATE TABLE " + ReferenceContract.TABLE_NAME + "("
                + ReferenceContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + ReferenceContract.Columns.REFERENCE_NAME + " TEXT NOT NULL, "
                + ReferenceContract.Columns.REFERENCE_DESIGNATION + " TEXT NOT NULL, "
                + ReferenceContract.Columns.REFERENCE_EMAIL + " TEXT NOT NULL, "
                + ReferenceContract.Columns.REFERENCE_PHONE + " TEXT NOT NULL, "
                + ReferenceContract.Columns.RESUME_ID + " INTEGER NOT NULL);";

        Log.d(TAG, "onCreate: Reference Table: " + sSQL);
        db.execSQL(sSQL);

        sSQL = "CREATE TABLE " + ProjectContract.TABLE_NAME + "("
                + ProjectContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + ProjectContract.Columns.PROJECT_NAME + " TEXT NOT NULL, "
                + ProjectContract.Columns.PROJECT_ROLE + " TEXT NOT NULL, "
                + ProjectContract.Columns.PROJECT_DESCRIPTION + " TEXT NOT NULL, "
                + ProjectContract.Columns.RESUME_ID + " INTEGER NOT NULL);";

        Log.d(TAG, "onCreate: Project Table: " + sSQL);
        db.execSQL(sSQL);

        Log.d(TAG, "onCreate: Ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Starts");

        switch (oldVersion) {
            case 1:
                //Upgrade logic from version 1
                break;
        }
    }
}
