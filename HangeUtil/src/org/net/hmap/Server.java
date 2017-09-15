package org.net.hmap;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Server {
	private static final String localIP = "127.0.0.1";//本地IP
	private Map<Integer, ServerSocket> myServers;//服务器
	private volatile boolean isStop;//连结队列控制
	private List<Transfer> Transferlist;//转发线程表
	private List<Route> routeTable;//转发路由表

	public Server(List<Route> routeTable) {
		this.routeTable = routeTable;
		initServers();
		SysLog.info("start Transfer......:");
		for (Route route : routeTable) {
			SysLog.info(route.toString());
		}
		for (Entry<Integer, ServerSocket> entry : myServers.entrySet()) {
			new portServer(entry.getValue());
		}
	}

	private void initServers() {
		myServers = new HashMap<Integer, ServerSocket>();
		for (Route route : routeTable) {
			try {
				InetAddress myAD = Inet4Address.getByName(localIP);
				myServers.put(route.getLocalPort(), 
						new ServerSocket(route.getLocalPort(), 4, myAD));
			} catch (Exception e) {
				SysLog.info("add server port " + route.getLocalPort() + " error:" + e);
			}
		}
	}
	
	private class portServer extends Thread {
		private int port;
		private int connCounter;
		private ServerSocket service;
		private List<Route> routeTable;//转发路由表
		public portServer(ServerSocket service) {
			this.port = service.getLocalPort();
			this.service = service;
			this.routeTable = new ArrayList<Route>();
			for (Route route : Server.this.routeTable) {
				if (route.getLocalPort() == port) {
					routeTable.add(route);
				}
			}
			start();
		}
		public void run() {
			while (!isStop) {
				Route route = null;
				try {
					Socket sock = service.accept();
					String inIP = sock.getInetAddress().getHostAddress();
					if ((route = checkIP(inIP)) != null) {
						SysLog.warning(" ransfer Server : " + route.toString() +
								"  Incoming:" + sock.getInetAddress());
						sock.setSoTimeout(0);
						connCounter++;
						Transfer myt = new Transfer(sock, route);
						Transferlist.add(myt);
					} else {
						SysLog.warning(" ransfer Server : " + route == null ? "route is null" : route.toString() +
								"  Refuse :" + sock.getInetAddress());
						closeSocket(sock);
					}

				} catch (Exception ef) {
					SysLog.severe(" Transfer Server : " + route.toString() +
							" accept error " + ef);
				}
			}
		}
		
		private Route checkIP(String inIP) {
			for (Route route : routeTable) {
				if (checkIP(route, inIP)) {
					return route;
				}
			}
			return null;
		}
		
		//检测进入的IP是否己许可
		private boolean checkIP(Route route, String inIP) {
			String[] inI = inIP.split(".");
			String[] list = route.getClientIP().split(".");
			if (inI.length != list.length) {
				SysLog.severe("Transfer Server Error Cfg AllowClient : " +
						route.toString());
				return false;
			}
			for (int i = 0; i < inI.length; i++) {
				if ((!inI[i].equals(list[i])) && !(list[i].equals("*"))) {
					SysLog.severe(": " + inI[i] + " :" + list[i]);
					return false;
				}
			}
			return true;
		}
	}
	//关闭这个服务器：
	public void closeServer() {
		isStop = true;
		while (Transferlist.size() > 0) {
			Transfer tc = (Transfer) Transferlist.remove(0);
			tc.closeSocket();
			tc = null;
		}
	}
	private void closeSocket(Socket s) {
		try {
			s.close();
		} catch (Exception ef) {}
	}
}
