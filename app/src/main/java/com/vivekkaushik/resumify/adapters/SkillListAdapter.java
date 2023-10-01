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
import com.vivekkaushik.resumify.template_model.Skill;

import java.util.ArrayList;

public class SkillListAdapter extends RecyclerView.Adapter<SkillListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Skill> skillList;
    private MenuButtonClickListener mbListener;

    public interface MenuButtonClickListener {
        void menuButtonClicked(View view, int position);
    }

    public SkillListAdapter(Context mContext, ArrayList<Skill> skillList) {
        this.mContext = mContext;
        this.skillList = skillList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_list_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Skill skill = skillList.get(position);

        holder.skill.setText(skill.getSkill());
        String skillLevel = "Skill Level : " + skill.getSkillLevel() + "%";
        holder.skillLevel.setText(skillLevel);

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbListener.menuButtonClicked(holder.moreButton, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    public void loadNewData(ArrayList<Skill> skillList) {
        this.skillList = skillList;
        notifyDataSetChanged();
    }

    public void setMenuButtonListener(MenuButtonClickListener m) {
        mbListener = m;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView skill, skillLevel;
        ImageView moreButton;

        public ViewHolder(View itemView) {
            super(itemView);
            skill = itemView.findViewById(R.id.skill);
            skillLevel = itemView.findViewById(R.id.skillLevel);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}
