package test.thread;

import static test.thread.PrintUtil.print;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloseResource {
	public static void main(String args[]) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8080);
		InputStream in = new Socket("localhost", 8080).getInputStream();
		exec.execute(new IOBlocked(in));//IOBlocked在同一个包下
		exec.execute(new IOBlocked(System.in));
		Thread.sleep(100);
		print("Shuting down all threads");
		exec.shutdownNow();
		Thread.sleep(1000);
		print("Closing " + in.getClass().getSimpleName());
		in.close();
		Thread.sleep(1000);
		print("Closing " + System.in.getClass().getSimpleName());
		System.in.close();
	}
}
