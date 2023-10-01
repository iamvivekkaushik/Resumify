package com.vivekkaushik.resumify.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.vivekkaushik.resumify.database.AppProvider.CONTENT_AUTHORITY;
import static com.vivekkaushik.resumify.database.AppProvider.CONTENT_AUTHORITY_URI;

public class ProjectContract {
    static final String TABLE_NAME = "Project";

    //Columns
    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String PROJECT_NAME = "ProjectName";
        public static final String PROJECT_ROLE = "Role";
        public static final String PROJECT_DESCRIPTION = "ProjectDescription";
        public static final String RESUME_ID = "ResumeId";

        private Columns (){
            //private constructor to prevent instantiation
        }
    }

    /**
     * The URI to access Project table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    /**
     * helper method to build uri with a projectID
     * @param taskId id you want to add to the uri
     * @return returns the Uri
     */
    public static Uri buildProjectUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }

    /**
     * This method retrieves the id from the URI
     * @param uri uri that contains the id
     * @return returns the id
     */
    static long getProjectId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
