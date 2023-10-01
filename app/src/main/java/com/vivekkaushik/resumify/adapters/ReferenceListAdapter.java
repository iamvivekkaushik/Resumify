package com.vivekkaushik.resumify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.template_model.Reference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReferenceListAdapter extends RecyclerView.Adapter<ReferenceListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Reference> referencesList;
    private MenuButtonClickListener mbListener;

    public interface MenuButtonClickListener {
        void menuButtonClicked(View view, int position);
    }

    public ReferenceListAdapter(Context mContext, ArrayList<Reference> referencesList) {
        this.mContext = mContext;
        this.referencesList = referencesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reference_list_item,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Reference reference = referencesList.get(position);

        holder.nameView.setText(reference.getName());
        holder.designationView.setText(reference.getDesignation());
        holder.emailView.setText(reference.getEmail());
        holder.phoneView.setText(reference.getPhone());

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbListener.menuButtonClicked(holder.moreButton, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return referencesList.size();
    }

    public void loadNewData() {
        notifyDataSetChanged();
    }

    public void setMenuButtonListener(MenuButtonClickListener m) {
        mbListener = m;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, designationView, emailView, phoneView;
        ImageView moreButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.referenceName);
            designationView = itemView.findViewById(R.id.designation);
            emailView = itemView.findViewById(R.id.referenceEmail);
            phoneView = itemView.findViewById(R.id.referencePhone);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}
