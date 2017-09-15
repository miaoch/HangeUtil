package org.net.hmap;

/**
 * 相当于静态路由
 * @author miaoch
 *
 */
public class Route {
	public Route(String clientIP, int localPort, 
			String destIP, int destPort) {
		this(clientIP, localPort, destIP, destPort, false);
	}
	private Route(String clientIP, int localPort, 
			String destIP, int destPort, boolean deny) {
		this.clientIP = clientIP;
		this.localPort = localPort;
		this.destIP = destIP;
		this.destPort = destPort;
		this.deny = deny;
	}
	private String clientIP;//可以通过*号进行模糊匹配
	private int localPort;//转发端口
	private String destIP;//目标地址
	private int destPort;//目标端口
	private boolean deny;//是否为拒绝，从上往下匹配。要排除某个ip得先写deny路由
	
	public String getClientIP() {
		return clientIP;
	}
	public int getLocalPort() {
		return localPort;
	}
	public String getDestIP() {
		return destIP;
	}
	public int getDestPort() {
		return destPort;
	}
	/*public boolean isDeny() {
		return deny;
	}*/
	
	@Override
	public String toString() {
		return (deny ? "deny " : "permit ") 
				+ clientIP + " " + localPort + " -> "
				+ destIP + " " + destPort + "\n";
	}
}
