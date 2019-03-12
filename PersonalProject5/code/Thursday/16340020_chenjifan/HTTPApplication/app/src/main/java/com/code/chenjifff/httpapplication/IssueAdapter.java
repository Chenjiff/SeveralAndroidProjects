package com.code.chenjifff.httpapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class IssueAdapter extends RecyclerView.Adapter<IssueViewHolder> {
    private ArrayList<Issue> data;
    private Context context;

    public IssueAdapter(Context context, ArrayList<Issue> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        IssueViewHolder holder =
                new IssueViewHolder(LayoutInflater.from(context).inflate(R.layout.issue_detail, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final IssueViewHolder viewHolder, int i) {
        viewHolder.issueTitle.setText(data.get(i).getTitle());
        viewHolder.issueCreate.setText(data.get(i).getCreated_at());
        viewHolder.issueStatus.setText(data.get(i).getState());
        viewHolder.issueDescription.setText(data.get(i).getBody());
    }
}
