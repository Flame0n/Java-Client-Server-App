package model;

import model.entity.Agent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

import static java.lang.System.out;

public class DatabaseManager {
    private final static String url = "jdbc:mysql://localhost:3306/courseworkschema";
    private final static String user = "root";
    private final static String password = "root";
    private final static String query = "INSERT INTO agents (session, id, group_color, x, y) VALUES (?, ?, ?, ?, ?)";

    private Connection connection;
    private PreparedStatement statement;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            out.println("Class not found: " + ex);
        }
    }

    public void WriteToDatabaseAsync(ArrayList<Agent> agents) {
        new Thread(() -> {
            try {
                connection = DriverManager.getConnection(url, user, password);
                statement = connection.prepareStatement(query);
                String sessionHash = MD5(new Timestamp(System.currentTimeMillis()).toString());
                for(Agent a : agents) {
                    statement.setString(1, sessionHash);
                    statement.setInt(2, a.getId());
                    statement.setString(3, a.getColor().toString());
                    statement.setDouble(4, a.getPosition().x);
                    statement.setDouble(5, a.getPosition().y);
                    statement.execute();
                }
                out.println("Sql Fine");
            } catch (SQLException ex) {
                out.println("SQL Error: " + ex);
            } finally {
                try { connection.close(); } catch(Exception ex) { /*can't do anything */ }
                try { statement.close(); } catch(Exception ex) { /*can't do anything */ }
            }
        }).start();
    }

    private String MD5(String md5) {
        try {
            byte[] array = MessageDigest.getInstance("MD5").digest(md5.getBytes());
            StringBuffer builder = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
                builder.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            out.println("Hash error: " + ex);
        }
        return null;
    }
}
