package com.vivekkaushik.resumify.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.vivekkaushik.resumify.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int RESUME = 100;
    private static final int RESUME_ID = 101;

    private static final int EDUCATION = 200;
    private static final int EDUCATION_ID = 201;

    private static final int EXPERIENCE = 300;
    private static final int EXPERIENCE_ID = 301;

    private static final int REFERENCE = 400;
    private static final int REFERENCE_ID = 401;

    private static final int PROJECT = 500;
    private static final int PROJECT_ID = 501;

    private static final int SKILL = 600;
    private static final int SKILL_ID = 601;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, ResumeContract.TABLE_NAME, RESUME);
        matcher.addURI(CONTENT_AUTHORITY, ResumeContract.TABLE_NAME + "/#", RESUME_ID);

        matcher.addURI(CONTENT_AUTHORITY, EducationContract.TABLE_NAME, EDUCATION);
        matcher.addURI(CONTENT_AUTHORITY, EducationContract.TABLE_NAME + "/#", EDUCATION_ID);

        matcher.addURI(CONTENT_AUTHORITY, ExperienceContract.TABLE_NAME, EXPERIENCE);
        matcher.addURI(CONTENT_AUTHORITY, ExperienceContract.TABLE_NAME + "/#", EXPERIENCE_ID);

        matcher.addURI(CONTENT_AUTHORITY, ReferenceContract.TABLE_NAME, REFERENCE);
        matcher.addURI(CONTENT_AUTHORITY, ReferenceContract.TABLE_NAME + "/#", REFERENCE_ID);

        matcher.addURI(CONTENT_AUTHORITY, ProjectContract.TABLE_NAME, PROJECT);
        matcher.addURI(CONTENT_AUTHORITY, ProjectContract.TABLE_NAME + "/#", PROJECT_ID);

        matcher.addURI(CONTENT_AUTHORITY, SkillContract.TABLE_NAME, SKILL);
        matcher.addURI(CONTENT_AUTHORITY, SkillContract.TABLE_NAME + "/#", SKILL_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: called with uri: " + uri);

        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: query match is: " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case RESUME:
                queryBuilder.setTables(ResumeContract.TABLE_NAME);
                break;
            case RESUME_ID:
                queryBuilder.setTables(ResumeContract.TABLE_NAME);
                long resumeId = ResumeContract.getResumeId(uri);
                queryBuilder.appendWhere(ResumeContract.Columns._ID + " = " + resumeId);
                break;
            case EDUCATION:
                queryBuilder.setTables(EducationContract.TABLE_NAME);
                break;
            case EDUCATION_ID:
                queryBuilder.setTables(EducationContract.TABLE_NAME);
                long educationId = EducationContract.getEducationId(uri);
                queryBuilder.appendWhere(EducationContract.Columns._ID + " = " + educationId);
                break;
            case EXPERIENCE:
                queryBuilder.setTables(ExperienceContract.TABLE_NAME);
                break;
            case EXPERIENCE_ID:
                queryBuilder.setTables(ExperienceContract.TABLE_NAME);
                long experienceId = ExperienceContract.getExperienceId(uri);
                queryBuilder.appendWhere(ExperienceContract.Columns._ID + " = " + experienceId);
                break;
            case REFERENCE:
                queryBuilder.setTables(ReferenceContract.TABLE_NAME);
                break;
            case REFERENCE_ID:
                queryBuilder.setTables(ReferenceContract.TABLE_NAME);
                long referenceId = ReferenceContract.getReferenceId(uri);
                queryBuilder.appendWhere(ReferenceContract.Columns._ID + " = " + referenceId);
                break;
            case PROJECT:
                queryBuilder.setTables(ProjectContract.TABLE_NAME);
                break;
            case PROJECT_ID:
                queryBuilder.setTables(ProjectContract.TABLE_NAME);
                long projectId = ProjectContract.getProjectId(uri);
                queryBuilder.appendWhere(ProjectContract.Columns._ID + " = " + projectId);
                break;
            case SKILL:
                queryBuilder.setTables(SkillContract.TABLE_NAME);
                break;
            case SKILL_ID:
                queryBuilder.setTables(SkillContract.TABLE_NAME);
                long skillId = SkillContract.getSkillId(uri);
                queryBuilder.appendWhere(SkillContract.Columns._ID + " = " + skillId);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Log.d(TAG, "query: " + queryBuilder.toString());
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RESUME:
                return ResumeContract.CONTENT_TYPE;
            case RESUME_ID:
                return ResumeContract.CONTENT_ITEM_TYPE;
            case EDUCATION:
                return EducationContract.CONTENT_TYPE;
            case EDUCATION_ID:
                return EducationContract.CONTENT_ITEM_TYPE;
            case EXPERIENCE:
                return ExperienceContract.CONTENT_TYPE;
            case EXPERIENCE_ID:
                return ExperienceContract.CONTENT_ITEM_TYPE;
            case REFERENCE:
                return ReferenceContract.CONTENT_TYPE;
            case REFERENCE_ID:
                return ReferenceContract.CONTENT_ITEM_TYPE;
            case PROJECT:
                return ProjectContract.CONTENT_TYPE;
            case PROJECT_ID:
                return ProjectContract.CONTENT_ITEM_TYPE;
            case SKILL:
                return SkillContract.CONTENT_TYPE;
            case SKILL_ID:
                return SkillContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert: Start with: " + uri);

        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "insert: match is : " + match);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch (match) {
            case RESUME:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ResumeContract.TABLE_NAME, null, values);

                if (recordId > 0) {
                    returnUri = ResumeContract.buildResumeUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case EDUCATION:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(EducationContract.TABLE_NAME, null, values);

                if (recordId > 0) {
                    returnUri = EducationContract.buildEducationUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case EXPERIENCE:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ExperienceContract.TABLE_NAME, null, values);

                if (recordId > 0) {
                    returnUri = ExperienceContract.buildExperienceUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case REFERENCE:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ReferenceContract.TABLE_NAME, null, values);

                if (recordId > 0) {
                    returnUri = ReferenceContract.buildReferenceUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case PROJECT:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ProjectContract.TABLE_NAME, null, values);

                if (recordId > 0) {
                    returnUri = ProjectContract.buildProjectUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case SKILL:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(SkillContract.TABLE_NAME, null, values);

                if (recordId > 0) {
                    returnUri = SkillContract.buildSkillUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri.toString());
        }
        db.close();
        Log.d(TAG, "insert: Exiting insert, returnUri: " + returnUri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete: called with uri: " + uri);

        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "delete: match is: " + match);

        SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (match) {
            case RESUME:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ResumeContract.TABLE_NAME, selection, selectionArgs);
                break;
            case RESUME_ID:
                db = mOpenHelper.getWritableDatabase();
                long resumeId = ResumeContract.getResumeId(uri);
                selectionCriteria = ResumeContract.Columns._ID + " = " + resumeId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(ResumeContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            case EDUCATION:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(EducationContract.TABLE_NAME, selection, selectionArgs);
                break;
            case EDUCATION_ID:
                db = mOpenHelper.getWritableDatabase();
                long educationId = EducationContract.getEducationId(uri);
                selectionCriteria = EducationContract.Columns._ID + " = " + educationId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(EducationContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            case EXPERIENCE:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ExperienceContract.TABLE_NAME, selection, selectionArgs);
                break;
            case EXPERIENCE_ID:
                db = mOpenHelper.getWritableDatabase();
                long experienceId = ExperienceContract.getExperienceId(uri);
                selectionCriteria = ExperienceContract.Columns._ID + " = " + experienceId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(ExperienceContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            case REFERENCE:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ReferenceContract.TABLE_NAME, selection, selectionArgs);
                break;
            case REFERENCE_ID:
                db = mOpenHelper.getWritableDatabase();
                long referenceId = ReferenceContract.getReferenceId(uri);
                selectionCriteria = ReferenceContract.Columns._ID + " = " + referenceId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(ReferenceContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            case PROJECT:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ProjectContract.TABLE_NAME, selection, selectionArgs);
                break;
            case PROJECT_ID:
                db = mOpenHelper.getWritableDatabase();
                long projectId = ProjectContract.getProjectId(uri);
                selectionCriteria = ProjectContract.Columns._ID + " = " + projectId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(ProjectContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            case SKILL:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(SkillContract.TABLE_NAME, selection, selectionArgs);
                break;
            case SKILL_ID:
                db = mOpenHelper.getWritableDatabase();
                long skillId = SkillContract.getSkillId(uri);
                selectionCriteria = SkillContract.Columns._ID + " = " + skillId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(SkillContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        db.close();
        Log.d(TAG, "delete: exiting, returning: " + count);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update: called with: " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "update: match is: " + match);

        final SQLiteDatabase db;

        int count;
        String selectionCriteria;

        switch (match) {
            case RESUME:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ResumeContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case RESUME_ID:
                db = mOpenHelper.getWritableDatabase();
                long resumeId = ResumeContract.getResumeId(uri);
                selectionCriteria = ResumeContract.Columns._ID + " = " + resumeId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(ResumeContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            case EDUCATION:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(EducationContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case EDUCATION_ID:
                db = mOpenHelper.getWritableDatabase();
                long educationId = EducationContract.getEducationId(uri);
                selectionCriteria = EducationContract.Columns._ID + " = " + educationId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(EducationContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            case EXPERIENCE:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ExperienceContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case EXPERIENCE_ID:
                db = mOpenHelper.getWritableDatabase();
                long experienceId = ExperienceContract.getExperienceId(uri);
                selectionCriteria = ExperienceContract.Columns._ID + " = " + experienceId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(ExperienceContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            case REFERENCE:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ReferenceContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REFERENCE_ID:
                db = mOpenHelper.getWritableDatabase();
                long referenceId = ReferenceContract.getReferenceId(uri);
                selectionCriteria = ReferenceContract.Columns._ID + " = " + referenceId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(ReferenceContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            case PROJECT:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ProjectContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PROJECT_ID:
                db = mOpenHelper.getWritableDatabase();
                long projectId = ProjectContract.getProjectId(uri);
                selectionCriteria = ProjectContract.Columns._ID + " = " + projectId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(ProjectContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            case SKILL:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(SkillContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SKILL_ID:
                db = mOpenHelper.getWritableDatabase();
                long skillId = SkillContract.getSkillId(uri);
                selectionCriteria = SkillContract.Columns._ID + " = " + skillId;

                if ((selection != null) && (selection.length() > 0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(SkillContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        db.close();
        return count;
    }
}
