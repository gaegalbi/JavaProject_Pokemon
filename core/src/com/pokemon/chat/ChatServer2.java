package com.pokemon.chat;

import com.badlogic.gdx.utils.Array;
import com.pokemon.game.Pokemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer2 {
    ServerSocket serverSocket;
    Socket socket;
    Array<SocketThread> array;
    Person person = new Person();
    public ChatServer2() {
        array = new Array<SocketThread>(100);
        newServer();
    }

    public void newServer() {

      /*  if (serverSocket != null) {
            return;
        }*/

        Runnable recieved = new Runnable() {
            public void run() {
                while (true) {
                    try {
                        serverSocket = new ServerSocket(9021);
                    } catch (IOException e) {
                        System.out.println("해당포트가 열려있습니다");
                    }
                    try {
                        System.out.println("서버 오픈!!");
                        socket = serverSocket.accept();
                        System.out.println("접속 완료");
                        addSocket(socket);
                    } catch (IOException e) {
                        System.out.println("머야");

                    }
                }
            }
        };
        new Thread(recieved).start();
    }


    private synchronized void addSocket(Socket socket) {
        SocketThread tmpThread = new SocketThread(socket);
        tmpThread.start();
        array.add(tmpThread);
    }

    public class SocketThread extends Thread {
        Socket socket;
        BufferedReader in;
        OutputStream out;

        public SocketThread(Socket sock) {
            socket = sock;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                out = socket.getOutputStream();

                while (true) {
                    System.out.println("전 받음");
                    String tmpstr = in.readLine();
                    System.out.println("받음");
                    person.setMessage(tmpstr);
                    if (tmpstr == null || tmpstr == "\n" ) {
                        System.out.println("no comment");
                    }
                    acceptMessage(tmpstr);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        public void toWrite(String str) {
            try {
                out.write(new String(str+"\n").getBytes());
            } catch (IOException ex) {

            }
        }
    }

    synchronized void acceptMessage(String str) {
        //Person person = new Json().fromJson(Person.class, str);
        //Person person = new Person();
        String send;

        if (person.getMessage().equals("")) {
            send ="came";
        } else {
            send = person.getMessage();
        }
        writeToAllUsers(send);
        System.out.println("보냄2");
    }

    void writeToAllUsers(String str) {
        for (SocketThread st : array) {
            st.toWrite(str);
        }
    }
}





