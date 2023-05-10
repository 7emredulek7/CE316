package com.example.iae;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
    private static DBConnection instance = null;
    private final String fileName;
    private Connection conn;

    private PreparedStatement insertProject, insertStudent;
    private PreparedStatement getProject, getStudents, getAllProjectNames;

    private DBConnection() {
        this.fileName = "info.db";
        File file = new File(fileName);
        boolean firstRun = !file.exists();
        conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            if (firstRun) {
                Statement stat = conn.createStatement();

                stat.executeUpdate("CREATE TABLE PROJECT(" +
                        "NAME TEXT PRIMARY KEY," +
                        "INPUT_FILE TEXT NOT NULL," +
                        "OUTPUT_FILE TEXT NOT NULL," +
                        "CONFIG_FILE TEXT NOT NULL)");

                stat.executeUpdate("CREATE TABLE STUDENT(" +
                        "PROJECT_NAME TEXT NOT NULL," +
                        "ID TEXT NOT NULL," +
                        "IS_PASSED INT NOT NULL," +
                        "PRIMARY KEY(PROJECT_NAME, ID) )");

                /* Insertion into database */
                insertProject = conn.prepareStatement("INSERT INTO PROJECT (NAME, INPUT_FILE, OUTPUT_FILE, CONFIG_FILE) VALUES (? ? ? ?)");
                insertStudent = conn.prepareStatement("INSERT INTO STUDENT (PROJECT_NAME, ID, IS_PASSED) VALUES (? ? ?)");

                /* Selection from database */
                getProject = conn.prepareStatement("SELECT * FROM PROJECT WHERE NAME = ?");
                getStudents = conn.prepareStatement("SELECT * FROM STUDENT WHERE PROJECT_NAME = ?");
                getAllProjectNames = conn.prepareStatement("SELECT NAME FROM PROJECT");

            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static DBConnection getInstance() {

        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public void addProject(Project project) { // Add project object to database with student and configuration info
        try {
            insertProject.setString(1, project.getName());
            insertProject.setString(2, project.getInputFilePath());
            insertProject.setString(3, project.getOutputFilePath());
            insertProject.setString(4, project.getConfiguration().getName());

            for(Student student : project.getStudents()) {
                int isPassed = student.isPassed() ? 1 : 0;

                try {
                    insertStudent.setString(1, student.getId());
                    insertStudent.setInt(2, isPassed);
                    insertStudent.setString(3, project.getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Project getProject(String projectName) { //Given the projectName, returns the project objects with students result and configuration info.
        Project p;
        try {
            getProject.setString(1, projectName);
            getProject.execute();
            ResultSet rs = getProject.executeQuery();
            rs.next();

            String name =  rs.getString(1);
            String inputFile = rs.getString(2);
            String outputFile = rs.getString(3);
            String configFileName = rs.getString(4);
            Config configFile = null;

            for(Config c : MainController.configurationsList) {
                if(c.getName().equals(configFileName))
                    configFile = c;
            }

            ArrayList<Student> students = new ArrayList<>();
            getStudents.setString(1, projectName);
            getStudents.execute();
            ResultSet studentRs = getStudents.executeQuery();

            while(studentRs.next()) {
                String studentID = rs.getString(2);
                boolean isPassed = rs.getInt(3) == 1;
                students.add(new Student(studentID, isPassed));
            }

            p = new Project(name, inputFile, outputFile, students, configFile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return p;
    }

    public ArrayList<Project> getAllProjects() { // Returns all the project objects in database.
        ArrayList<Project> projects = new ArrayList<>();
        try {
            getAllProjectNames.execute();
            ResultSet rs = getAllProjectNames.executeQuery();

            while(rs.next()) {
                String projectName = rs.getString(1);
                projects.add(getProject(projectName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return projects;
    }





}

