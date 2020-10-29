package project.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String databaseName = "U0666B";
    private static final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/" + databaseName;
    private static final String username = "U0666B";
    private static final String password = "53688689036";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection connection; //sat

    //Get a connection to SQL Database
    public static Connection makeConnection() throws ClassNotFoundException, SQLException, Exception{
        System.out.println('8');
        connection = DriverManager.getConnection(DB_URL, username, password);
        System.out.println('a');
        return connection;
    }

    //Closes the current connection
    public static void closeConnection() throws SQLException{
        connection.close();
    }
}
