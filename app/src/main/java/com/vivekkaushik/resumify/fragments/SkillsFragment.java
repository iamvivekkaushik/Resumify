package com.vivekkaushik.resumify.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.adapters.SkillListAdapter;
import com.vivekkaushik.resumify.template_model.Skill;

import static com.vivekkaushik.resumify.FormActivity.resume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SkillsFragment extends Fragment implements View.OnClickListener, SkillListAdapter.MenuButtonClickListener {
    SkillListAdapter adapter;
    Button addButton;

    EditText skill, skillLevel;
    Button cancelButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skills, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.skillListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SkillListAdapter(getContext(), resume.getSkillList());
        recyclerView.setAdapter(adapter);

        addButton = view.findViewById(R.id.skillAddButton);
        addButton.setOnClickListener(this);
        adapter.setMenuButtonListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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
        View mView = getLayoutInflater().inflate(R.layout.dialog_skill, null);
        skill = mView.findViewById(R.id.editTextSkill);
        skillLevel = mView.findViewById(R.id.editTextSkillLevel);
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
        String skillString = skill.getText().toString().trim();
        String sLevel = skillLevel.getText().toString().trim();

        if (!skillString.isEmpty() && !sLevel.isEmpty()) {
            //Check if skillLevel contains %
            if (sLevel.contains("%")) {
                sLevel = sLevel.substring(0, sLevel.indexOf("%"));
            }
            //skill level in integer
            float skillL;
            skillL = Float.parseFloat(sLevel);

            if (position < 0) resume.getSkillList().add(new Skill(skillString, skillL));
            else {
                resume.getSkillList().remove(position);
                resume.getSkillList().add(position, new Skill(skillString, skillL));
            }

            adapter.loadNewData(resume.getSkillList());
            return true;
        }

        return false;
    }

    @Override
    public void menuButtonClicked(View view, final int position) {
        final Skill skillItem = resume.getSkillList().get(position);
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
                    skill.setText(skillItem.getSkill());
                    skillLevel.setText(String.valueOf(skillItem.getSkillLevel()));
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
                    resume.getSkillList().remove(position);
                    adapter.loadNewData(resume.getSkillList());
                    return true;
                }

                return false;
            }
        });
        //displaying the popup
        popup.show();
    }
}
