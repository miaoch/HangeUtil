/*
 * Route.java
 *
 * Created on 2006��12��28��, ����12:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netjava.jportmap;
/**
 *ת��������������ݶ���ģ��
 * <p>Company: www.NetJava.org</p>
 * @author javafound
 */
public class Route {
    public Route() {}

    //jPortMap�󶨵�IP
    String  LocalIP="";
    //�����Ķ˿�
    int     LocalPort=0;
    //ת�����ݵ�Ŀ�����IP
    String  DestHost="";
    //ת����Ŀ��˿�
    int     DestPort=0;
    //���ת������ɽ����IP�б�
    String  AllowClient="";
//��д��toString�������������Route�������Ϣ�Ա�debug    
    public String toString() {
        StringBuffer stb = new StringBuffer();
        stb.append(" LocalADD  " + LocalIP);
        stb.append(" :" + LocalPort);
        stb.append(" --->DestHost " + DestHost);
        stb.append(" :" + DestPort);
        stb.append("   (AllowClient) " + AllowClient);
        return stb.toString();
    }
}
