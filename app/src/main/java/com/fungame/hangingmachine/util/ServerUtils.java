package com.fungame.hangingmachine.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by tom on 2015/11/19.
 * descripton :
 */
public class ServerUtils {

    private static SocketCallBack callBack;

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

    public void sendCommand(final String s, final SocketCallBack cb) {

            callBack = cb;
            new Thread(){
                @Override
                public void run(){
                    try {
                        Socket sock = null;
                        try {
                           sock = new Socket("58.221.58.99", 347);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        OutputStream outputStream = sock.getOutputStream();
                        InputStream inputStream = sock.getInputStream();
//                        byte[] outBuffer = s.getBytes();
                        byte[] outBuffer = get16Byte(s);
                        outputStream.write(outBuffer, 0, outBuffer.length);
                        outputStream.flush();
                        byte[] buffer = new byte[1024];
                        inputStream.read(buffer);
//                        String receiver  = new String(buffer);
                        String receiver = toStringHex(buffer);
                        Message msg = handler.obtainMessage();
                        msg.obj = receiver;
                        msg.what = 100;
                        Log.i("--tom", "get result:" + receiver);
                        handler.sendMessage(msg);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();

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

}
