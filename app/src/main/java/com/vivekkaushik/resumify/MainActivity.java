package com.vivekkaushik.resumify;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vivekkaushik.resumify.adapters.UserResumeAdapter;
import com.vivekkaushik.resumify.database.ResumeContract;
import com.vivekkaushik.resumify.helper.QueryHelper;
import com.vivekkaushik.resumify.helper.ResumeImage;
import com.vivekkaushik.resumify.model.ResumeList;
import com.vivekkaushik.resumify.templates.Template1;
import com.vivekkaushik.resumify.templates.Template10;
import com.vivekkaushik.resumify.templates.Template2;
import com.vivekkaushik.resumify.templates.Template3;
import com.vivekkaushik.resumify.templates.Template4;
import com.vivekkaushik.resumify.templates.Template5;
import com.vivekkaushik.resumify.templates.Template6;
import com.vivekkaushik.resumify.templates.Template7;
import com.vivekkaushik.resumify.templates.Template8;
import com.vivekkaushik.resumify.templates.Template9;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements UserResumeAdapter.MenuButtonClickListener {
    private static final String TAG = "MainActivity";
    private static final String FOLDER_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Resumify";
    RecyclerView recyclerView;
    UserResumeAdapter resumeAdapter;
    FloatingActionButton fab;
    ArrayList<ResumeList> resumeList;

    // Request code
    private static final int PERMISSION_CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resumeAdapter = new UserResumeAdapter(this, new ArrayList<ResumeList>());
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setAdapter(resumeAdapter);

        resumeAdapter.setMenuButtonListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TemplatesActivity.class);
                intent.putExtra("templateChange", false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeList = createMainList(getContentResolver());
        resumeAdapter.loadNewData(resumeList);
    }

    private ArrayList<ResumeList> createMainList(ContentResolver contentResolver) {
        ArrayList<ResumeList> resumeList = new ArrayList<>();

        int resumeId = -1;
        String firstName = null;
        String lastName = null;
        String template = null;

        String[] projection = {ResumeContract.Columns._ID, ResumeContract.Columns.RESUME_FIRST_NAME,
                ResumeContract.Columns.RESUME_LAST_NAME,
                ResumeContract.Columns.RESUME_TEMPLATE};

        Cursor cursor = contentResolver.query(ResumeContract.CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (cursor != null) {
            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    Log.d(TAG, "" + cursor.getColumnName(i) + " : " + cursor.getString(i) + "\n");
                    switch (cursor.getColumnName(i)) {
                        case ResumeContract.Columns._ID:
                            resumeId = Integer.parseInt(cursor.getString(i));
                            break;
                        case ResumeContract.Columns.RESUME_FIRST_NAME:
                            firstName = cursor.getString(i);
                            break;
                        case ResumeContract.Columns.RESUME_LAST_NAME:
                            lastName = cursor.getString(i);
                            break;
                        case ResumeContract.Columns.RESUME_TEMPLATE:
                            template = cursor.getString(i);
                            break;
                        default:
                            Log.d(TAG, "createMainList: No idea what to do HERE????");
                    }
                }
                resumeList.add(new ResumeList(resumeId, firstName + " " + lastName,
                        Integer.parseInt(template), ResumeImage.getImage(template)));
            }
            cursor.close();
        }

        return resumeList;
    }




    private boolean saveToStorage(PdfDocument document, ResumeList resumeItem) {
        File folderPath = getExternalFilesDir("Resumify");
        //Write the document content
        String name = String.valueOf(resumeItem.getTemplateId());
        assert folderPath != null;
        String targetPdf = folderPath.getPath() + File.separator + name + ".pdf";
        File filePath = new File(targetPdf);

        Toast.makeText(getApplicationContext(), "Generating PDF...", Toast.LENGTH_SHORT).show();

        AsyncTask.execute(() -> {
            try {
                document.writeTo(Files.newOutputStream(filePath.toPath()));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to save file", Toast.LENGTH_SHORT).show();
            } finally {
                document.close();
            }
        });


        Toast.makeText(this, "Saved: " + filePath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean checkPermission(String permission) {
        //check if permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
        return true;
//        if (this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//            //You have permission
//            return true;
//        }
//        //You don't have permission
//        return false;
    }

    public void getPermission(String[] permission) {
        // No explanation needed, we can request the permission.
        this.requestPermissions( permission, PERMISSION_CONSTANT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted, Now do whatever the fuck you wanted to do

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    // Son of a bitch denied the permission. He will go to hell for this.
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void menuButtonClicked(View view, final int position) {
        final ResumeList resumeItem = resumeList.get(position);
        //creating a popup menu
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        //inflating menu from xml resource
        popup.inflate(R.menu.main_list_item_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.main_list_item_share) {
                shareResume(resumeItem);
                return true;
            } else if (item.getItemId() == R.id.main_list_item_generate) {
                generateResume(resumeItem);
                return true;
            } else if (item.getItemId() == R.id.main_list_item_edit) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("resumeId", resumeItem.getResumeId());
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.main_list_item_delete) {
                deleteImageIfNotInUse(resumeItem.getResumeId());
                QueryHelper.deleteEverything(getContentResolver(), resumeItem.getResumeId());
                resumeList = createMainList(getContentResolver());
                resumeAdapter.loadNewData(resumeList);
                return true;
            }

            return false;
        });
        //displaying the popup
        popup.show();
    }

    private void deleteImageIfNotInUse(long id) {
        String imagePath = QueryHelper.queryImagePath(getContentResolver(), id);
        boolean inUse = QueryHelper.checkImageInUse(getContentResolver(), imagePath);

        if (!inUse) {
            File file = new File(imagePath);

            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public void listItemClicked(int position) {
        openResume(resumeList.get(position));
    }

    private void shareResume(ResumeList resumeItem) {
        String name = String.valueOf(resumeItem.getTemplateId());

        String path = Objects.requireNonNull(getExternalFilesDir("Resumify")).getPath();
        File file = new File(path + File.separator + name + ".pdf");

        Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileProvider", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setType("application/pdf");

        startActivity(Intent.createChooser(shareIntent, "Share With"));
    }

    private void openResume(ResumeList resumeItem) {
//        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//            getPermission(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE});
//            return;
//        }

        String name = String.valueOf(resumeItem.getTemplateId());

        String path = Objects.requireNonNull(getExternalFilesDir("Resumify")).getPath();
        File file = new File(path + File.separator + name + ".pdf");

        if (!file.exists()) {
            generateResume(resumeItem);
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileProvider", file);
        intent.setDataAndType(fileUri, "application/pdf");
        startActivity(Intent.createChooser(intent, "Open With"));
    }

    private void generateResume(ResumeList resumeItem) {
//        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//            getPermission(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE});
//            return;
//        }
        switch (resumeItem.getTemplateId()) {
            case 1:
                Template1 template = new Template1(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template.createPdf(), resumeItem);
                break;
            case 2:
                Template2 template2 = new Template2(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template2.createPdf(), resumeItem);
                break;
            case 3:
                Template3 template3 = new Template3(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template3.createPdf(), resumeItem);
                break;
            case 4:
                Template4 template4 = new Template4(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template4.createPdf(), resumeItem);
                break;
            case 5:
                Template5 template5 = new Template5(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template5.createPdf(), resumeItem);
                break;
            case 6:
                Template6 template6 = new Template6(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template6.createPdf(), resumeItem);
                break;
            case 7:
                Template7 template7 = new Template7(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template7.createPdf(), resumeItem);
                break;
            case 8:
                Template8 template8 = new Template8(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template8.createPdf(), resumeItem);
                break;
            case 9:
                Template9 template9 = new Template9(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template9.createPdf(), resumeItem);
                break;
            case 10:
                Template10 template10 = new Template10(this, QueryHelper.queryEverything(getContentResolver(), resumeItem.getResumeId()));
                saveToStorage(template10.createPdf(), resumeItem);
                break;
            default:
                Log.e(TAG, "generateResume: Unknown Resume");
        }
    }
}
