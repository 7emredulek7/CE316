package com.example.iae;

import java.util.ArrayList;

public class Project {
    private String name;
    private String inputFilePath;
    private String outputFilePath;

    private ArrayList<Student> students;

    private Config configuration;

    public Project(String name, String inputFilePath, String outputFilePath, Config configuration) {
        this.name = name;
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.configuration = configuration;
        this.students = new ArrayList<>();
    }

    public Project(String name, String inputFilePath, String outputFilePath, ArrayList<Student> students, Config configuration) {
        this.name = name;
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.students = students;
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Config getConfiguration() {
        return configuration;
    }

    public void addStudent(Student s){
        students.add(s);
    }

    @Override
    public String toString() {
        return name;
    }
}
