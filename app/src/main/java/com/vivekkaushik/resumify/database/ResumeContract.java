package com.vivekkaushik.resumify.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.vivekkaushik.resumify.database.AppProvider.CONTENT_AUTHORITY;
import static com.vivekkaushik.resumify.database.AppProvider.CONTENT_AUTHORITY_URI;


/**
 * Contract Class for Resume Table
 */
public class ResumeContract {
    static final String TABLE_NAME = "Resume";

    //Columns
    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String RESUME_FIRST_NAME = "FirstName";
        public static final String RESUME_LAST_NAME = "LastName";
        public static final String RESUME_JOB = "Job";
        public static final String RESUME_OBJECTIVE = "Objective";
        public static final String RESUME_MOBILE = "Mobile";
        public static final String RESUME_EMAIL = "Email";
        public static final String RESUME_WEBSITE = "Website";
        public static final String RESUME_ADDRESS = "Address";
        public static final String RESUME_LANGUAGE = "Language";
        public static final String RESUME_HOBBY = "Hobby";
        public static final String RESUME_TEMPLATE = "Template";
        public static final String RESUME_IMG_PATH = "ImagePath";

        private Columns() {
            //private constructor to prevent instantiation
        }
    }

    /**
     * The URI to access Resume table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    /**
     * helper method to build uri with a resumeID
     *
     * @param taskId id you want to add to the uri
     * @return returns the Uri
     */
    public static Uri buildResumeUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }

    /**
     * This method retrieves the id from the URI
     *
     * @param uri uri that contains the id
     * @return returns the id
     */
    public static long getResumeId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
