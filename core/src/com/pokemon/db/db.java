package com.pokemon.db;

import java.sql.*;

public class db{
    public static Connection con = null;
    public static ResultSet rs = null;
    public static Statement stmt = null;
    public static void DBC(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection(
                    "jdbc:mysql://192.168.31.1:3306/mydb?serverTimezone=UTC",
                    "pro", "1234");
            System.out.println("DB접속 Success");
        }
        catch(SQLException e){
            System.out.println("SQLException" + e);
            e.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }
}
