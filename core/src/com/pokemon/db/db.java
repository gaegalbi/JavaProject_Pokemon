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
    public static String sP(String id,int num) {
        String sql = "SELECT PM_NAME from PM_INFO WHERE PM_ID = (SELECT PM_ID FROM PM where U_ID = '"+id+"' AND PM_BATTLE ='" + num +"');";
        String PM_NAME = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PM_NAME = rs.getString("PM_NAME");
            }
            return PM_NAME;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return PM_NAME;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return PM_NAME;
        }
    }
    public static String sP(String id) {
        String sql = "SELECT PM_NAME from PM_INFO WHERE PM_ID = (SELECT PM_ID FROM PM_INFO where PM_ID = '"+id+"');";
        String PM_NAME = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PM_NAME = rs.getString("PM_NAME");
            }
            return PM_NAME;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return PM_NAME;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return PM_NAME;
        }
    }

    public static String GET_PM_SK_NAME(String id) {
        String sql = "SELECT PM_SK_NAME from PM_SK_INFO WHERE PM_SK_ID = '"+id+"';";
        String PM_SK_NAME = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PM_SK_NAME = rs.getString("PM_SK_NAME");
            }
            return PM_SK_NAME;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return PM_SK_NAME;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return PM_SK_NAME;
        }
    }

    public static int GET_PM_SK_CNT(String id) {
        String sql = "SELECT PM_SK_CNT from PM_SK_INFO WHERE PM_SK_ID = '"+id+"';";
        int PM_SK_CNT = 0;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PM_SK_CNT = rs.getInt("PM_SK_CNT");
            }
            return PM_SK_CNT;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return PM_SK_CNT;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return PM_SK_CNT;
        }
    }

    public static double GET_PM_DA(String id) {
        String sql = "SELECT PM_SK_DA from PM_SK_INFO WHERE PM_SK_ID = '"+id+"';";
        double PM_DA= 0;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PM_DA = rs.getDouble("PM_SK_DA");
            }
            return PM_DA;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return PM_DA;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return PM_DA;
        }
    }
    public static String GET_PMType(String id) {
        String sql = "SELECT PM_TYPE from PM_INFO WHERE PM_ID = '"+id+"';";
        String PM_TYPE = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PM_TYPE = rs.getString("PM_TYPE");
            }
            return PM_TYPE;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return PM_TYPE;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return PM_TYPE;
        }
    }

    // 랭크 점수에 대한 ID
    public static String rank_GET_U_ID(int num) {
        String sql = "SELECT * FROM (SELECT *, rank() over(order by U_RANK DESC) AS RANKING FROM user) AS R WHERE R.RANKING = '"+num+"';";
        String U_ID = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                U_ID = rs.getString("U_ID");
            }
            return U_ID;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return U_ID;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return U_ID;
        }
    }

    // ID에 대한 랭크 점수
    public static int rank_GET_U_RANK(String ID) {
        String sql = "SELECT * FROM (SELECT *, rank() over(order by U_RANK DESC) AS RANKING FROM user) AS R WHERE R.U_ID = '"+ID+"';";
        int U_RANK = 0;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                U_RANK = rs.getInt("U_RANK");
            }
            return U_RANK;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return U_RANK;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return U_RANK;
        }
    }

    // ID에 대한 랭킹
    public static int rank_GET_RANKINKG(String ID) {
        String sql = "SELECT * FROM (SELECT *, rank() over(order by U_RANK DESC) AS RANKING FROM user) AS R WHERE R.U_ID = '"+ID+"';";
        int RANKING = 0;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                RANKING = rs.getInt("RANKING");
            }
            return RANKING;
        } catch (SQLException e) {
            System.out.println("불러오는 SQL문이 틀렸습니다.");
            System.out.print("이유 : " + e);
            e.printStackTrace();
            return RANKING;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return RANKING;
        }
    }
}
