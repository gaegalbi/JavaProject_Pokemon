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
                    DbCon.DBURL,
                    DbCon.DBID, DbCon.DBPASS);
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
    public static void insert(String sql){
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
    public static boolean signUp(String sql){
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int count=0;
            while(rs.next()) {
                count = rs.getInt("count(USERID)");
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

    public static boolean login(String sql,String password) {
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            String checkPas = null;
            while (rs.next()) {
                checkPas = rs.getString("USERPAS");
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
