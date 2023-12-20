package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    Scanner in;
    PrintStream out;
    ChatServer server;
    String name;

    public Client(Socket socket, ChatServer server) {

        this.socket = socket;
        this.server = server;
        // запускаем поток
        new Thread(this).start();
    }

    void receive(String msg) {
        out.println(msg);
    }


    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to Chat!");
            out.println("Enter your name:");
            name = in.nextLine();
            out.println("Hello, " + name + "! Chat started!");
            String input = in.nextLine();
            while (!input.equals("bye")) {
                Date dateNow = new Date();
                SimpleDateFormat date = new SimpleDateFormat("yy.MM.dd ',' hh:mm:ss");
                server.sendAll(name + " ["+ date.format(dateNow) + "] " + ": "+ input);
                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
