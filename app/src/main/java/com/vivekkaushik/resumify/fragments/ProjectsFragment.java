package com.vivekkaushik.resumify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.adapters.ProjectsListAdapter;
import com.vivekkaushik.resumify.template_model.Project;

import static com.vivekkaushik.resumify.FormActivity.resume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectsFragment extends Fragment implements View.OnClickListener, ProjectsListAdapter.MenuButtonClickListener {
    ProjectsListAdapter adapter;
    Button addButton;

    EditText projectName, projectRole, projectDesc;
    Button cancelButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.projectListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProjectsListAdapter(getContext(), resume.getProjectList());
        recyclerView.setAdapter(adapter);

        addButton = view.findViewById(R.id.projectAddButton);
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
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_project, null);
        projectName = mView.findViewById(R.id.editTextProjectName);
        projectRole = mView.findViewById(R.id.editTextRole);
        projectDesc = mView.findViewById(R.id.editTextProjectDesc);
        cancelButton = mView.findViewById(R.id.cancelButton);
        saveButton = mView.findViewById(R.id.saveButton);
        mBuilder.setView(mView);
        return mBuilder.create();
    }

    /**
     * This method add data to the Project List if the position is less than 0 and updates the
     * data if the position is more than 0
     *
     * @param position position whose data needs to update (pass -1 if you want to add new data)
     * @return returns true if the data is saved or updated
     */
    private boolean saveData(int position) {
        String projectNameString = projectName.getText().toString().trim();
        String projectRoleString = projectRole.getText().toString().trim();
        String projectDescString = projectDesc.getText().toString().trim();

        if (!projectNameString.isEmpty() && !projectRoleString.isEmpty() && !projectDescString.isEmpty()) {
            if (position < 0)
                resume.getProjectList().add(new Project(projectNameString, projectRoleString, projectDescString));
            else {
                resume.getProjectList().remove(position);
                resume.getProjectList().add(position, new Project(projectNameString, projectRoleString, projectDescString));
            }

            adapter.loadNewData();
            return true;
        }

        return false;
    }

    @Override
    public void menuButtonClicked(View view, final int position) {
        final Project projectItem = resume.getProjectList().get(position);
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
                    projectName.setText(projectItem.getProjectName());
                    projectRole.setText(projectItem.getProjectRole());
                    projectDesc.setText(projectItem.getProjectDescription());
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
                } else if (item.getItemId() == R.id.form_list_item_edit) {
                    resume.getProjectList().remove(position);
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
