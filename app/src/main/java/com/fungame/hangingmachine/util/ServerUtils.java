package com.fungame.hangingmachine.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by tom on 2015/11/19.
 * descripton :
 */
public class ServerUtils {

    private static SocketCallBack callBack;
    private boolean hasReceive = false;

    public interface SocketCallBack{
        public void getCallBack(String back);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 100){
                SocketCallBack callBack = (SocketCallBack) msg.obj;
                String result = (String) msg.obj;
                if (callBack != null) {
                    callBack.getCallBack(result);
                }
            }
        }
    };

    public byte[] get16Byte(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baKeyword;
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(byte[] baKeyword)
    {
//        byte[] baKeyword = new byte[s.length()/2];
        String s = "";
//        for(int i = 0; i < baKeyword.length; i++)
//        {
//            try
//            {
//                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
        try
        {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return s;
    }

    public void sendCommand(final String str, final SocketCallBack cb) {

            callBack = cb;
//            new Thread(){
//                @Override
//                public void run() {
//                        try {
//                            //建立Socket
////                            Socket s = new Socket("58.221.58.190", 347);
////                            Socket s = new Socket("192.168.1.24", 1000);
//                            int TIME_OUT = 30000;
//                            SocketChannel client = SocketChannel.open();
////                            InetSocketAddress isa = new InetSocketAddress("58.221.58.190", 347);
//
//                            Socket socket = new Socket();
//                            SocketAddress address = new InetSocketAddress("58.221.58.190", 347);
//                            socket.connect(address, TIME_OUT);
//                            socket.setSoTimeout(TIME_OUT);
//                            BufferedReader in = new BufferedReader(new InputStreamReader(
//                                    socket.getInputStream()));
//
//                            PrintWriter out = new PrintWriter(new BufferedWriter(
//                                    new OutputStreamWriter(socket.getOutputStream())), true);
//
//                            System.out.println("ready to write:" + str);
//                            out.println(str.getBytes("UTF-8"));
////                            out.write(str);
//                            out.flush();
//                            while (true) {
//                                try {
//                                    if (!socket.isClosed() && socket.isConnected()
//                                            && !socket.isInputShutdown()) {
////                                        byte[] lenBuffer = new byte[1024];
////                                        int len = 0;
//                                        try {
//                                            String tmp = in.readLine();
//                                            System.out.println("read line lenth:" + tmp);
//                                        } catch (Exception e) {
//                                            System.out.println("1111 SocketSvr socket read timeout");
//                                        }
//                                    }
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//
////                            brNet.close();
////                            s.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                }
//            }.start();


        onCreate(str, callBack);



//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                Socket sock = null;
//                try {
//                    sock = new Socket("58.221.58.99", 347);
//                    InputStream inputStream = sock.getInputStream();
//                    byte[] inBuffer = new byte[1024];
//                    // 登陆获取公告
//                    StringBuilder builder = new StringBuilder();
//                    while (true) {
//                        int reads = inputStream.read(inBuffer);
//                        String tmp = new String(inBuffer);
//                        builder.append(tmp);
//                        if(reads  > 0){
//                            Message msg = handler.obtainMessage();
//                            msg.obj = builder.toString();
//                            msg.what = 100;
//                            Log.i("--tom", "get result:" + builder.toString());
//                            handler.sendMessage(msg);
//                            break;
//                        }
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//            }
//        }.start();
    }

    InputStream reader;
    OutputStream writer;
    Socket socket;


    public void onCreate(final String command, final SocketCallBack callBack) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    //初始化socket
                    if(socket == null){
                        socket = new Socket("58.221.58.190", 347);
                    }
//                    writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    reader = socket.getInputStream();
                    writer = socket.getOutputStream();
                    System.out.println("成功连接服务器！");
                    dataSend(command);
                    if(!hasReceive){
                        dataReceived();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //接收到数据的处理方法
    private void dataReceived(){
        final AsyncTask<Void,String,Void> read=new AsyncTask<Void, String, Void>() {
            protected Void doInBackground(Void... params) {
                String line;
                try {
                    hasReceive = true;
                    byte[]  buffer = new byte[1024];
                    while ((reader.read(buffer)) != 0){
                        String readLine = new String(buffer, "GBK");
                        publishProgress(readLine);
                        socket.close();
                        socket = null;

                        System.out.println("22222222222");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("444444444");
                return null;
            }
            protected void onProgressUpdate(String... values) {
                //回调到主线程更新对方位置
                super.onProgressUpdate(values);
                if(callBack != null){
                    callBack.getCallBack(values[0]);
                }
                System.out.println("read line:" + values[0]);
            }
        };
        read.execute();
    }
    //数据发送方法
    private void dataSend(String command){
        //将数据发送到服务器
        try {
            if (writer!=null) {
                writer.write(command.getBytes("GBK"));
                writer.flush();
                System.out.println("1111发送的数据：" + command);
            }
            System.out.println("发送的数据："+command);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
