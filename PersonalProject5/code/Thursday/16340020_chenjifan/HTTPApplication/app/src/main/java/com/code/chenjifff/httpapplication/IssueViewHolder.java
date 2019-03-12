package com.code.chenjifff.httpapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class IssueViewHolder extends RecyclerView.ViewHolder {
    TextView issueTitle;
    TextView issueCreate;
    TextView issueStatus;
    TextView issueDescription;

    public IssueViewHolder(View _view) {
        super(_view);
        issueTitle = (TextView) _view.findViewById(R.id.issue_title);
        issueCreate = (TextView) _view.findViewById(R.id.issue_create);
        issueStatus = (TextView) _view.findViewById(R.id.issue_status);
        issueDescription = (TextView) _view.findViewById(R.id.issue_description);
    }
}
