package com.fodala.ant.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class CsvToDatabaseTask extends Task {

    private String csvFilePath;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String tableName;

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void execute() throws BuildException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Assuming the table has two columns, modify this as per your table structure
                String query = "INSERT INTO " + tableName + " (column1, column2) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, values[0]);
                    preparedStatement.setString(2, values[1]);
                    preparedStatement.executeUpdate();
                }
            }

        } catch (Exception e) {
            throw new BuildException("Error importing CSV to database", e);
        }
    }

    private static void generateSqlInsertStatements(String tableName, String[] headers, List<String[]> rows) {
        for (String[] row : rows) {
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO ").append(tableName).append(" (");
            for (String header : headers) {
                query.append(header).append(", ");
            }
            // Remove the trailing comma and space
            query.setLength(query.length() - 2);
            query.append(") VALUES (");
            for (String value : row) {
                query.append("'").append(value).append("', ");
            }
            // Remove the trailing comma and space
            query.setLength(query.length() - 2);
            query.append(");");
            return query.toString();
        }
    }
}

