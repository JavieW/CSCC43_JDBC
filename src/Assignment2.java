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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Closes the connection. Returns true if closure was sucessful
    public boolean disconnectDB(){
        try{
            connection.close();
            return true;
        } catch (SQLException e) {
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
            return (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getChampions(int pid) {
        String query = "SELECT count(*) AS numChams FROM Player p, Champion c WHERE p.pid = c.pid AND c.pid = ?";
        try{
            ps = connection.prepareStatement(query);
            ps.setInt(1,pid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCourtInfo(int courtid){
        String query = "SELECT courtid,courtname,capacity,tname FROM Court c,tournament t WHERE c.tid = t.tid AND courtid = ?";
        try{
            ps = connection.prepareStatement(query);
            ps.setInt(1,courtid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) +":"+ rs.getString(2) +":"+ rs.getInt(3) +":"+ rs.getString(4);
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean chgRecord(int pid, int year, int wins, int losses){
        String query = "UPDATE Record SET wins = ?, losses = ? WHERE pid = ? AND year = ?;";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, wins);
            ps.setInt(2, losses);
            ps.setInt(3, pid);
            ps.setInt(4, year);
            return (ps.executeUpdate() == 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMatchBetween(int p1id, int p2id) {
        String query = "DELETE FROM event WHERE winid = ? AND lossid = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, p1id);
            ps.setInt(2, p2id);
            int firstDelte = ps.executeUpdate();
            ps = connection.prepareStatement(query);
            ps.setInt(1, p2id);
            ps.setInt(2, p1id);
            int secondDelte = ps.executeUpdate();
            return (firstDelte + secondDelte) >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String listPlayerRanking(){
        String query = "SELECT pname,globalrank FROM player ORDER BY globalrank";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            String result = "";
            while (rs.next()) {
                result += rs.getString(1) +":"+ rs.getInt(2)+"\n";
            }
            return result.trim();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        String URL = "jdbc:postgresql://localhost:5432/javiewang";
        String username = "javiewang";
        String password = "";
        System.out.println("test connection: ");
        System.out.println(a2.connectDB(URL, username, password));

//        System.out.println("test disconnection: ");
//        System.out.println(a2.disconnectDB());

//        System.out.println("test insert player: ");
//        System.out.println(a2.insertPlayer(1,"javie", 1, 1));

//        System.out.println("test getChampions: ");
//        System.out.println(a2.getChampions(2));

//        System.out.println("test getCourtInfo: ");
//        System.out.println(a2.getCourtInfo(2));

//        System.out.println("test chgRecord: ");
//        System.out.println(a2.chgRecord(1, 2012, 2,2));
//
//        System.out.println("test deleteMatchBetween: ");
//        System.out.println(a2.deleteMatchBetween(1, 3));

//        System.out.println("test listPlayerRanking: ");
//        System.out.println(a2.listPlayerRanking());


    }
}
