package com.github.common.api.model;

import java.util.List;

/**
 * Created by zlove on 2018/1/24.
 */

public class Movie {
    private int count;
    private int start;
    private int total;
    private String title;
    private List<Object> subjects;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Object> subjects) {
        this.subjects = subjects;
    }
}
