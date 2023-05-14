package com.example.iae;

import java.io.Serializable;
import java.util.ArrayList;

public class Config implements Serializable {
    private String name;
    private String source;
    private ArrayList<String> libraries = new ArrayList<String>();
    private String compilerPath;

    public Config(String name, String source, ArrayList<String> libraries, String compilerPath) {
        this.name = name;
        this.source = source;
        this.libraries = libraries;
        this.compilerPath = compilerPath;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<String> getLibraries() {
        return libraries;
    }

    public void setLibraries(ArrayList<String> libraries) {
        this.libraries = libraries;
    }

    public String getCompilerPath() {
        return compilerPath;
    }

    public void setCompilerPath(String compilerPath) {
        this.compilerPath = compilerPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
