package org.netjava.jportmap;
import java.net.*;
import java.util.*;

/**
 * <p>Title: �˿�ת����</p>
 * <p>Description:������������ </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: www.NetJava.org</p>
 * @author javafound
 * @version 1.0
 */
public class Server extends Thread {
	//����һ��ת��������
	public Server(Route route, int id) {
		this.route = route;
		connectionQueue = new Vector();
		myID = id;
		start();
	}

	//�ر������������
	public void closeServer() {
		isStop = true;
		if (null != myServer) {
			closeServerSocket();
		} while (this.connectionQueue.size() > 0) {
			Transfer tc = (Transfer) connectionQueue.remove(0);
			tc.closeSocket(tc.socket);
			tc = null;
		}
	}

	//����ת����������ִ���߳�
	public void run() {
		SysLog.info(" start Transfer......:" + route.toString());
		ServerSocket myServer = null;
		try {
			InetAddress myAD = Inet4Address.getByName(route.LocalIP);
			myServer = new ServerSocket(route.LocalPort, 4, myAD);
		} catch (Exception ef) {
			SysLog.severe("Create Server " + route.toString() + " error:" + ef);
			closeServerSocket();
			return;
		}
		SysLog.info("Transfer Server : " + route.toString() + " created OK");
		while (!isStop) {
			String clientIP = "";
			try {
				Socket sock = myServer.accept();
				clientIP = sock.getInetAddress().getHostAddress();
				if (checkIP(route, clientIP)) {
					SysLog.warning(" ransfer Server : " + route.toString() +
							"  Incoming:" + sock.getInetAddress());
					sock.setSoTimeout(0);
					connCounter++;
					Transfer myt = new Transfer(sock, route);
					connectionQueue.add(myt);
				} else {
					SysLog.warning(" ransfer Server : " + route.toString() +
							"  Refuse :" + sock.getInetAddress());
					closeSocket(sock);
				}

			} catch (Exception ef) {
				SysLog.severe(" Transfer Server : " + route.toString() +
						" accept error" + ef);
			}
		}
	}

	//�������IP�Ƿ����
	private static boolean checkIP(Route route, String inIP) {
		String[] inI = string2StringArray(inIP, ".");
		String[] list = string2StringArray(route.AllowClient, ".");
		if (inI.length != list.length) {
			SysLog.severe(" Transfer Server Error Cfg AllowClient : " +
					route.toString());
			return false;
		}
		for (int i = 0; i < inI.length; i++) {
			if ((!inI[i].equals(list[i])) && !(list[i].equals("*"))) {
				System.out.println(": " + inI[i] + " :" + list[i]);
				return false;
			}
		}
		return true;
	}


	/*
	 * @param srcString ԭ�ַ���
	 * @param separator �ָ���
	 * @return Ŀ������
	 */
	private static final String[] string2StringArray(String srcString,
			String separator) {
		int index = 0;
		String[] temp;
		StringTokenizer st = new StringTokenizer(srcString, separator);
		temp = new String[st.countTokens()];
		while (st.hasMoreTokens()) {
			temp[index] = st.nextToken().trim();
			index++;
		}
		return temp;
	}

	//�ر�ServerSocket
	private void closeServerSocket() {
		try {
			this.myServer.close();
		} catch (Exception ef) {
		}
	}
	private void closeSocket(Socket s) {
		try {
			s.close();
		} catch (Exception ef) {

		}
	}

	//������
	private ServerSocket myServer = null;
	//������п���
	private boolean isStop = false;
	//
	private Vector connectionQueue = null;
	private int connCounter = 0;
	// ·�ɶ���
	private Route route = null;
	//�����ID�ţ���δ��
	private static int  myID = 0;
}
