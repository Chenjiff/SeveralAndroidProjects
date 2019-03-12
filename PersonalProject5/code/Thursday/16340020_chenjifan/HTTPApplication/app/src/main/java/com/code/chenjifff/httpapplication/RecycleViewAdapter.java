package com.code.chenjifff.httpapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.LogRecord;

public  class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder>{
    private ArrayList<Video> data;
    private Context context;
    private OnItemClickListener itemClickListener;

    public RecycleViewAdapter(Context context, ArrayList<Video> data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickListener {
        void OnItemLongClick(int i);
        void OnItemClick(int i);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecycleViewHolder holder =
                new RecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.result_detail, viewGroup, false));
        return holder;
    }

    private Handler handler = new Handler();
    @Override
    public void onBindViewHolder(final RecycleViewHolder viewHolder, int i) {
//        if(itemClickListener != null) {
//            viewHolder.foodName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.OnItemClick(viewHolder.getAdapterPosition());
//                }
//            });
//            viewHolder.foodName.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    itemClickListener.OnItemLongClick(viewHolder.getAdapterPosition());
//                    return true;
//                }
//            });
//        }
        viewHolder.cover.setImageResource(R.drawable.loading);
        viewHolder.playNumber.setText(Integer.toString(data.get(i).getPlayNumber()));
        viewHolder.commentNumber.setText(Integer.toString(data.get(i).getCommentNumber()));
        viewHolder.duration.setText(data.get(i).getDuration());
        viewHolder.createTime.setText(data.get(i).getCreateTime());
        viewHolder.title.setText(data.get(i).getTitle());
        viewHolder.content.setText(data.get(i).getContent());
        final String cover = data.get(i).getCoverPath();
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(cover);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                        if(conn.getResponseCode() == 200){
                            InputStream inputStream = conn.getInputStream();
                            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.cover.setImageBitmap(bitmap);
                                }
                            });
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();

    }
}


