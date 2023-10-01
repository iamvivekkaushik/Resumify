package com.vivekkaushik.resumify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.model.ResumeList;

import java.util.ArrayList;

public class UserResumeAdapter extends RecyclerView.Adapter<UserResumeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ResumeList> mResumeList;
    private MenuButtonClickListener mbListener;

    public interface MenuButtonClickListener {
        void menuButtonClicked(View view, int position);

        void listItemClicked(int position);
    }

    public UserResumeAdapter(Context mContext, ArrayList<ResumeList> resumeList) {
        this.mContext = mContext;
        this.mResumeList = resumeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_resume_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ResumeList listItem = mResumeList.get(position);

        holder.fileNameView.setText(listItem.getFileName());
        String template = "Template " + listItem.getTemplateId();
        holder.dateView.setText(template);
        Glide.with(mContext)
                .load(listItem.getResumeImage())
                .into(holder.resumeImageView);

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbListener.menuButtonClicked(holder.moreButton, holder.getAbsoluteAdapterPosition());
            }
        });
        holder.listItem.setOnClickListener(view -> mbListener.listItemClicked(holder.getAbsoluteAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mResumeList.size();
    }

    public void loadNewData(ArrayList<ResumeList> resumeList) {
        this.mResumeList = resumeList;
        notifyDataSetChanged();
    }

    public void setMenuButtonListener(MenuButtonClickListener m) {
        mbListener = m;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView listItem;
        ImageView resumeImageView;
        TextView fileNameView;
        TextView dateView;
        ImageView moreButton;

        public ViewHolder(View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.list_item);
            resumeImageView = itemView.findViewById(R.id.resumeImage);
            fileNameView = itemView.findViewById(R.id.fileName);
            dateView = itemView.findViewById(R.id.dateCreated);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}
