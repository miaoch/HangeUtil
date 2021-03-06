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
		prints.add(new Order());//标题
		while (sc.hasNext()) {
			String linedata = sc.nextLine();
			String regex1 = ".*#[\\d]+!!饿了么订单 !!.*";
			if (linedata.matches(regex1)) {
				Order order = new Order();
				sc.nextLine();//第二行无用数据
				order.setStoreName(sc.nextLine().substring(9));//第三行从第9个字符开始为店铺名称
				sc.nextLine();//第四行无用数据
				sc.nextLine();//第五行无用数据
				sc.nextLine();//第六行无用数据
				order.setTime(sc.nextLine().substring(14));//第七行从第14个字符开始为下单时间
				sc.nextLine();//第8行无用数据
				String dishes = "";
				while (!(linedata = sc.nextLine()).contains("其他费用")) {
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
				Pattern p = Pattern.compile("×([\\d\\.]+)[^\\d]*([\\d]+)");
				Matcher m = p.matcher(dishes);
				double sum = 0;
				while (m.find()) {
					sum += Double.parseDouble(m.group(1)) * Double.parseDouble(m.group(2));
				}
				order.setDishes(dishes);
				order.setPrice(sum + "");
				while (!(linedata = sc.nextLine()).contains("----------------")) {
					if (linedata.contains("餐盒")) {
						order.setLunch(linedata.replaceAll(".*(×[\\d\\.]+)[^\\d\\.]*([\\d\\.]+).*", "$2$1"));
					} else if (linedata.contains("配送费")) {
						order.setDistribution(linedata.replaceAll(".*([\\d\\.]+).*", "$1"));
					} else if (linedata.contains("使用红包")) {
						order.setRed(linedata.replaceAll(".*(-[\\d\\.]+).*", "$1"));
					} else {
						order.setDiscount(linedata.replaceAll(".*(-[\\d\\.]+).*", "$1"));
					}
				}
				order.setPay(sc.nextLine().replaceAll("[^\\d\\.]*([\\d\\.]+)[^\\d\\.]*", "$1"));
				sc.nextLine();//无用数据
				order.setAddress(sc.nextLine().substring(6));
				sc.nextLine();//无用数据
				order.setName(sc.nextLine().substring(6));
				order.setPhone(sc.nextLine().substring(6));
				if (order.getRed().equals("红包")) {
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
//订单
class Order {
	private String storeName = "店铺名称";//店铺名
	private String time = "下单时间";//下单时间
	private String dishes = "菜品名称数量";//菜品名称数量
	private String price = "菜品总价";//菜品总价
	private String lunch = "餐盒费";//餐盒费
	private String distribution = "配送费";//配送费
	private String discount = "优惠活动";//优惠活动
	private String red = "红包";//红包
	private String pay = "已支付金额";//支付金额
	private String address = "顾客地址";//顾客地址
	private String phone = "顾客号码";//顾客手机
	private String name = "顾客姓名";//顾客姓名
	
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