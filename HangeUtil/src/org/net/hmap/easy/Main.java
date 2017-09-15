package org.net.hmap.easy;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.net.hmap.Route;
import org.net.hmap.Transfer;

public class Main {
	
	public static void main(String[] args) throws Exception {
		Route route = new Route("127.0.0.1", 8888, "192.168.71.140", 8680);
		InetAddress myAD = Inet4Address.getByName("127.0.0.1");
		ServerSocket service = new ServerSocket(route.getLocalPort(), 4, myAD);
			Socket sock = service.accept();
			Thread t = new Transfer(sock, route);
			t.join();
	}
}
