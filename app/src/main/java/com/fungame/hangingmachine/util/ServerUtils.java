package com.fungame.hangingmachine.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by tom on 2015/11/19.
 * descripton :
 */
public class ServerUtils {

    private static final int EXIST = 20;
    private boolean hasReceive = false;
    private static SocketCallBack callBack;
    private static final int CALL_BACK = 100;
    public static final String IP_ADDRESS = "58.221.57.99";
    public static final int PORT = 347;

    public interface SocketCallBack{
        public void getCallBack(String back);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == CALL_BACK){
                String result = (String) msg.obj;
                if (callBack != null) {
                    callBack.getCallBack(result);
                }
            } else if(msg.what == EXIST){
                if (callBack != null) {
                    callBack.getCallBack("exit");
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
        String s = "";
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
        onCreate(str, callBack);

    }

    private static InputStream reader;
    private static OutputStream writer;
    private static Socket socket;

    public void onCreate(final String command, final SocketCallBack callBack) {
        new Thread(new Runnable() {
            public void run() {
                try {

                    System.out.println("command:" + command);

                    // 如果是close命令，就表示要关闭socket连接
                    if("close".equals(command)){
                        try{
                            closeSocket();
                        } catch(Exception ex){
                            ex.printStackTrace();
                        }

                        return;
                    }
                    //初始化socket
                    if(socket == null){

//                        socket = new Socket("58.221.58.190", 347);
                        socket = new Socket(IP_ADDRESS, PORT);
                        System.out.println("socket is null, new socket -- ");
                    }
//                    writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if(reader == null) {
                        reader = socket.getInputStream();
                        System.out.println("reader is null , get input stream -- ");
                    }

                    if(writer == null) {
                        writer = socket.getOutputStream();
                        System.out.println("write is null , get writer stream -- ");
                    }

                    System.out.println("成功连接服务器！");
                    dataSend(command);
                    if(!hasReceive){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dataReceived();
                            }
                        });
                    };
                } catch (IOException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void closeSocket() throws IOException {
        if(socket != null) {
            if (reader != null) {
                reader.close();
                reader = null;
            }
            if (writer != null) {
                writer.close();
                writer = null;
            }
            socket.close();
            socket = null;
            System.out.println("close -- ");
        }
    }

    //接收到数据的处理方法
    private void dataReceived(){
        final AsyncTask<Void,String,Void> read = new AsyncTask<Void, String, Void>() {
            protected Void doInBackground(Void... params) {
                String line;
                try {
                    hasReceive = true;
                    byte[]  buffer = new byte[1024];
//                    BufferedReader br = new BufferedReader(new InputStreamReader(reader,"GBK"));

                    String readLine = "";
                    System.out.println("233333333");
                    int len = 0;
//                    StringBuffer buffer = new StringBuffer();
                    while (reader != null && (len = reader.read(buffer)) > 0){
                        String bufStr = new String(buffer, 0, len, "GBK");
//                        buffer.append(readLine);
                        publishProgress(bufStr);
                        System.out.println("233333333");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        closeSocket();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    publishProgress("exception");
//
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
            handler.sendEmptyMessage(EXIST);
        }
    }

}
