package jforkts.in.myjavafx;


import java.awt.*;
import java.io.*;
import java.sql.*;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class HelloController implements Initializable {

    Connection myCon=null;

    String gender;
    String participation="";

    @FXML
    private ImageView collegeImg;
    @FXML
    private CheckBox ncc;
    @FXML
    private CheckBox nss;
    @FXML
    private CheckBox rc;

    @FXML
    private Label welcomeText;
    Boolean firstTime=true;
    @FXML
    private Button addStudent;
    @FXML
    private Label dataStatus;
    @FXML
    private Label msg;

    @FXML
    private TextField stdUsn;

    @FXML
    private RadioButton rb1;
    @FXML
    private RadioButton rb2;
    @FXML
    private Button writeToExcel;
    @FXML
    private TextField stdName;
    @FXML
    private TextField stdSem;
    @FXML
    private TextField stdBranch;

    @FXML
    private TableView<Student> allStudents;
    @FXML
    private Button viewStudent;

    Image myImage=new Image(getClass().getResourceAsStream("/images/GSS.jpg"));;

    @FXML
    private ChoiceBox<Integer> mySemComboBox;
    @FXML
    private ChoiceBox<String> myBranchComboBox;

    @FXML
            private Button logout;

    int mysem;
    String mybranch;

    public void onLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-page.fxml"));
        Parent mainView = loader.load();
        Stage stage = (Stage) stdUsn.getScene().getWindow();

        Scene myscene=new Scene(mainView);
        String css = this.getClass().getResource("/application.css").toExternalForm();
        myscene.getStylesheets().add(css);
        stage.setScene(myscene);

        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        //stage.setMaximized(true);


        dataStatus.setText("Loggin Out..");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Integer semesters[]={1,2,3,4,5,6};
        String branches[]={"CSE","ISE","AIML","MECH","CIVIL","EC","EE"};
        mySemComboBox.getItems().addAll(semesters);
        myBranchComboBox.getItems().addAll(branches);
        mySemComboBox.setOnAction(this::getSemester);
        myBranchComboBox.setOnAction(this::getBranch);
        //  collegeImage.getImage(Boolean.parseBoolean("../../../images/GSS.jpg"));

        collegeImg.setImage(myImage);
        gender="Male";
        try {
            myCon = DBConnect.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void upDateData() throws SQLException {

        String usn=stdUsn.getText();
        String name=stdName.getText();
        int sem=mysem;
        String branch=mybranch;
        String gender="Male";
        if(rb1.isSelected())
            gender="Male";
        else if(rb2.isSelected())
            gender="Female";
        String otherActivity="";
        if(nss.isSelected())
            otherActivity+=" NSS ";
        if(ncc.isSelected())
            otherActivity+=" NCC ";
        if(rc.isSelected())
            otherActivity+="Rotary Club";
        PreparedStatement statement=null;
        String sqlStatement="update student1 set usn=?, sname=?, gender=?, sem=?, branch=?, participation=? where usn=?";
        statement=myCon.prepareStatement(sqlStatement);

        statement.setString(1,usn);
        statement.setString(2,name);
        statement.setString(3,gender);
        statement.setInt(4,sem);
        statement.setString(5,branch);
        statement.setString(6,otherActivity);
        statement.setString(7,usn);

        int rowsAffected=statement.executeUpdate();

        dataStatus.setText(rowsAffected+" row is Updated...");

    }

    public void importFromExcel() {

            ExcelToPostgresApp excelToPostgresApp;
            excelToPostgresApp = new ExcelToPostgresApp();
            dataStatus.setText("Data imported Succesfully...");
            Stage primaryStage = new Stage();
            excelToPostgresApp.start(primaryStage);

    }
    public void writeExcel(){
        System.out.println("Write To Excel File...");
        try {
            Statement statement=myCon.createStatement();
            ResultSet rs = statement.executeQuery("select * from student1");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Student Info");
            String excelFilePath = "studentData.xlsx";
            writeHeaderLine(sheet);

            writeDataLines(rs, workbook, sheet);

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            dataStatus.setText("Data Written to Excel Sheet!!!");
        }catch (SQLException e){
            System.out.println(e.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void getGender(ActionEvent event){
        if(rb1.isSelected())
            gender="Male";
        else if(rb2.isSelected())
            gender="Female";
    }



    public void showStudentData(KeyEvent event) throws SQLException {
        KeyCode code=event.getCode();
        String myUsn="";
        if(code==KeyCode.ENTER)
        {
            myUsn=stdUsn.getText();
            Statement statement=myCon.createStatement();
            ResultSet rs=statement.executeQuery("select * from student1 where usn='"+myUsn+"'");

            if(!rs.isBeforeFirst() && rs.getRow() == 0) {
                dataStatus.setText("No such Student..");
            }
            else {
                rs.next();
                dataStatus.setText("Student Found..");
                stdName.setText(rs.getString("sname"));
            }
        }

    }
    public void getNssParticipation(ActionEvent event){
        System.out.println("I am here...");
        if(nss.isSelected())
            participation+=" NSS ";
        System.out.println(participation);
    }
    public  void getNccParticipation(ActionEvent event){
        if(ncc.isSelected())
            participation+=" NCC ";
    }
    public  void getRotaryParticipation(ActionEvent event){
        if(rc.isSelected())
            participation+=" Rotary Club";
    }
    public void getSemester(ActionEvent event){
        mysem=mySemComboBox.getValue();
        System.out.println(mysem);
    }
    public void getBranch(ActionEvent event){
          mybranch=myBranchComboBox.getValue();
          System.out.println(mybranch);
         // welcomeText.setText(mybranch);
    }
    @FXML
    public void deleteStudent() throws SQLException {
        ButtonType foo = new ButtonType("YES",ButtonBar.ButtonData.OK_DONE);
        ButtonType bar = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Do You really want to delete.",
                foo,
                bar);
        try {


            alert.setTitle("Student Info Delete Warning...");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == foo) {
                Statement stmt = myCon.createStatement();
                stmt.execute("delete from student1 where usn='" + stdUsn.getText() + "'");
                dataStatus.setText("One Row Deleted...");
            }
        }catch (SQLException e){
            System.out.println(e.toString());
        }
    }

    @FXML
    protected void viewAllStudents() {
        try {
            myCon = DBConnect.getConnection();
            System.out.println("Connected Succesfully...");
        }catch (SQLException e){
            System.out.println(e.toString());
        }

        allStudents.getItems().clear();
        if(firstTime) {
            TableColumn<Student, String> usnCol = new TableColumn<>("USN");
            usnCol.setCellValueFactory(new PropertyValueFactory<>("usn"));

            TableColumn<Student, String> nameCol = new TableColumn<>("NAME");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("sname"));

            TableColumn<Student, Integer> semCol = new TableColumn<>("SEM");
            semCol.setCellValueFactory(new PropertyValueFactory<>("sem"));

            TableColumn<Student, String> branchCol = new TableColumn<>("BRANCH");
            branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

            TableColumn<Student, String> genderCol = new TableColumn<>("GENDER");
            genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

            TableColumn<Student, String> partCol = new TableColumn<>("PARTICIPATION");
            partCol.setCellValueFactory(new PropertyValueFactory<>("participation"));


            allStudents.getColumns().addAll(usnCol,nameCol,semCol,branchCol,genderCol,partCol);
            firstTime=false;
        }

        ObservableList<Student> allStds=FXCollections.observableArrayList();

        try {
            Statement statement=myCon.createStatement();
            ResultSet rs1=statement.executeQuery("select * from student1");


            while(rs1.next()){
                String usn=rs1.getString("usn");
                String sname=rs1.getString("sname");
                Integer sem=rs1.getInt("sem");
                String branch=rs1.getString("branch");
                String gend=rs1.getString("gender");
                String part=rs1.getString("participation");
                allStds.add(new Student(usn,sname,sem,branch,gend,part));
            }
            allStudents.setItems(allStds);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        welcomeText.setText("Welcome to JavaFX Application!");
        dataStatus.setText("Have a nice Day!!!");
    }


    private void writeDataLines(ResultSet result, XSSFWorkbook workbook,
                                XSSFSheet sheet) throws SQLException {
        int rowCount = 1;

        while (result.next()) {
            String studentUsn = result.getString("usn");
            String studentName = result.getString("sname");
            int studentSem = result.getInt("sem");
            String studentBranch = result.getString("branch");
            String studentGender = result.getString("gender");
            String studentParticipation = result.getString("participation");


            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(studentUsn);

            cell = row.createCell(columnCount++);
            cell.setCellValue(studentName);

            cell = row.createCell(columnCount++);

           /* CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            cell.setCellStyle(cellStyle);*/

            cell.setCellValue(studentSem);

            cell = row.createCell(columnCount++);
            cell.setCellValue(studentBranch);

            cell = row.createCell(columnCount++);
            cell.setCellValue(studentGender);

            cell = row.createCell(columnCount++);
            cell.setCellValue(studentParticipation);
        }
    }

    private void writeHeaderLine(XSSFSheet sheet) {

        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("USN");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Student Name");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Semester");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Bracnh");

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("Gender");
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("Participation");
    }


    @FXML
    protected void insertStudentClick(){

        try {
            myCon = DBConnect.getConnection();
            System.out.println("Connected Succesfully...");
        }catch (SQLException e){
            System.out.println(e.toString());
        }
        String usn=stdUsn.getText();
        String sname=stdName.getText();
        if(usn.equals("") || sname.equals("")) {
            Alert a=new Alert(Alert.AlertType.WARNING);
            EventHandler<ActionEvent> event = new
                    EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {// set alert type
                            a.setAlertType(Alert.AlertType.ERROR);
                            a.setTitle("Inputs Cant be Blank..");
                            // show the dialog
                            a.show();
                        }
                    };
            addStudent.setOnAction(event);
            System.out.println("Data cant be blank");
        }else {
            int Sem = mysem;  //Integer.parseInt(stdSem.getText());
            String Branch = mybranch;     //stdBranch.getText();
            System.out.println(usn + " " + sname + "  " + Sem + "  " + Branch + " " + participation.toString());

            String insertSQL = "INSERT INTO student1(usn, sname, sem, branch,gender,participation) VALUES (?, ?, ?, ?,?,?)";
            try {
                PreparedStatement preparedStatement = myCon.prepareStatement(insertSQL);
                preparedStatement.setString(1, usn); // id
                preparedStatement.setString(2, sname); // name
                preparedStatement.setInt(3, Sem); // age
                preparedStatement.setString(4, Branch); // grade
                preparedStatement.setString(5, gender);
                preparedStatement.setString(6, participation.toString());
                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " rows inserted..");
                dataStatus.setText("One Row Inserted...");
                Thread.sleep(1000);
                dataStatus.setText("One Row Inserted...");
                if (rowsInserted > 0) {
                    System.out.println("A new student was inserted successfully!");
                }

            } catch (SQLException e) {
                System.out.println(e.toString());
                dataStatus.setText(e.toString().substring(e.toString().length()-15,e.toString().length()).toUpperCase());
                //e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {

            }
        }

    }



}