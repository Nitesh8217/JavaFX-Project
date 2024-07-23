package jforkts.in.myjavafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class ExcelToPostgresApp extends Application {

    @FXML
    private Label dataStatus;

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            // Call the method to import data from Excel to PostgreSQL
            importDataFromExcel(selectedFile);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void importDataFromExcel(File file) {

            String jdbcURL = "jdbc:postgresql://localhost:5432/KLSGit";
            String username = "postgres";
            String password = "nitesh";

            DataFormatter formatter = new DataFormatter();
            String excelFilePath = file.getAbsolutePath();

            try (FileInputStream inputStream = new FileInputStream(excelFilePath);
                 Workbook workbook = new XSSFWorkbook(inputStream);
                 Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
                System.out.println("Connected to POSTGRES SQL");
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell cell1 = row.getCell(0);
                    Cell cell2 = row.getCell(1);

                    Cell cell3 = row.getCell(2);
                    Cell cell4 = row.getCell(3);

                    Cell cell5 = row.getCell(4);
                    Cell cell6 = row.getCell(5);

                    // Assuming two columns in Excel: col1 and col2
                    String col1 = cell1.getStringCellValue();
                    String col2 = formatter.formatCellValue(cell2);

                    String col3 = formatter.formatCellValue(cell3); //.getStringCellValue();
                    String col4 = cell4.getStringCellValue();

                    String col5 = cell5.getStringCellValue();
                    String col6 = cell6.getStringCellValue();

                    String sql = "INSERT INTO student1(usn, sname,sem,branch,gender,participation) VALUES (?,?,?,?,?,?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, col1);
                        statement.setString(2, col2);
                        statement.setInt(3, Integer.parseInt(col3));
                        statement.setString(4, col4);
                        statement.setString(5, col5);
                        statement.setString(6, col6);
                        statement.executeUpdate();
                        System.out.println("Data is imported...");
                        dataStatus.setText("Data imported Succesfully...");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                dataStatus.setText("Already Exists..");
            }
        }
        // Implement the method to read Excel and import data to PostgreSQL
}
