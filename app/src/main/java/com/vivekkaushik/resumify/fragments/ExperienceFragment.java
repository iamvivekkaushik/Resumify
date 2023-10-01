package com.vivekkaushik.resumify.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.adapters.ExperienceListAdapter;
import com.vivekkaushik.resumify.template_model.Experience;

import java.util.Objects;

import static com.vivekkaushik.resumify.FormActivity.resume;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExperienceFragment extends Fragment implements View.OnClickListener, ExperienceListAdapter.MenuButtonClickListener {
    ExperienceListAdapter adapter;
    Button addButton;

    EditText jobTitle, companyName, startYear, endYear, jobDesc;
    Button cancelButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_experience, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.experienceListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);
        adapter = new ExperienceListAdapter(getContext(), resume.getExpList());
        recyclerView.setAdapter(adapter);

        addButton = view.findViewById(R.id.experienceAddButton);
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
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_exp, null);
        jobTitle = mView.findViewById(R.id.editTextJobTitle);
        companyName = mView.findViewById(R.id.editTextCompany);
        startYear = mView.findViewById(R.id.editTextStartYear);
        endYear = mView.findViewById(R.id.editTextEndYear);
        jobDesc = mView.findViewById(R.id.editTextJobDesc);
        cancelButton = mView.findViewById(R.id.cancelButton);
        saveButton = mView.findViewById(R.id.saveButton);
        mBuilder.setView(mView);

        return mBuilder.create();
    }

    /**
     * This method add data to the Experience List if the position is less than 0 and updates the
     * data if the position is more than 0
     *
     * @param position position whose data needs to update (pass -1 if you want to add new data)
     * @return returns true if the data is saved or updated
     */
    private boolean saveData(int position) {
        String jobTitleString = jobTitle.getText().toString().trim();
        String companyNameString = companyName.getText().toString().trim();
        String startString = startYear.getText().toString().trim();
        String endString = endYear.getText().toString().trim();
        String jobDescString = jobDesc.getText().toString().trim();

        if (!jobTitleString.isEmpty() && !companyNameString.isEmpty() && !startString.isEmpty() &&
                !endString.isEmpty() && !jobDescString.isEmpty()) {

            if (position < 0)
                resume.getExpList().add(new Experience(jobTitleString, companyNameString, startString,
                        endString, jobDescString));
            else {
                resume.getExpList().remove(position);
                resume.getExpList().add(position, new Experience(jobTitleString, companyNameString, startString,
                        endString, jobDescString));
            }

            adapter.loadNewData();
            return true;
        }

        return false;
    }

    @Override
    public void menuButtonClicked(View view, final int position) {
        final Experience expItem = resume.getExpList().get(position);
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
                    jobTitle.setText(expItem.getJobTitle());
                    companyName.setText(expItem.getCompanyName());
                    startYear.setText(expItem.getStarted());
                    endYear.setText(expItem.getEnded());
                    jobDesc.setText(expItem.getJobDesc());
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
                    resume.getExpList().remove(position);
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
