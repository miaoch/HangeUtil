package test.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟LOL要10个人才能开始游戏
 * 每次必须满10个人一起加载一次，当加载量都为100时，游戏开始
 */
public class PlayGame {
	public static ExecutorService exec = Executors.newCachedThreadPool();
	public static List<Player> players = new ArrayList<Player>();
	public static CyclicBarrier barrier = new CyclicBarrier(10, new StartGame(players));
	static {
		for (int i=0; i<10; i++) {
			Player p = new Player();
			players.add(p);
			System.out.println("player" + p.id + " 准备进入游戏");
		}
	}
	public static void main(String args[]) {
		for (int i=0; i<10; i++)
			exec.execute(new LoadGame(players.get(i), barrier));
	}
}
class Player {
	private static int counter = 1;
	private int percentage = 0;
	public final int id = counter++;
	public void addPercentage(int nextInt) {
		percentage += nextInt;
		System.out.println("player" + id + " 加载了" + percentage + "%");
	}
	public int getPercentage() {return percentage;}
}
class LoadGame implements Runnable {
	
	private static Random rand = new Random(47);
	private final CyclicBarrier barrier;
	private final Player player;
	public LoadGame(Player player, CyclicBarrier barrier) {
		this.player = player;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				if (player.getPercentage() < 96) {
					player.addPercentage(rand.nextInt(5) + 1);//1~5
				} else {
					player.addPercentage(100 - player.getPercentage());
				}
				barrier.await();
			}
		} catch (InterruptedException e) {
			System.out.println();
		} catch (BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
		System.out.println("player" + player.id + " 开始游戏");
	}
	
}
class StartGame implements Runnable {
	private List<Player> players;
	public StartGame(List<Player> players) {
		this.players = players;
	}
	@Override
	public void run() {
		System.out.println("服务器集中加载一次完毕！");
		int i = 0;
		for (Player player : players) {
			if (player.getPercentage() < 100) {
				break;
			}
			i++;
		}
		if (i == players.size()) {
			System.out.println("比赛开始！");
			PlayGame.exec.shutdownNow();
		}
	}
	
}