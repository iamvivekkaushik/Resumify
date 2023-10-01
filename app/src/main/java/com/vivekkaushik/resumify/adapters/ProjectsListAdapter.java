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
import com.vivekkaushik.resumify.template_model.Project;

import java.util.ArrayList;

public class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Project> projectsList;
    private MenuButtonClickListener mbListener;

    public interface MenuButtonClickListener {
        void menuButtonClicked(View view, int position);
    }

    public ProjectsListAdapter(Context mContext, ArrayList<Project> projectsList) {
        this.mContext = mContext;
        this.projectsList = projectsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Project project = projectsList.get(position);

        holder.projectNameView.setText(project.getProjectName());
        holder.projectRoleView.setText(project.getProjectRole());
        holder.projectDescView.setText(project.getProjectDescription());

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbListener.menuButtonClicked(holder.moreButton, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    public void loadNewData() {
        notifyDataSetChanged();
    }

    public void setMenuButtonListener(MenuButtonClickListener m) {
        mbListener = m;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView projectNameView, projectRoleView, projectDescView;
        ImageView moreButton;

        public ViewHolder(View itemView) {
            super(itemView);
            projectNameView = itemView.findViewById(R.id.projectTitleView);
            projectRoleView = itemView.findViewById(R.id.roleView);
            projectDescView = itemView.findViewById(R.id.projectDescriptionView);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}
