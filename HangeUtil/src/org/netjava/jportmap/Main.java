package org.netjava.jportmap;
import java.io.*;
import java.util.*;

/**
 * <p>Title: �˿�ת����</p>
 * <p>Description:�������ࣺ��ȡ���ã������������� </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: www.NetJava.org</p>
 * @author javafound
 * @version 1.0
 */
public class Main {

	//Server����������
	private static List<Server> serverList = new ArrayList<Server>();

	//Route����
	private static List<Route> routeList = new ArrayList<Route>();

	public static void main(String args[]) {
		startService();
	}

	//start
	public static void startService() {
		if (!loadCfgFile()) {
			System.exit(1);
		} 
		while (serverList.size() > 0) {
			Server ts = serverList.remove(0);
			ts.closeServer();
		}
		for (int i = 0; i < routeList.size(); i++) {
			Route r = routeList.get(i);
			Server server = new Server(r, i);
			serverList.add(server);
		}
	}


	// ֹͣ����ӿ�,��������ģ�����
	public static void stop() {
		while (serverList.size() > 0) {
			Server ts = serverList.remove(0);
			ts.closeServer();
		}
	}
	/**
	 *�������ļ���ȡ���ݣ�����Route����
	 * read cfg parameter
	 * @return boolean
	 */
	private static boolean loadCfgFile() {
		try {
			String userHome = System.getProperties().getProperty("user.dir");
			if (userHome == null) {
				userHome = "";
			} else {
				userHome = userHome + File.separator;
			}
			userHome += "cfg" + File.separator + "jPortMap.cfg";
			InputStream is = new FileInputStream(userHome);
			Properties pt = new Properties();
			pt.load(is);
			//���м���ҵ��ģ��
			int ServiceCount = Integer.parseInt(pt.getProperty("TransferCount"));
			for (; ServiceCount > 0; ServiceCount--) {
				Route r = new Route();
				r.LocalIP = pt.getProperty("LocalIP." + ServiceCount).trim();
				r.LocalPort = Integer.parseInt(pt.getProperty("LocalPort." +
						ServiceCount).trim());
				r.DestHost = pt.getProperty("DestHost." + ServiceCount).trim();
				r.DestPort = Integer.parseInt(pt.getProperty("DestPort." +
						ServiceCount).trim());
				r.AllowClient = pt.getProperty("AllowClient." + ServiceCount).
						trim();
				routeList.add(r);
			}
			is.close();
			SysLog.info("ystem Read cfg file OK");
		} catch (Exception e) {
			System.out.println("�Ҳ��������ļ�:"+e);
			SysLog.severe("loadCfgFile false :" + e);
			return false;
		}
		return true;
	}
}



