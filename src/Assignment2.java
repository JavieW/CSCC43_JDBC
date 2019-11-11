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
        try {
            String selectQuery = "SELECT pid FROM Player WHERE pid = ?";
            String insertQuery = "INSERT INTO Player VALUES (?, ?, ?, ?)";
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
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getChampions(int pid) {
        try{
            String query = "SELECT count(*) AS numChams FROM Player p, Champion c WHERE p.pid = c.pid AND c.pid = ?";
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
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCourtInfo(int courtid){
        try{
            String query = "SELECT courtid,courtname,capacity,tname FROM Court c,tournament t " +
                    "WHERE c.tid = t.tid AND courtid = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1,courtid);
            rs = ps.executeQuery();
            if (rs.next()) {
                // "courtid:courtname:capacity:tname"
                return rs.getInt(1) + ":" + rs.getString(2) + ":" +
                        rs.getInt(3) + ":" + rs.getString(4);
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean chgRecord(int pid, int year, int wins, int losses){
        try {
            String query = "UPDATE Record SET wins = ?, losses = ? WHERE pid = ? AND year = ?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, wins);
            ps.setInt(2, losses);
            ps.setInt(3, pid);
            ps.setInt(4, year);
            return (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteMatchBetween(int p1id, int p2id) {
        try {
            String query = "DELETE FROM event WHERE winid = ? AND lossid = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, p1id);
            ps.setInt(2, p2id);
            int firstDelete = ps.executeUpdate();

            ps = connection.prepareStatement(query);
            ps.setInt(1, p2id);
            ps.setInt(2, p1id);
            int secondDelete = ps.executeUpdate();

            return (firstDelete + secondDelete) >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String listPlayerRanking(){
        try {
            sql = connection.createStatement();
            rs = sql.executeQuery("SELECT pname,globalrank FROM player ORDER BY globalrank");
            String result = "";
            while (rs.next()) {
                result += rs.getString(1) +":"+ rs.getInt(2)+"\n";
            }
            return result.trim();
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                rs.close();
                sql.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int findTriCircle(){
        try {
            sql = connection.createStatement();
            rs = sql.executeQuery("SELECT e1.winid, e2.winid, e3.winid FROM event e1, event e2, event e3 " +
                    "WHERE e1.winid < e2.winid AND e2.winid < e3.winid AND e1.lossid = e2.winid " +
                    "AND e2.lossid = e3.winid and e3.lossid = e1.winid");
            int result = 0;
            while (rs.next()) {
                result += 1;
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                rs.close();
                sql.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateDB(){
        try {
            // create championPlayers table
            sql = connection.createStatement();
            sql.executeUpdate("DROP TABLE IF EXISTS championPlayers CASCADE;");
            sql.executeUpdate("CREATE TABLE championPlayers(pid INTEGER, pname VARCHAR, nchampions INTEGER);");

            // select data
            rs = sql.executeQuery("select p.pid, pname, count(*) as nchampions from player p, champion c " +
                    "where p.pid = c.pid group by p.pid order by p.pid ASC;");

            // insertion
            ps = connection.prepareStatement("INSERT INTO championPlayers VALUES (?, ?, ?)");
            while (rs.next()) {
                ps.setInt(1, rs.getInt(1));
                ps.setString(2, rs.getString(2));
                ps.setInt(3, rs.getInt(3));
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                sql.close();
                ps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

//        System.out.println("test deleteMatchBetween: ");
//        System.out.println(a2.deleteMatchBetween(1, 3));

//        System.out.println("test listPlayerRanking: ");
//        System.out.println(a2.listPlayerRanking());

//        System.out.println("test findTriCircle: ");
//        System.out.println(a2.findTriCircle());

        System.out.println("test updateDB: ");
        System.out.println(a2.updateDB());
    }
}
