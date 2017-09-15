package org.net.hmap;

/**
 * �൱�ھ�̬·��
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
	private String clientIP;//����ͨ��*�Ž���ģ��ƥ��
	private int localPort;//ת���˿�
	private String destIP;//Ŀ���ַ
	private int destPort;//Ŀ��˿�
	private boolean deny;//�Ƿ�Ϊ�ܾ�����������ƥ�䡣Ҫ�ų�ĳ��ip����дdeny·��
	
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
