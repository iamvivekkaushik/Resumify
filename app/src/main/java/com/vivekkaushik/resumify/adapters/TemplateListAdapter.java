package com.vivekkaushik.resumify.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vivekkaushik.resumify.FormActivity;
import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.TemplatesActivity;
import com.vivekkaushik.resumify.fragments.AboutFragment;
import com.vivekkaushik.resumify.model.Template;

import java.util.ArrayList;

public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Template> templatesList;
    private boolean templateSelect;

    public TemplateListAdapter(Context mContext, ArrayList<Template> templatesList, boolean templateSelect) {
        this.mContext = mContext;
        this.templatesList = templatesList;
        this.templateSelect = templateSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_list_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Template template = templatesList.get(position);

        Glide.with(mContext)
                .load(template.getImage())
                .into(holder.templateImageView);

        holder.templateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (templateSelect) {
                    AboutFragment.changeTemplate(holder.getAdapterPosition() + 1);
                }
                else {
                    Intent intent = new Intent(mContext, FormActivity.class);
                    intent.putExtra("isEdit", false);
                    intent.putExtra("resumeId", 0);
                    intent.putExtra("templateId", holder.getAdapterPosition() + 1);
                    mContext.startActivity(intent);
                }

                ((TemplatesActivity)mContext).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return templatesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView templateImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            templateImageView = itemView.findViewById(R.id.templateImageView);
        }
    }
}
