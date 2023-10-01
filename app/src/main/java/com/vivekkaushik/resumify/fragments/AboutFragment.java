package com.vivekkaushik.resumify.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.TemplatesActivity;
import com.vivekkaushik.resumify.helper.TWatcher;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.vivekkaushik.resumify.FormActivity.resume;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {
    private static final String TAG = "AboutFragment";
    // Request code
    private static final int PERMISSION_CONSTANT = 100;
    private static final int RESULT_LOAD_IMAGE = 99;
    //View
    //static NachoTextView languageView, hobbyView;
    EditText firstName, lastName, job, objective, mobile, email, website, address, languageView, hobbyView;
    TextView templateView;
    Button changeTemplateButton;
    ImageView resumeImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        resumeImage = view.findViewById(R.id.resumeImage);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        job = view.findViewById(R.id.job);
        objective = view.findViewById(R.id.objective);
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        website = view.findViewById(R.id.website);
        address = view.findViewById(R.id.address);
        languageView = view.findViewById(R.id.languageView);
        hobbyView = view.findViewById(R.id.hobbyView);
        templateView = view.findViewById(R.id.templateTextView);
        changeTemplateButton = view.findViewById(R.id.templateButton);

        //setupChipTextView(languageView);
        //setupChipTextView(hobbyView);

        setImage(); // this method set the user image to imageView
        populateEditText();

        firstName.addTextChangedListener(new TWatcher(TWatcher.RESUME_FIRST_NAME));
        lastName.addTextChangedListener(new TWatcher(TWatcher.RESUME_LAST_NAME));
        job.addTextChangedListener(new TWatcher(TWatcher.RESUME_JOB));
        objective.addTextChangedListener(new TWatcher(TWatcher.RESUME_OBJECTIVE));
        mobile.addTextChangedListener(new TWatcher(TWatcher.RESUME_MOBILE));
        email.addTextChangedListener(new TWatcher(TWatcher.RESUME_EMAIL));
        website.addTextChangedListener(new TWatcher(TWatcher.RESUME_WEBSITE));
        address.addTextChangedListener(new TWatcher(TWatcher.RESUME_ADDRESS));
        languageView.addTextChangedListener(new TWatcher(TWatcher.RESUME_LANGUAGE));
        hobbyView.addTextChangedListener(new TWatcher(TWatcher.RESUME_HOBBY));

        changeTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutFragment.this.getContext(), TemplatesActivity.class);
                intent.putExtra("templateChange", true);
                startActivity(intent);
            }
        });

        resumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    getPermission(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }
            }
        });
        return view;
    }

    public boolean checkPermission(String permission) {
        //check if permission is granted
        return requireContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void getPermission(String[] permission) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(requireActivity(), permission, PERMISSION_CONSTANT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted, Now do whatever the fuck you wanted to do
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    // Son of a bitch denied the permission. He will go to hell for this.
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called");
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

//            CropImage.activity(selectedImage)
//                    .setAspectRatio(1, 1)
//                    .setFixAspectRatio(true)
//                    .start(requireContext()), AboutFragment.this);
      }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                deleteIfExist(resume.getImgLocation());
//                Uri resultUri = result.getUri();
//                resume.setImgLocation(resultUri.getPath());
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Log.e(TAG, "onActivityResult: " + error);
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Also populating TextView
        String templateName = "Template " + resume.getTemplateId();
        templateView.setText(templateName);
        setImage();
    }

    private void setImage() {
        if (resume.getImgLocation().isEmpty()) {
            return;
        }
        Glide.with(requireContext())
                .load(resume.getImgLocation())
                .into(resumeImage);
    }

    private void populateEditText() {
        firstName.setText(resume.getFirstName());
        lastName.setText(resume.getLastName());
        job.setText(resume.getJob());
        objective.setText(resume.getObjective());
        mobile.setText(resume.getMobile());
        email.setText(resume.getEmail());
        website.setText(resume.getWebsite());
        address.setText(resume.getAddress());
        languageView.setText(resume.getLanguage());
        hobbyView.setText(resume.getHobby());
        //languageView.setText(getChipList(resume.getLanguage()));
        //hobbyView.setText(getChipList(resume.getHobby()));
    }

    private void deleteIfExist(String path) {
        if (path.isEmpty()) {
            return;
        }

        File file = new File(path);

        if (file.exists()) {
            file.delete();
        }
    }

    /*private ArrayList<String> getChipList(String string) {
        ArrayList<String> list = new ArrayList<>();

        while (string.contains(", ")) {
            String s = string.substring(0, string.indexOf(", "));
            list.add(s);
            if (s.length() >= 2) {
                string = string.substring(string.indexOf(", ") + 2);
            }
        }
        if (!string.isEmpty()) list.add(string);

        return list;
    }*/

    /*private void setupChipTextView(NachoTextView nachoTextView) {
        nachoTextView.setIllegalCharacters('\"');
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nachoTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        nachoTextView.setNachoValidator(new ChipifyingNachoValidator());
        nachoTextView.enableEditChipOnTouch(true, true);
    }*/

    public static void changeTemplate(int template){
        resume.setTemplateId(template);
    }

    /*public static void saveChips() {
        List<String> list = languageView.getChipValues();
        resume.setLanguage(list.toString().substring(1, list.toString().length() - 1));

        list = hobbyView.getChipValues();
        resume.setHobby(list.toString().substring(1, list.toString().length() - 1));
    }*/
}
