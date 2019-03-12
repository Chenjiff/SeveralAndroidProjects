package com.code.chenjifff.httpapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecycleViewHolder extends RecyclerView.ViewHolder {
    ImageView cover;
    TextView playNumber;
    TextView commentNumber;
    TextView duration;
    TextView createTime;
    TextView title;
    TextView content;
    public RecycleViewHolder(View _view) {
        super(_view);
        cover = (ImageView) _view.findViewById(R.id.cover);
        playNumber = (TextView) _view.findViewById(R.id.play_number);
        commentNumber = (TextView) _view.findViewById(R.id.comment_number);
        duration = (TextView) _view.findViewById(R.id.duration_number);
        createTime = (TextView) _view.findViewById(R.id.create_time);
        title = (TextView) _view.findViewById(R.id.title);
        content = (TextView) _view.findViewById(R.id.content);
    }
}
