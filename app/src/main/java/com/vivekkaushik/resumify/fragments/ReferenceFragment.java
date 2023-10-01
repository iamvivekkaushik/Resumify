package com.vivekkaushik.resumify.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.adapters.ReferenceListAdapter;
import com.vivekkaushik.resumify.template_model.Reference;


import static com.vivekkaushik.resumify.FormActivity.resume;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReferenceFragment extends Fragment implements View.OnClickListener, ReferenceListAdapter.MenuButtonClickListener {
    ReferenceListAdapter adapter;
    Button addButton;

    EditText referenceName, designation, email, phone;
    Button cancelButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reference, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.referenceListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ReferenceListAdapter(getContext(), resume.getReferenceList());
        recyclerView.setAdapter(adapter);

        addButton = view.findViewById(R.id.referenceAddButton);
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
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_reference, null);
        referenceName = mView.findViewById(R.id.editTextReferenceName);
        designation = mView.findViewById(R.id.editTextDesignation);
        email = mView.findViewById(R.id.editTextEmail);
        phone = mView.findViewById(R.id.editTextPhone);
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
        String referenceNameString = referenceName.getText().toString().trim();
        String designationString = designation.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String phoneString = phone.getText().toString().trim();

        if (!referenceNameString.isEmpty() && !designationString.isEmpty() && !emailString.isEmpty() &&
                !phoneString.isEmpty()) {

            if (position < 0)
                resume.getReferenceList().add(new Reference(referenceNameString, designationString,
                        emailString, phoneString));
            else {
                resume.getReferenceList().remove(position);
                resume.getReferenceList().add(position, new Reference(referenceNameString, designationString,
                        emailString, phoneString));
            }

            adapter.loadNewData();
            return true;
        }

        return false;
    }

    @Override
    public void menuButtonClicked(View view, final int position) {
        final Reference refItem = resume.getReferenceList().get(position);
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
                    referenceName.setText(refItem.getName());
                    designation.setText(refItem.getDesignation());
                    email.setText(refItem.getEmail());
                    phone.setText(refItem.getPhone());
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
                    resume.getReferenceList().remove(position);
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
