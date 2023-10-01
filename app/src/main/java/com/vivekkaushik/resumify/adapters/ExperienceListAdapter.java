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
import com.vivekkaushik.resumify.template_model.Experience;

import java.util.ArrayList;

public class ExperienceListAdapter extends RecyclerView.Adapter<ExperienceListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Experience> experienceList;
    private MenuButtonClickListener mbListener;

    public interface MenuButtonClickListener {
        void menuButtonClicked(View view, int position);
    }

    public ExperienceListAdapter(Context mContext, ArrayList<Experience> experienceList) {
        this.mContext = mContext;
        this.experienceList = experienceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.experience_layout_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Experience exp = experienceList.get(position);

        holder.jobTitleView.setText(exp.getJobTitle());
        String year = exp.getStarted() + " - " + exp.getEnded();
        holder.yearView.setText(year);
        holder.companyNameView.setText(exp.getCompanyName());
        holder.jobDescView.setText(exp.getJobDesc());

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbListener.menuButtonClicked(holder.moreButton, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return experienceList.size();
    }

    public void loadNewData() {
        notifyDataSetChanged();
    }

    public void setMenuButtonListener(MenuButtonClickListener m) {
        mbListener = m;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleView, yearView, companyNameView, jobDescView;
        ImageView moreButton;

        ViewHolder(View itemView) {
            super(itemView);
            jobTitleView = itemView.findViewById(R.id.jobTitle);
            yearView = itemView.findViewById(R.id.year);
            companyNameView = itemView.findViewById(R.id.companyName);
            jobDescView = itemView.findViewById(R.id.jobDescription);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}
