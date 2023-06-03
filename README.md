# CE316
Integrated Assignment Environment (IAE)

The Integrated Assignment Environment (IAE) is a lightweight integrated development environment designed to help educators manage programming assignments efficiently. This system provides an interface for creating new assignments, managing student submissions, and reporting the status of each student's assignment. IAE is configurable for various programming languages, including C, C++, Java, and Python.

Features

    Assignment Management: Create new assignments, download student submissions in ZIP format, and manage the results.

    Multi-Language Support: IAE can be configured for different programming languages.

    Scenario Sharing: Create different scenarios as configurations for different programming languages and share them with others.

    Error Handling: IAE can handle errors in student files, such as compilation errors, and continue with the next student.

    Results Storage: Save and view assignment results within the IAE.

Structure

The IAE is comprised of four main classes:

    Configuration: Stores all the information required to compile/interpret code files.

    Student: Reports the status of each student’s assignment.

    Project: Represents all the structure and information about the assignment or project.

    Driver: Handles the execution of student’s code in zip files.

Content of IAE

    Windows Operating System: IAE is designed to run on Windows.

    Help Menu: Includes a manual displayed with a "Help" menu item to guide the user.

    Project Creation: User can create a project that uses an existing or a new configuration.

    Configuration Management: User can create, edit, and remove a configuration.

    Import/Export Configurations: User can import and export configurations.

    Zip File Processing: IAE can process ZIP files for each student.

    Code Execution: IAE can compile or interpret source code using the configuration of the project.

    Output Comparison: IAE can compare the output of the student program and the expected output.

    Result Display: IAE displays the results of each student file.

Database

IAE uses SQL Lite on localhost to store student information and assignment results.

Usage

    Install the software using the provided installer.
    
    Open IAE and navigate to create a new project.

    Fill up the project name and select an existing configuration or create a new one.
    
   ![resim](https://github.com/7emredulek7/CE316/assets/80330367/f790b4b5-e062-4964-88de-cbb5afe96bcd)


    If creating a new configuration, enter information like the configuration's name, compiler path, and libraries to be used.
    
   ![resim](https://github.com/7emredulek7/CE316/assets/80330367/9cdf3703-cb81-49f3-aa07-d4036605309a)

    
    You can find your compiler path by writing "where <compilerName>" to your command prompt and getting the path without "compilerName.exe" part.
    
   ![resim](https://github.com/7emredulek7/CE316/assets/80330367/6ce0c2da-1117-49c5-9912-513cbeb7c831)


    If using an existing configuration, select from a list of all available configurations.
    
    You can also delete, import or edit the configuration that you created.
    
   ![resim](https://github.com/7emredulek7/CE316/assets/80330367/bf0cb718-f489-48a8-97fa-8aca0c7864c2)    

    Enter the file path for the solution folder, sample input to be tested, and the sample output to be returned.
    
    For souce file you should use a parent directory for your all student codes. As you can see in the image you should choose StudentC directory and program will iterate inside of it. 
    
   ![resim](https://github.com/7emredulek7/CE316/assets/80330367/679c1207-6d93-40b1-83f0-ed39a14b8678)


    You can upload student submissions with or without ZIP format and run the assignment.
   
    View and manage assignment results.
    
   ![resim](https://github.com/7emredulek7/CE316/assets/80330367/b51bf4fb-dfe2-4058-909f-763a434ddd50)

Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
