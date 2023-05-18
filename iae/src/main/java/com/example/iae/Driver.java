package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Driver {

    private Project project;
    private String path;
    private final String name = "main";
    private String language;
    private String compilerPath;
    private Map<String, String> env;
    private String libraries;
    private boolean compilerError = false;

    public Driver(Project project, String path) {
        this.project = project;
        this.path = path;
        this.language = project.getConfiguration().getSource();
        this.compilerPath = Paths.get(project.getConfiguration().getCompilerPath()).toAbsolutePath().normalize()
                .toString();
        this.env = new HashMap<>(System.getenv());
        env.put("PATH", compilerPath + File.pathSeparator + compilerPath + File.pathSeparator + env.get("PATH"));
        this.libraries = project.getConfiguration().getLibraries();

    }

    public boolean getCompileError() {
        return compilerError;
    }

    private String compileProject(File directory) {
        File[] fileList = directory.listFiles();
        String[] fileNames = new String[fileList.length];

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                fileNames[i] = fileName;
            }
        }

        String command = "cmd.exe /c ";

        switch (language) {

            case "C":
                command += "gcc -w -o " + name + ".exe ";
                for (String fileName : fileNames) {
                    if (fileName != null && (fileName.endsWith(".c") || fileName.endsWith(".h"))) {
                        command += fileName + " ";
                    }

                }

                command += libraries;
                command += "&& " + name;
                break;

            case "C++":
                command += "g++ -w -o " + name + ".exe ";
                for (String fileName : fileNames) {
                    if (fileName != null && (fileName.endsWith(".cpp") || fileName.endsWith(".h"))) {
                        command += fileName + " ";
                    }
                }
                command += libraries;
                command += "&& " + name;
                break;
            case "Java":
                command += "javac *.java ";
                command += libraries;
                command += "&& java " + name;

                break;
            case "Dart":
                command += "dart ";
                command += libraries;
                command += name + ".dart";
                break;
            case "Python":
                command += "py -m py_compile " + name + ".py ";
                command += libraries;
                command += "&& " + "py " + name + ".py";

                break;
            default:
                break;
        }

        return command;
    }

    public void evaluateAllStudents() throws IOException {
        File folder = new File(path);
        for (File studentFolder : folder.listFiles()) {
            if (studentFolder.isFile() && studentFolder.getName().endsWith(".zip")) {
                studentFolder = ZipExtractor.extractZipFile(studentFolder.getAbsolutePath());

            }

            boolean isPassed = evaluateStudent(studentFolder);

            project.addStudent(new Student(studentFolder.getName(), isPassed));

        }
        if (compilerError == false) {
            DBConnection.getInstance().addProject(project);

        }

    }

    private boolean evaluateStudent(File studentFolder) {
        ArrayList<String> output = new ArrayList<String>();
        try {
            String[] commandArgs = compileProject(studentFolder).split("\\s+");
            ProcessBuilder builder = new ProcessBuilder(commandArgs);
            builder.environment().putAll(env);
            builder.directory(studentFolder);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            OutputStream outputStream = process.getOutputStream();
            File inputFile = new File(project.getInputFilePath());
            byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
            outputStream.write(inputBytes);
            outputStream.close();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                output.add(line);
            }

            process.waitFor();
            process.destroy();

            if (output.get(0).toString().contains("is not recognized as an internal or external command,")) {
                compilerError = true;
            }

            return compareOutput(output);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean compareOutput(ArrayList<String> outputLines) {

        int i = 0;
        try (
                BufferedReader br = new BufferedReader(new FileReader(project.getOutputFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i == outputLines.size())
                    return false;

                if (!line.equals(outputLines.get(i)))
                    return false;
                i++;
            }

            if (i != outputLines.size())
                return false;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return true;

    }

    public class ZipExtractor {
        public static File extractZipFile(String zipFilePath) {
            try {
                File zipFile = new File(zipFilePath);
                String outputDirectory = zipFile.getParent() + File.separator
                        + getFileNameWithoutExtension(zipFile.getName());

                byte[] buffer = new byte[1024];
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
                ZipEntry zipEntry = zipInputStream.getNextEntry();

                while (zipEntry != null) {
                    String entryPath = outputDirectory + File.separator + zipEntry.getName();
                    File entryFile = new File(entryPath);

                    if (zipEntry.isDirectory()) {
                        entryFile.mkdirs();
                    } else {
                        entryFile.getParentFile().mkdirs();
                        FileOutputStream fos = new FileOutputStream(entryFile);
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        fos.close();
                    }

                    zipEntry = zipInputStream.getNextEntry();
                }

                zipInputStream.closeEntry();
                zipInputStream.close();

                System.out.println("Zip file extracted  to: " + outputDirectory);
                File file = new File(outputDirectory);
                zipFile.delete();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String getFileNameWithoutExtension(String fileName) {
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                return fileName.substring(0, index);
            }
            return fileName;
        }

    }
}