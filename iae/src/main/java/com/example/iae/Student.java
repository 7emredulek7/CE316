package com.example.iae;

public class Student {
    private String id;
    private boolean isPassed;
    public Student(String id, boolean isPassed) {
        this.id = id;
        this.isPassed = isPassed;
    }

    public String getId() {
        return id;
    }

    public boolean isPassed() {
        return isPassed;
    }
}
