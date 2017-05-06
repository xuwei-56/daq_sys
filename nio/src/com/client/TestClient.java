package com.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class TestClient{
    public static void main(String[] args) throws Exception{
        Socket s=new Socket("114.115.206.42",8000);
        
        InputStream  inStram=s.getInputStream();
        OutputStream outStream=s.getOutputStream();
        
        // 输出
        PrintWriter out=new PrintWriter(outStream,true);
        
        String a =  "123213";
        //out.write(a);
        System.out.println(a);
        out.print("a");
        out.flush();

        s.shutdownOutput();// 输出结束
        
        // 输入
        Scanner in=new Scanner(inStram);
        StringBuilder sb=new StringBuilder();
        while(in.hasNextLine()){
            String line=in.nextLine();
            sb.append(line);
        }
        String response=sb.toString();
        System.out.println("response="+response);
    }
}