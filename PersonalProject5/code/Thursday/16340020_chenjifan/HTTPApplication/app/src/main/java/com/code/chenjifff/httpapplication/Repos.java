package com.code.chenjifff.httpapplication;

public class Repos {
    private String name;
    private int id;
    private int open_issues_count;
    private String description;
    private boolean has_issues;

    public boolean isHas_issues() {
        return has_issues;
    }

    public void setHas_issues(boolean has_issues) {
        this.has_issues = has_issues;
    }

    public Repos(String name, int id, int issueNumber, String description, boolean hasIssue) {
        this.name = name;
        this.id = id;
        this.open_issues_count = issueNumber;
        this.description = description;
        this.has_issues = hasIssue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(int open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
