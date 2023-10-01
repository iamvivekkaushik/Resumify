package com.vivekkaushik.resumify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.adapters.EducationListAdapter;
import com.vivekkaushik.resumify.template_model.Education;

import java.util.Objects;

import static com.vivekkaushik.resumify.FormActivity.resume;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EducationFragment extends Fragment implements View.OnClickListener, EducationListAdapter.MenuButtonClickListener {
    EducationListAdapter adapter;
    Button addButton;

    EditText institute, course, startYear, endYear;
    Button cancelButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.educationListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new EducationListAdapter(getContext(), resume.getEduList());
        recyclerView.setAdapter(adapter);


        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        adapter.setMenuButtonListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    addButton.setVisibility(View.GONE);
                } else if (dy < 0) {
                    addButton.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        final AlertDialog dialog = createAddDialog();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveData(-1)) dialog.dismiss();
            }
        });
    }

    /**
     * This is a helper method to create a Alert Dialog
     *
     * @return returns the AlertDialog object
     */
    private AlertDialog createAddDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_edu, null);
        course = mView.findViewById(R.id.editTextCourse);
        institute = mView.findViewById(R.id.editTextInstitute);
        startYear = mView.findViewById(R.id.editTextStartYear);
        endYear = mView.findViewById(R.id.editTextEndYear);
        cancelButton = mView.findViewById(R.id.cancelButton);
        saveButton = mView.findViewById(R.id.saveButton);
        mBuilder.setView(mView);

        return mBuilder.create();
    }

    /**
     * This method add data to the Education List if the position is less than 0 and updates the
     * data if the position is more than 0
     *
     * @param position position whose data needs to update (pass -1 if you want to add new data)
     * @return returns true if the data is saved or updated
     */
    private boolean saveData(int position) {
        String courseString = course.getText().toString().trim();
        String instituteString = institute.getText().toString().trim();
        String startString = startYear.getText().toString().trim();
        String endString = endYear.getText().toString().trim();

        if (!courseString.isEmpty() && !instituteString.isEmpty() && !startString.isEmpty() && !endString.isEmpty()) {
            if (position < 0)
                resume.getEduList().add(new Education(courseString, instituteString, startString, endString));
            else {
                resume.getEduList().remove(position);
                resume.getEduList().add(position, new Education(courseString, instituteString, startString, endString));
            }

            adapter.loadNewData();
            return true;
        }

        return false;
    }

    @Override
    public void menuButtonClicked(View view, final int position) {
        final Education eduItem = resume.getEduList().get(position);
        //creating a popup menu
        PopupMenu popup = new PopupMenu(requireContext(), view);
        //inflating menu from xml resource
        popup.inflate(R.menu.form_list_item_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.form_list_item_edit) {
                    final AlertDialog dialog = createAddDialog();
                    course.setText(eduItem.getCourse());
                    institute.setText(eduItem.getInstitute());
                    startYear.setText(eduItem.getStartYear());
                    endYear.setText(eduItem.getEndYear());
                    dialog.show();

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (saveData(position)) dialog.dismiss();
                        }
                    });
                    return true;
                } else if (item.getItemId() == R.id.form_list_item_delete) {
                    resume.getEduList().remove(position);
                    adapter.loadNewData();
                    return true;
                }

                return false;
            }
        });
        //displaying the popup
        popup.show();
    }
}
