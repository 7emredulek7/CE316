package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
    Project project;

    private static String compileProject(String language, String projectName) {
        String command = "default";
        switch (language) {
            case "C":
                command = "cmd.exe /c gcc -o " + projectName + ".exe " + projectName + ".c & " + projectName;
                break;
            case "C++":
                command = "cmd.exe /c g++ -o " + projectName + ".exe " + projectName + ".cpp & " + projectName;
                break;
            case "Python":
                command = "cmd.exe /c py " + projectName + ".py";
                break;
            case "Java":
                command = "cmd.exe /c javac " + projectName + ".java & java " + projectName;
                break;
            case "Dart":
                command = "cmd.exe /c dart run " + projectName + ".dart";
                break;
            default:
                break;
        }
        return command;
    }

    static void studentEvaluation(String path, String language, String projectName, String inputPath) {
        try {
            String[] commandArgs = compileProject(language, projectName).split("\\s+");
            ProcessBuilder builder = new ProcessBuilder(commandArgs);
            builder.inheritIO();
            builder.directory(new File(path));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            builder.redirectInput(new File(inputPath));
            Process executeProcess = builder.start();

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            ((Throwable) e).printStackTrace();
        }

    }
}
