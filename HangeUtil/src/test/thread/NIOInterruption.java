package test.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static test.thread.PrintUtil.print;

public class NIOInterruption {
	public static void main(String args[]) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8080);
		InetSocketAddress isa = new InetSocketAddress("localhost", 8080);
		SocketChannel sc1 = SocketChannel.open(isa);
		SocketChannel sc2 = SocketChannel.open(isa);
		Future<?> f = exec.submit(new NIOBlocked(sc1));
		exec.submit(new NIOBlocked(sc2));
		exec.shutdown();
		Thread.sleep(1000);
		f.cancel(true);
		Thread.sleep(1000);
		sc2.close();
	}
}
class NIOBlocked implements Runnable {
	private final SocketChannel sc;
	public NIOBlocked(SocketChannel sc) {this.sc = sc;}
	
	@Override
	public void run() {
		try {
			print("Waiting for read() for " + this);
			sc.read(ByteBuffer.allocate(1));
		} catch (ClosedByInterruptException e1) {
			print("ClosedByInterruptException");
		} catch (AsynchronousCloseException e2) {
			print("AsynchronousCloseException");
		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
		print("Exiting NIOBlocked.run() " + this);
	}
}