package jforkts.in.myjavafx;
import java.sql.*;

public class DBConnect {

    private static final String URL = "jdbc:postgresql://localhost:5432/KLSGit";
    private static final String USER = "postgres";
    private static final String PASSWORD = "nitesh";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET search_path TO public");
        }
        return conn;
    }

}
