package com.fodala.service.impl;

import com.fodala.ant.task.CustomBuildListener;
import com.fodala.service.AntRunner;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;

@Service
public class AntRunnerImpl implements AntRunner {
    private static final Logger logger = LoggerFactory.getLogger(AntRunnerImpl.class);

    @Override
    public void run(String buildFilePath, Map<String, String> properties){
        Project project = new Project();
        project.init();

        // Load the build file
        File buildFile = new File(buildFilePath);
        ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
        project.addReference("ant.projectHelper", projectHelper);
        project.addBuildListener(new CustomBuildListener());
        projectHelper.parse(project, buildFile);
        // Configure the project logging
        DefaultLogger logger = new AntRunnerLogger();

        LoggerFactory.getLogger(AntRunnerImpl.class);

        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        logger.setMessageOutputLevel(Project.MSG_INFO);
        project.addBuildListener(logger);

        // Set the base directory for the project
        project.setBaseDir(new File(buildFile.getParent()));

        // Set a property (in this example, the folderPath property)
        properties.forEach(project::setProperty);

        // Execute the default target (or any other target you want to run)
        project.executeTarget(project.getDefaultTarget());
    }

    class AntRunnerLogger extends DefaultLogger {
        @Override
        protected void printMessage(String message, PrintStream stream, int priority) {
            logger.info(message);
            super.printMessage(message, stream, priority);
        }
    }
}
