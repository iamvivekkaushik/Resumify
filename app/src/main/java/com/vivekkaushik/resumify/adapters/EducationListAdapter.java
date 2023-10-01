package com.vivekkaushik.resumify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.template_model.Education;

import java.util.ArrayList;

public class EducationListAdapter extends RecyclerView.Adapter<EducationListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Education> educationList;
    private MenuButtonClickListener mbListener;

    public interface MenuButtonClickListener {
        void menuButtonClicked(View view, int position);
    }

    public EducationListAdapter(Context mContext, ArrayList<Education> educationList) {
        this.mContext = mContext;
        this.educationList = educationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_list_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Education edu = educationList.get(position);

        holder.courseName.setText(edu.getCourse());
        String year = edu.getStartYear() + " - " + edu.getEndYear();
        holder.yearView.setText(year);
        holder.instituteName.setText(edu.getInstitute());

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbListener.menuButtonClicked(holder.moreButton, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }

    public void loadNewData() {
        notifyDataSetChanged();
    }

    public void setMenuButtonListener(MenuButtonClickListener m) {
        mbListener = m;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, yearView, instituteName;
        ImageView moreButton;

        ViewHolder(View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            yearView = itemView.findViewById(R.id.year);
            instituteName = itemView.findViewById(R.id.instituteName);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}
