package com.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientDemo implements Runnable{
    private Socket socket;
    BufferedReader reader;
    private PrintWriter writer;
    public SocketClientDemo(){
        try{
            //127.0.0.1表示本机IP，10000为服务器Socket设置的端口
            socket = new Socket("127.0.0.1", 10000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GB2312"));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("working.............");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void run(){
        try{
            //这里就可以读取所有行String
            String line, buffer="";
            while(!((line = reader.readLine())==null))
                buffer+=line;
            System.out.println(buffer);
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("problem");
        }finally{
            //最后关闭Socket
            try{
                if(socket!=null)socket.close();
                if(reader!=null)reader.close();
                if(writer!=null)writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }

    }
    public static void main(String[] args){
        new Thread(new SocketClientDemo()).start();


    }

}
