package com.pokemon.multibattle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User extends Thread{
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    User other;
    int start;

    public User(Socket socket,int start) {
        this.socket = socket;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.start = start;
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void setOther(User other){
        this.other = other;
    }
    public void run(){
        try {
            if (start == 2) {
                out.println("2");
                other.out.println("2");
            }
            while (true) {
                System.out.println("제발");
                String name = in.readLine();
                if(name == null)
                    continue;
                System.out.println(name);
                System.out.println("보내짐");
                other.out.println(name);
            }
        }catch (IOException e){
            System.out.println("오류");
        }
    }
}
