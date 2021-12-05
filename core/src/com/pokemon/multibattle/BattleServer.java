package com.pokemon.multibattle;

import com.pokemon.chat.ChatServer2;
import com.pokemon.game.Pokemon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BattleServer {
    Socket socket;
    ServerSocket serverSocket;

    public BattleServer(){
        Runnable recieved2 = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        serverSocket = new ServerSocket(9021);

                    } catch (IOException e) {
                        System.out.println("해당포트가 열려있습니다");
                    }
                    try {
                        System.out.println("서버 오픈!!");
                        User user1 = new User(serverSocket.accept(), 1);
                        User user2 = new User(serverSocket.accept(), 2);

                        user1.setOther(user2);
                        user2.setOther(user1);

                        user1.start();
                        user2.start();
                        System.out.println("접속 완료");

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
           }
        }; new Thread(recieved2).start();

    }

    public static void main(String[] args) {
        new BattleServer();
        new ChatServer2();
    }
}

