package org.net.hmap;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.netjava.jportmap.SysLog;

public class Transfer extends Thread {
	//���������Route����
	private Route route;
	//���������õ�Socket
	private Socket socket;
	//��ʱ
	private static int TIMEOUT = 1000;
	//����
	private static int BUFSIZ = 40960;

	public Transfer(Socket s, Route route) {
		this.route = route;
		this.socket = s;
		this.start();
	}

	// ִ�в������߳�
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
	 * �����ʵ�ַ���
	 * ��is0��ȡд�뵽os0����os1���ص�д�뵽is1
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
	
	//�ر�socket
	public void closeSocket(Socket s) {
		try {
			s.close();
		} catch (Exception ef) {}
	}
	public void closeSocket() {
		closeSocket(socket);
	}
}
