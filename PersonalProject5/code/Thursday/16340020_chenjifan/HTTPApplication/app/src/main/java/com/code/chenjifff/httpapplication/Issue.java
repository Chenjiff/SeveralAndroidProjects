package com.code.chenjifff.httpapplication;

public class Issue {
    private String title;
    private String created_at;
    private String state;
    private String body;

    public Issue(String title, String create, String status, String description) {
        this.title = title;
        this.created_at = create;
        this.state = status;
        this.body = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
