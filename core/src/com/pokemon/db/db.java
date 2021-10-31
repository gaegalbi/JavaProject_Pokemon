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
                    "jdbc:mysql://221.164.163.17:3306/mydb?serverTimezone=UTC",
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
    public static void insert(String id, String pas){
        //, int lv, int exp, int rank
        //String sql = ("INSERT INTO user VALUES('" +id + "','" +pas + "','"+ lv + "','"+ exp + "','" +rank+"');");
        String sql = ("INSERT INTO user(U_ID,U_PAS) VALUES('" +id + "','" +pas +"');");
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("DB 삽입 Success");
        }
        catch(SQLException e){
            System.out.println("삽입SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }
    public static boolean signUp(String id){
        String sql = "Select COUNT(U_ID) from user where U_ID = '"+id+"';";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int count=0;
            while(rs.next()) {
                count = rs.getInt("count(U_ID)");
            }
            if(count>=1){
                return true;
            }else {
                return false;
            }
        }
        catch(SQLException e){
            System.out.println("회원가입 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return true;
        }
        catch(Exception e){
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return true;
        }
    }

    public static boolean login(String id,String password) {
        String sql = "Select U_PAS from user where U_ID = '"+id+"';";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            String checkPas = null;
            while (rs.next()) {
                checkPas = rs.getString("U_PAS");
            }
            if (password.equals(checkPas)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("로그인SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return true;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return true;
        }
    }
}
