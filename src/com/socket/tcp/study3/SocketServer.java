package com.socket.tcp.study3;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2018/4/23.
 *  通过指定长度

 　　如果你了解一点class文件的结构（后续会写，敬请期待），那么你就会佩服这么设计方式，也就是说我们可以在此找灵感，就是我们可以先指定后续命令的长度，然后读取指定长度的内容做为客户端发送的消息。

 　　现在首要的问题就是用几个字节指定长度呢，我们可以算一算：

 1个字节：最大256，表示256B
 2个字节：最大65536，表示64K
 3个字节：最大16777216，表示16M
 4个字节：最大4294967296，表示4G
 依次类推
 　　这个时候是不是很纠结，最大的当然是最保险的，但是真的有必要选择最大的吗，其实如果你稍微了解一点UTF-8的编码方式（字符编码后续会写，敬请期待），那么你就应该能想到为什么一定要固定表示长度字节的长度呢，我们可以使用变长方式来表示长度的表示，比如：

 第一个字节首位为0：即0XXXXXXX，表示长度就一个字节，最大128，表示128B
 第一个字节首位为110，那么附带后面一个字节表示长度：即110XXXXX 10XXXXXX，最大2048，表示2K
 第一个字节首位为1110，那么附带后面二个字节表示长度：即110XXXXX 10XXXXXX 10XXXXXX，最大131072，表示128K
 依次类推
 　　上面提到的这种用法适合高富帅的程序员使用，一般呢，如果用作命名发送，两个字节就够了，如果还不放心4个字节基本就能满足你的所有要求，下面的例子我们将采用2个字节表示长度，目的只是给你一种思路，让你知道有这种方式来获取消息的结尾：

 　　服务端程序：
 */

public class SocketServer {
    public static void main(String[] args) throws Exception {
        // 监听指定的端口
        int port = 55533;
        ServerSocket server = new ServerSocket(port);

        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");
        Socket socket = server.accept();
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes;
        // 因为可以复用Socket且能判断长度，所以可以一个Socket用到底
        while (true) {
            // 首先读取两个字节表示的长度
            int first = inputStream.read();
            //如果读取的值为-1 说明到了流的末尾，Socket已经被关闭了，此时将不能再去读取
            if(first==-1){
                break;
            }
            int second = inputStream.read();
            int length = (first << 8) + second;
            // 然后构造一个指定长的byte数组
            bytes = new byte[length];
            // 然后读取指定长度的消息即可
            inputStream.read(bytes);
            System.out.println("get message from client: " + new String(bytes, "UTF-8"));
        }
        inputStream.close();
        socket.close();
        server.close();
    }
}
