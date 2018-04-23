package com.socket.tcp.study3;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2018/4/23.
 * 客户端要多做的是，在发送消息之前先把消息的长度发送过去。

 　　这种事先约定好长度的做法解决了之前提到的种种问题，Redis的Java客户端Jedis就是用这种方式实现的这种方式的缺点：

 暂时还没发现
 　　当然如果是需要服务器返回结果，那么也依然使用这种方式，服务端也是先发送结果的长度，然后客户端进行读取。当然现在流行的就是，长度+类型+数据模式的传输方式。
 */

public class SocketClient {
    public static void main(String args[]) throws Exception {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message = "你好  yiwangzhibujian";
        //首先需要计算得知消息的长度
        byte[] sendBytes = message.getBytes("UTF-8");
        //然后将消息的长度优先发送出去
        int ss = sendBytes.length >>8;
        outputStream.write(sendBytes.length >>8);
        outputStream.write(sendBytes.length);
        //然后将消息再次发送出去
        outputStream.write(sendBytes);
        outputStream.flush();
        //==========此处重复发送一次，实际项目中为多个命名，此处只为展示用法
        message = "第二条消息";
        sendBytes = message.getBytes("UTF-8");
        outputStream.write(sendBytes.length >>8);
        outputStream.write(sendBytes.length);
        outputStream.write(sendBytes);
        outputStream.flush();
        //==========此处重复发送一次，实际项目中为多个命名，此处只为展示用法
        message = "the third message!";
        sendBytes = message.getBytes("UTF-8");
        outputStream.write(sendBytes.length >>8);
        outputStream.write(sendBytes.length);
        outputStream.write(sendBytes);

        outputStream.close();
        socket.close();
    }
}
