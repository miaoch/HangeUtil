package org.net.hmap;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.netjava.jportmap.SysLog;

public class Transfer extends Thread {
	//传输任务的Route对象
	private Route route;
	//传入数据用的Socket
	private Socket socket;
	//超时
	private static int TIMEOUT = 1000;
	//缓存
	private static int BUFSIZ = 40960;

	public Transfer(Socket s, Route route) {
		this.route = route;
		this.socket = s;
		this.start();
	}

	// 执行操作的线程
	public void run() {
		Socket outbound = null;
		try {
			outbound = new Socket(route.getDestIP(), route.getDestPort());
			socket.setSoTimeout(TIMEOUT);
			outbound.setSoTimeout(TIMEOUT);
			pipe(socket.getInputStream(), outbound.getInputStream(),
					outbound.getOutputStream(), socket.getOutputStream());
			//pipe(socket, outbound);
		} catch (Exception e) {
			SysLog.severe(" transfer error:" + route.toString() + " :" + e);
		} finally {
			SysLog.warning("Disconnect :"+ route.toString());
			closeSocket(outbound);
			closeSocket(socket);
		}
	}

	/**
	 * 传输的实现方法
	 * 从is0读取写入到os0，将os1返回的写入到is1
	 */
	private void pipe(InputStream is0, InputStream is1,
			OutputStream os0, OutputStream os1) {
		try {
			int ir;
			byte bytes[] = new byte[BUFSIZ];
			while (true) {
				try {
					if ((ir = is0.read(bytes)) > 0) {
						os0.write(bytes, 0, ir);
					} else if (ir < 0) {
						break;
					}
				} catch (InterruptedIOException e) {}
				try {
					if ((ir = is1.read(bytes)) > 0) {
						os1.write(bytes, 0, ir);
					} else if (ir < 0) {
						break;
					}
				} catch (InterruptedIOException e) {}
			}
		} catch (Exception e0) {
			SysLog.warning(" Method pipe" + this.route.toString() + " error:" +
					e0);
		}
	}
	
	//关闭socket
	public void closeSocket(Socket s) {
		try {
			s.close();
		} catch (Exception ef) {}
	}
	public void closeSocket() {
		closeSocket(socket);
	}
}
