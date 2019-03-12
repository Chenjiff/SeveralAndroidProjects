package com.code.chenjifff.httpapplication;

public class Video {
    private String coverPath;
    private int playNumber;
    private int commentNumber;
    private String duration;
    private String createTime;
    private String title;
    private String content;

    public Video(String coverPath, int playNumber, int commentNumber, String duration, String createTime, String title, String content) {
        this.coverPath = coverPath;
        this.playNumber = playNumber;
        this.commentNumber = commentNumber;
        this.duration = duration;
        this.createTime = createTime;
        this.title = title;
        this.content = content;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public int getPlayNumber() {
        return playNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public String getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getContent() {
        return content;
    }
}
