package com.client;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class HeavySocketClient {
	private static ExecutorService  tp=Executors.newCachedThreadPool();
	int i = 0;
	public static class EchoClient implements Runnable{
		public void run(){
			Socket client = null;
		      PrintWriter writer = null;
		      PrintStream out = null;
		      BufferedReader reader = null;
		      try
		      {
		        client = new Socket();
		        client.connect(new InetSocketAddress("114.115.206.42", 8000));
		        //client.connect(new InetSocketAddress("localhost", 8000));
		        //client.connect(new InetSocketAddress("172.18.110.98", 8000));
		        out = new PrintStream(client.getOutputStream(), true);
		        writer = new PrintWriter(client.getOutputStream(), true);
		        
		        int a1 = 127;
		        int a2 = 0;
		        int a3 = 0;
		        int a4 = 13;
		        int a5 = 0;
		        int a6 = 1;
		        int a7 = 11;
		        int a8 = 1;
		        int a9 = 0;
		        int a10 = 12;
		        int a11 = 0;
		        int a12 = 0;
		        a12 = 15;
		        int a13 = 15;
		        byte[] data = new byte[26];
		        data[0] = ((byte)a1);
		        data[1] = ((byte)a2);
		        data[2] = ((byte)a3);
		        data[3] = ((byte)a4);
		        data[4] = ((byte)a5);
		        data[5] = ((byte)a6);
		        data[6] = ((byte)a7);
		        data[7] = ((byte)a8);
		        data[8] = ((byte)a9);
		        data[9] = ((byte)a10);
		        data[10] = ((byte)a11);
		        data[11] = ((byte)a12);
		        data[12] = ((byte)a13);
		        data[13] = ((byte)a3);
		        data[14] = ((byte)a2);
		        data[15] = ((byte)a3);
		        data[16] = ((byte)a4);
		        data[17] = ((byte)a1);
		        data[18] = ((byte)a1);
		        data[19] = ((byte)a1);
		        data[20] = ((byte)a1);
		        data[21] = ((byte)a9);
		        data[22] = ((byte)a10);
		        data[23] = ((byte)a11);
		        data[24] = ((byte)a12);
		        data[25] = ((byte)a13);
		        out.write(data, 0, 26);
		        
		        LockSupport.parkNanos(1000000000L);
		        

		        //client.close();
		        InputStream bt1 = client.getInputStream();
		        InputStreamReader bt2 = new InputStreamReader(bt1);
		        reader = new BufferedReader(bt2);
		        System.out.println(reader.toString());
		        System.out.println("jieshouok " );
		      }
		      catch (UnknownHostException e)
		      {
		        e.printStackTrace();
		        try
		        {
		          if (writer != null) {
		            writer.close();
		          }
		          if (reader != null) {
		            reader.close();
		          }
		          if (client != null) {
		            client.close();
		          }
		        }
		        catch (IOException localIOException1) {}
		      }
		      catch (IOException e)
		      {
		        e.printStackTrace();
		        try
		        {
		          if (writer != null) {
		            writer.close();
		          }
		          if (reader != null) {
		            reader.close();
		          }
		          if (client != null) {
		            client.close();
		          }
		        }
		        catch (IOException localIOException2) {}
		      }
		      finally
		      {
		        try
		        {
		          if (writer != null) {
		            writer.close();
		          }
		          if (reader != null) {
		            reader.close();
		          }
		          if (client != null) {
		            client.close();
		          }
		        }
		        catch (IOException localIOException3) {}
		      }
		    
		}
	}
    public static void main(String[] args) throws IOException {
    	EchoClient ec=new EchoClient();
    	for(int i=0;i<1;i++)
    		tp.execute(ec);
    }
}
