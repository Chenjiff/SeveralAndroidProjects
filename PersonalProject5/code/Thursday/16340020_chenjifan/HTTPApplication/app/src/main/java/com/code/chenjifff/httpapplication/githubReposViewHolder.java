package com.code.chenjifff.httpapplication;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class githubReposViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView id;
    TextView issueNumber;
    TextView description;
    CardView reposCardView;

    public githubReposViewHolder(View _view) {
        super(_view);
        name = (TextView) _view.findViewById(R.id.name);
        id = (TextView) _view.findViewById(R.id.id);
        issueNumber = (TextView) _view.findViewById(R.id.issue_number);
        description = (TextView) _view.findViewById(R.id.description);
        reposCardView = (CardView) _view.findViewById(R.id.repos_card_view);
    }
}
