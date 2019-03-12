package com.code.chenjifff.httpapplication;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class githubReposAdapter extends RecyclerView.Adapter<githubReposViewHolder>{
    private ArrayList<Repos> data;
    private Context context;
    private com.code.chenjifff.httpapplication.githubReposAdapter.OnItemClickListener onItemClickListener;

    public githubReposAdapter(Context context, ArrayList<Repos> data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public void setOnItemClickListener(com.code.chenjifff.httpapplication.githubReposAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public githubReposViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        githubReposViewHolder holder =
                new githubReposViewHolder(LayoutInflater.from(context).inflate(R.layout.repos_detail, viewGroup, false));
        return holder;
    }

    private Handler handler = new Handler();
    @Override
    public void onBindViewHolder(final githubReposViewHolder viewHolder, int i) {
        if(onItemClickListener != null) {
            viewHolder.reposCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(viewHolder.getAdapterPosition());
                }
            });
        }
        viewHolder.name.setText(data.get(i).getName());
        viewHolder.id.setText(Integer.toString(data.get(i).getId()));
        viewHolder.issueNumber.setText(Integer.toString(data.get(i).getOpen_issues_count()));
        viewHolder.description.setText(data.get(i).getDescription());
    }
}
