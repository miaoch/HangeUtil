package ele;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.excel.ExcelUtil;

public class Analysis {
	
	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(new File("data.txt"));
		List<Order> prints = new ArrayList<Order>();
		prints.add(new Order());//����
		while (sc.hasNext()) {
			String linedata = sc.nextLine();
			String regex1 = ".*#[\\d]+!!����ô���� !!.*";
			if (linedata.matches(regex1)) {
				Order order = new Order();
				sc.nextLine();//�ڶ�����������
				order.setStoreName(sc.nextLine().substring(9));//�����дӵ�9���ַ���ʼΪ��������
				sc.nextLine();//��������������
				sc.nextLine();//��������������
				sc.nextLine();//��������������
				order.setTime(sc.nextLine().substring(14));//�����дӵ�14���ַ���ʼΪ�µ�ʱ��
				sc.nextLine();//��8����������
				String dishes = "";
				while (!(linedata = sc.nextLine()).contains("��������")) {
					dishes += linedata;
				}
				dishes = dishes.replace("!!", "");
				dishes = dishes.replace("a", "");
				dishes = dishes.replace("a", "");
				dishes = dishes.replace("!", "");
				dishes = dishes.replace("------------", "");
				dishes = dishes.replace("-----------", "");
				dishes = dishes.replaceAll("[\\s]+", " ");
				dishes = dishes.replaceAll("([\\d]+)", "$1 ");
				Pattern p = Pattern.compile("��([\\d\\.]+)[^\\d]*([\\d]+)");
				Matcher m = p.matcher(dishes);
				double sum = 0;
				while (m.find()) {
					sum += Double.parseDouble(m.group(1)) * Double.parseDouble(m.group(2));
				}
				order.setDishes(dishes);
				order.setPrice(sum + "");
				while (!(linedata = sc.nextLine()).contains("----------------")) {
					if (linedata.contains("�ͺ�")) {
						order.setLunch(linedata.replaceAll(".*(��[\\d\\.]+)[^\\d\\.]*([\\d\\.]+).*", "$2$1"));
					} else if (linedata.contains("���ͷ�")) {
						order.setDistribution(linedata.replaceAll(".*([\\d\\.]+).*", "$1"));
					} else if (linedata.contains("ʹ�ú��")) {
						order.setRed(linedata.replaceAll(".*(-[\\d\\.]+).*", "$1"));
					} else {
						order.setDiscount(linedata.replaceAll(".*(-[\\d\\.]+).*", "$1"));
					}
				}
				order.setPay(sc.nextLine().replaceAll("[^\\d\\.]*([\\d\\.]+)[^\\d\\.]*", "$1"));
				sc.nextLine();//��������
				order.setAddress(sc.nextLine().substring(6));
				sc.nextLine();//��������
				order.setName(sc.nextLine().substring(6));
				order.setPhone(sc.nextLine().substring(6));
				if (order.getRed().equals("���")) {
					order.setRed("");
				}
				if (!order.getPhone().matches(".*[\\d]+.*")) {
					order.setPhone("");
				}
				prints.add(order);
			}
			
		}
		sc.close();
		System.out.println(prints);
		ExcelUtil.writeFile(prints, "D://test/test.xls");
	}
}
//����
class Order {
	private String storeName = "��������";//������
	private String time = "�µ�ʱ��";//�µ�ʱ��
	private String dishes = "��Ʒ��������";//��Ʒ��������
	private String price = "��Ʒ�ܼ�";//��Ʒ�ܼ�
	private String lunch = "�ͺз�";//�ͺз�
	private String distribution = "���ͷ�";//���ͷ�
	private String discount = "�Żݻ";//�Żݻ
	private String red = "���";//���
	private String pay = "��֧�����";//֧�����
	private String address = "�˿͵�ַ";//�˿͵�ַ
	private String phone = "�˿ͺ���";//�˿��ֻ�
	private String name = "�˿�����";//�˿�����
	
	public String toString() {
		return storeName + time + dishes + price + lunch + distribution +
				discount + red + pay + address + phone + name;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDishes() {
		return dishes;
	}
	public void setDishes(String dishes) {
		this.dishes = dishes;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLunch() {
		return lunch;
	}
	public void setLunch(String lunch) {
		this.lunch = lunch;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getRed() {
		return red;
	}
	public void setRed(String red) {
		this.red = red;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}