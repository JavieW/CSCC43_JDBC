import com.sun.org.apache.xpath.internal.functions.FuncFalse;

import java.sql.*;

public class Assignment2 {

    // A connection to the database
    Connection connection;

    // Statement to run queries
    Statement sql;

    // Prepared Statement
    PreparedStatement ps;

    // Resultset for the query
    ResultSet rs;

    //CONSTRUCTOR
    Assignment2(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Failed to find the JDBC driver");
        }
    }

    //Using the input parameters, establish a connection to be used for this session. Returns true if connection is sucessful
    public boolean connectDB(String URL, String username, String password){
        try{
            connection = DriverManager.getConnection(URL, username, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Closes the connection. Returns true if closure was sucessful
    public boolean disconnectDB(){
        try{
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPlayer(int pid, String pname, int globalRank, int cid) {
        String selectQuery = "SELECT pid FROM Player WHERE pid = ?";
        String insertQuery = "INSERT INTO Player VALUES (?, ?, ?, ?)";
        try {
            // check the existence
            ps = connection.prepareStatement(selectQuery);
            ps.setInt(1,pid);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("pid exist!");
                return false;
            }

            // do insertion
            ps = connection.prepareStatement(insertQuery);
            ps.setInt(1,pid);
            ps.setString(2, pname);
            ps.setInt(3, globalRank);
            ps.setInt(4, cid);
            if (ps.executeUpdate() == 1) {
                return true;
            }
            else {
                System.out.println("Should have exactly one row be updated");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getChampions(int pid) {
        return 0;
    }

    public String getCourtInfo(int courtid){
        return "";
    }

    public boolean chgRecord(int pid, int year, int wins, int losses){
        return false;
    }

    public boolean deleteMatcBetween(int p1id, int p2id){
        return false;
    }

    public String listPlayerRanking(){
        return "";
    }

    public int findTriCircle(){
        return 0;
    }

    public boolean updateDB(){
        return false;
    }

    public static void main(String args[]){
        Assignment2 a2 = new Assignment2();
        String URL = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "";
        System.out.print("test connection, expected: true, got: ");
        System.out.println(a2.connectDB(URL, username, password));

//        System.out.print("test disconnection, expected: true, got: ");
//        System.out.println(a2.disconnectDB());

        System.out.print("test insert player, expected: true, got: ");
        a2.insertPlayer(1,"javie", 1, 1);
        System.out.println(a2.insertPlayer(1,"javie", 1, 1));

    }
}
