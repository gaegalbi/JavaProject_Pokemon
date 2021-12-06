package com.pokemon.multibattle;

import com.pokemon.game.Pokemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BattleClient extends Thread{
    char me,other;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Pokemon game;
    int a;
    String sendMessage;
    String recieveMessage;
    int b=0;
    public BattleClient(final Pokemon game) {
        try {
            socket = new Socket("localhost", 9032);

            this.game = game;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        }catch (IOException e){
            e.printStackTrace();
        }
        Runnable recieved = new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        String i = in.readLine();
                        System.out.println(i);
                        game.setrecieveMessage(i);
                        game.setI(1);
                        if (i.equals("2")) {
                            a = 50;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        new Thread(recieved).start();

        Runnable send = new Runnable() {
            @Override
            public  void run() {
                getSendMessage();
            }

        };
        new Thread(send).start();
    }

    public int getA() {
        return a;
    }

    public synchronized void setSendMessage(String sendMessage) {
        System.out.println("b==0");
        this.sendMessage = sendMessage;
        game.setSendMessage(sendMessage);
        notify();
    }
    public synchronized void getSendMessage(){
        while (true) {
            try {
                System.out.println("아니");
                wait();
                System.out.println("와우");
                if(sendMessage !=null){
                    out.println(sendMessage);
                    game.setJ(1);
                    System.out.println("와우");
                    System.out.println("ㅎ"+sendMessage+"ㅎ");
                    sendMessage = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
