package com.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.database.ConnectionPool;


public class NIOServer1 {
	
	private static final Logger logger = LoggerFactory.getLogger(NIOServer1.class);
	
	public int flag = 1;
	
	//定义选择器
	private Selector selector;
	
	//定义线程池
	private ExecutorService tp = Executors.newCachedThreadPool();
	
	 // 超时时间,单位毫秒
    private static final int TimeOut=3000;
	
	//统计服务器线程在一个客户端花费的时间
	/*public static Map<Socket,Long> time_stat = new HashMap<Socket,Long>(10240);*/
	
	// 定义数据库连接池
	public static ConnectionPool cp = new ConnectionPool(20);
	
	public void closeServer()
	  {
	    try
	    {
	      this.selector.close();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	  }
	//
	public void startServer() throws Exception {
		logger.info("服务器开始启动");
		selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//将ServerSocketChannel设置为非阻塞模式
		ssc.configureBlocking(false);
		//绑定端口，实现 IP 套接字地址（IP 地址 + 端口号）
		//InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(),8000);
		InetSocketAddress isa = new InetSocketAddress(8000);
		ssc.socket().bind(isa);
		//将ServerSocketChannel绑定到selector上，并注册它感兴趣的时间为OP_ACCEPT
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		//死循环
		while(true) {
			//这是阻塞方法，等待读取数据
			if(selector.select(TimeOut)==0){
                continue;
            }
			selector.select();
			//获取准备好的selectedKeys
			Iterator<SelectionKey> i = selector.selectedKeys().iterator();
			/*long e = 0;*/
			while (i.hasNext()) {
				SelectionKey sk = i.next();
				//从集合中删除
				i.remove();
				//判断状态,接收
				if (sk.isAcceptable()) {
					logger.info("客户端连接");
					doAccept(sk);
				}
				//判断状态,可读
				else if (sk.isValid() && sk.isReadable()) {
					/*if (!time_stat.containsKey(((SocketChannel)sk.channel()).socket())) {
						time_stat.put(((SocketChannel)sk.channel()).socket(), System.currentTimeMillis());
					}*/
					//System.out.println("读取");
					//wait(50);
					doRead(sk);
				} 
				//判断状态,写入
				else if (sk.isValid() && sk.isWritable()) {
					doWrite(sk);
					/*e = System.currentTimeMillis();
					long b = time_stat.remove(((SocketChannel)sk.channel()).socket());
					logger.info("spend:" + (e-b) + "ms");*/
					/*disconnect(sk);*/
				}
				
			}
		}
	}
	//接收 ，与socket编程类似
	private void doAccept(SelectionKey sk) {
		// TODO Auto-generated method stub
		ServerSocketChannel server = (ServerSocketChannel) sk.channel();
		SocketChannel clientChannel;
		try {
			//生成与客户端通信的通道
			clientChannel = server.accept();
			//channel设置为非阻塞模式
			clientChannel.configureBlocking(false);
			
			//将新生成的channel注册到selector选择器上
			clientChannel.register(selector, SelectionKey.OP_READ);
			//新建一个对象实例，一个EchoClient实例表示一个客户端
			//返回客户端地址
			InetAddress clientAddress = clientChannel.socket().getInetAddress();
			logger.info("Accepted connection from " + clientAddress.getHostAddress() + ".");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Failed to accept new client.");
			e.printStackTrace();
		}
	}

	private void doRead(SelectionKey sk) {
		// TODO Auto-generated method stub
		SocketChannel channel = (SocketChannel) sk.channel();
		//初始化8k的缓存区读取数据
		ByteBuffer bb = ByteBuffer.allocate(8192);
		int len;
		//System.out.println("进入读取");
		try {
			//sk.wait(50);
			//将字节序列从此通道中读入给定的缓冲区
			len = channel.read(bb);
			if (len < 0) {
				disconnect(sk);
				return;
			}		
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("failed to read from client");
			e.printStackTrace();
			disconnect(sk);
			return;
		}
		//使缓冲区为一系列新的通道写入或相对获取 操作做好准备
		bb.flip();
		//记录时间，并读取
		tp.execute(new HandleMsg(sk,bb));
	}
	
	private void doWrite(SelectionKey sk) {
		// TODO Auto-generated method stub
		SocketChannel channel = (SocketChannel) sk.channel();
		ByteBuffer bb = ByteBuffer.allocate(40);
		// 返回数据
		String outS = "Accepet OK!";
	    byte[] outb = null;
		try {
			outb = outS.getBytes("UTF-8");
			System.out.println(outb.length);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    bb.put(outb);
		try {
			int len = channel.write(bb);
			logger.info(""+len);
			if (len == -1) {
				disconnect(sk);
				return;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Failed to write to client.");
			e.printStackTrace();
			disconnect(sk);
		}
		if (bb.remaining() == 0) {
			sk.interestOps(SelectionKey.OP_READ);
		}
	}
	
	private void disconnect(SelectionKey sk) {
		// TODO Auto-generated method stub
		SocketChannel channel = (SocketChannel) sk.channel();

        InetAddress clientAddress = channel.socket().getInetAddress();
        logger.info(clientAddress.getHostAddress() + " disconnected.");
        System.out.println(clientAddress.getHostAddress() + " disconnected.");
        try {
            channel.close();
        } catch (Exception e) {
        	logger.info("Failed to close client socket channel.");
            e.printStackTrace();
        }
	}
	
	class HandleMsg implements Runnable{
		
		SelectionKey sk ;
		ByteBuffer bb;
		
		public HandleMsg(SelectionKey sk, ByteBuffer bb) {
			this.sk = sk;
			this.bb = bb;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("读取操作开始");
			logger.info("读取数据");
			//数据接收逻辑开始
			if (((bb.limit() - bb.position()) % 13 != 0) || (bb == null))
			{
				logger.debug("收到了一次长度为" + (bb.limit() - bb.position()) + "的数据，不符合数据规范，没有储存到数据库" + "数据为" + bb);
			}
			else
			{
				String dataSql = " ";
				
				int dataNumber = (bb.limit() - bb.position()) / 13;
				for (int i = 0; i < dataNumber; i++)
				{
					byte[] ba = new byte[13];
					int[] data = new int[9];
					for (int n = 0; n < 13; n++) {
						ba[n] = bb.get(n + i * 13);
					}
					for (int n = 0; n < 4; n++) {
						data[n] = (0xFF & ba[n]);
					}
					int n = 4;
					for (int k = 4; n < 12; k++)
					{
						data[k] = (((0xFF & ba[n]) << 8) + (0xFF & ba[(n + 1)]));n += 2;
					}
					data[8] = (0xFF & ba[12]);
					int device_ip_1 = data[0];
					int device_ip_2 = data[1];
					int device_ip_3 = data[2];
					int device_ip_4 = data[3];
					String device_ip = device_ip_1 + "." + device_ip_2 + "." + device_ip_3 + "." + device_ip_4;
					int pulse_current = data[4];
					int pulse_accumulation = data[5];
					int voltage = data[6];
					int resistance_current = data[7];
					logger.info("数据为" + pulse_current + "***" + pulse_accumulation + "***" + voltage + "***" + resistance_current + "***");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String generate_time = sdf.format(new Date());
					if (i == dataNumber - 1) {
						dataSql = dataSql + "('" + CommonUtil.GUID() + "'" + "," + "'" + device_ip + "'" + "," + generate_time + "," + pulse_current + "," + pulse_accumulation + "," + voltage + "," + resistance_current + ")";
					} else {
						dataSql = dataSql + "('" + CommonUtil.GUID() + "'" + "," + "'" + device_ip + "'" + "," + generate_time + "," + pulse_current + "," + pulse_accumulation + "," + voltage + "," + resistance_current + "),";
					}
				}
				try
				{
					
					logger.info("建立一次和数据库的链接");
					Connection conn = cp.getConnection();
					Statement stmt2 = conn.createStatement();
					
					String insertData = "set @insertData = concat(\"INSERT INTO data_\", date_format(curdate(),'%Y%m%d'),\" (`data_id`,`device_ip`, `generate_time`, `pulse_current`, `pulse_accumulation`, `voltage`, `resistance_current`)values" + 
							dataSql + "\");" + "PREPARE insertData FROM @insertData;EXECUTE insertData;";
					logger.info(insertData);
					stmt2.executeUpdate(insertData);
					stmt2.close();
					cp.returnConnection(conn);
					logger.info("对数据库进行操作时成功");
				}
				catch (Exception e)
				{
					logger.info("对数据库进行操作时失败");
					logger.info(e.getMessage());
				}
			}
			//数据接收结束
			sk.interestOps(/**/SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			//强迫selector立即返回
			selector.wakeup();
		}
		
	}
}




