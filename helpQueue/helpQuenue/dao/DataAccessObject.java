package helpQuenue.dao;


import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DataAccessObject {
    private Connection daoConn = null;			// JDBC connection
    private ResultSet  daoRset = null;			// result set for queries
    private int returnValue;					// return value for all other commands




    // --- connect() - connect to the Oracle database
    public void connect () {
        // --- set the username and password
        String user = "LIUJ0331";					// NOTE: you must set your UWEC Oracle username here
        String pass = "3OEUIK6P"; 					// NOTE: you must set your UWEC Oracle pw here

        // --- 1) get the Class object for the driver
        try {
            Class.forName ("oracle.jdbc.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            System.err.println ("Could not get class object for Driver");
        }

        // --- 2) connect to database
        try {
            daoConn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@alfred.cs.uwec.edu:1521:csdev",user,pass);
        }
        catch (SQLException sqle) {
            System.err.println ("DAO, connect() - Could not make connection to database");
            System.err.println(sqle.getMessage());
        }
    }	// end - method connect

    // --- executeSQLQuery() - execute an SQL query
    public ResultSet executeSQLQuery (String sqlQuery) throws SQLException {
            // --- 3a) execute SQL query
            Statement stmt = null;		// SQL statement object
            daoRset = null;				// initialize result set

            stmt = daoConn.createStatement();
            daoRset = stmt.executeQuery(sqlQuery);
            return daoRset;

    }	// end - method executeSQLQuery

    // --- executeSQLNonQuery() - execute an SQL command that is not a query
    public void executeSQLNonQuery (String sqlCommand) throws SQLException {
            // --- 3b) execute SQL non-query command
            Statement stmt = null;		// SQL statement object


            stmt = daoConn.createStatement();
            stmt.executeUpdate(sqlCommand);

    }	// end - method executeSQLNonQuery

    public boolean executeSQL(String sqlCommand) throws SQLException{
        boolean result;
        Statement stmt = null;
        stmt = daoConn.createStatement();
        result = stmt.execute(sqlCommand);

        return result;
    }

//    public String processResultSet () {
//        // --- 4) process result set, only applicable if executing an SQL SELECT statement
//        ResultSetMetaData rsmd = null;		// result set metadata object
//        int columnCount = -1;				// column count
//        String resultString = "";			// result string
//
//        try {
//            rsmd = daoRset.getMetaData();
//
//            // get number of columns from result set metadata
//            columnCount = rsmd.getColumnCount();
//
//            // row processing of result set
//            while (daoRset.next()) {
//                for (int index = 1; index <= columnCount; index++) {
//                    resultString += daoRset.getString(index) + "  ";
//                }
//                resultString += "\n";
//            }
//        }
//        catch (SQLException sqle) {
//            System.err.println("Error in processing result set");
//            System.err.println(sqle.getMessage());
//        }
//        catch (NullPointerException npe) {
//            System.err.println("DAO, processResultSet() - no result set generated");
//            System.err.println(npe.getMessage());
//        }
//        return resultString;
//    }	// end - method processResultSet

    // --- setAutoCommit(flag) - set autocommit on or off based on flag
    public void setAutoCommit (boolean flag) {
        try {
            daoConn.setAutoCommit(flag);
        }
        catch (SQLException sqle) {
            System.err.println("DAO, setAutoCommit() - error in setting");
            System.err.println(sqle.getMessage());
        }
    }	// end - method setAutoCommit()

    // --- commit() - commit current transaction on connection
    public void commit () {
        try {
            daoConn.commit();
        }
        catch (SQLException sqle) {
            System.err.println("DAO, commit() - error in commit");
            System.err.println(sqle.getMessage());
        }
    }	// end - method commit()

    // --- rollback() - rollback current transactino on connection
    public void rollback () {
        try {
            daoConn.rollback();
        }
        catch (SQLException sqle) {
            System.err.println("DAO, rollback() - error in rollback");
            System.err.println(sqle.getMessage());
        }
    }	// end - method rollback()

    // --- disconnect() - disconnect from the Oracle database
    public void disconnect () {
        // --- 5) disconnect from database
        try {
            if (daoConn != null) {
                daoConn.close();
            }
            if (daoRset != null) {
                daoRset = null;
            }
        }
        catch (SQLException sqle) {
            System.err.println ("Error in closing database connection");
            System.err.println(sqle.getMessage());
        }
        finally {
            if (daoConn != null) {
                try {
                    daoConn.close();
                }
                catch (SQLException sqlerb) {
                    daoConn = null;
                }
            }
            if (daoRset != null) {
                try {
                    daoRset = null;
                }
                catch (Exception e) {
                    daoRset = null;
                }
            }
        }
    }	// end - method disconnect



}	// end - class DataAccessObject
