package org.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	private static final int DEFAUL_SHEET = 0;
	private Workbook wb = null;  
	private Sheet sheet = null;
	private String filePath;
	private int currentsheet = -1;

	public ExcelUtil (String filePath) throws IOException {
		this.filePath = filePath;
		init(filePath);
	}

	/**
	 * 根据文件路径，设置Workbook Sheet Row对象
	 * @param filePath
	 * @throws IOException 
	 */
	private void init(String filePath) throws IOException  {  
		InputStream fileItem = new FileInputStream(new File(filePath));
		String suffix = filePath.substring(filePath.lastIndexOf("."));
		if (".xls".equals(suffix)) {  
			wb = new HSSFWorkbook(fileItem);  
		} else if(".xlsx".equals(suffix)) {  
			wb = new XSSFWorkbook(fileItem);  
		} else {  
			wb = null;
		}  
		if (wb != null) {
			if (currentsheet != -1) {
				sheet = wb.getSheetAt(currentsheet);
			} else {
				sheet = wb.getSheetAt(DEFAUL_SHEET);
				currentsheet = DEFAUL_SHEET;
			}
		}
	}

	public void setSheet(int sheetNO) {
		this.sheet = wb.getSheetAt(sheetNO);
		if (sheet != null) {
			currentsheet = getSheetIndex();
		}
	}

	public void setSheet(String sheetName) {
		this.sheet = wb.getSheet(sheetName);
		if (sheet != null) {
			currentsheet = getSheetIndex();
		}
	}

	public int getSheetIndex() {
		return wb.getSheetIndex(this.sheet);
	}

	public String getSheetName() {
		return getSheetIndex() != -1?
				wb.getSheetName(getSheetIndex()):"unknow";
	}

	/**
	 * 输入行，获取数据。
	 * @param index
	 * @return String[]
	 */
	public String[] readRowData(int index) {
		String[] result = null;
		if (sheet != null && index <= sheet.getLastRowNum()) {
			Row row = sheet.getRow(index);
			if (row != null) {
				int colnum = row.getLastCellNum();
				result = new String[colnum];
				for (int i=0; i<colnum; i++) {
					Cell cell = row.getCell(i);
					if (cell != null) {
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_BLANK:
						case HSSFCell.CELL_TYPE_ERROR:
							result[i] = "null";
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
						case HSSFCell.CELL_TYPE_NUMERIC:
							//result[i] = String.valueOf(new BigDecimal(String.valueOf(cell.getNumericCellValue())).stripTrailingZeros());
							String num = String.valueOf(cell.getNumericCellValue());
							result[i] = num.substring(0, num.indexOf("."));
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							result[i] = String.valueOf(cell.getBooleanCellValue());
							break;
						case HSSFCell.CELL_TYPE_STRING:
							result[i] = cell.getStringCellValue();
							break;
						default:
							result[i] = "null";
						}

					} else {
						result[i] = "null";
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取所有数据。
	 * @return String[][]
	 */
	public String[][] readRowData() {
		String[][] result = null;
		int rownum = sheet.getLastRowNum();
		result = new String[rownum+1][];
		for (int i=0; i<=rownum; i++) {
			String[] row = readRowData(i);
			result[i] = row;
		}
		return result;
	}

	/**
	 * 将wb写入磁盘
	 */
	public void write() {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filePath);
			wb.write(stream);
			init(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 传入一个值数组，添加到当前电子表的最后面。
	 * @param rowdata
	 */
	public void writeRowData(Object[] rowdata) {
		writeRowData(sheet.getLastRowNum() + 1, rowdata);
	}

	/**
	 * 传入一个对象，添加到当前电子表的最后面。
	 * @param data
	 */
	public void writeRowData(Object data) {
		writeRowData(sheet.getLastRowNum() + 1, data);
	}

	/**
	 * 传入一个值数组和下标index(0开始)，修改第index行数据。
	 * @param index
	 * @param rowdata
	 */
	public void writeRowData(int index, Object[] rowdata) {
		Row row = sheet.createRow(index);   
		for(int i=0; i<rowdata.length; i++) {  
			Cell cell = row.createCell(i);  
			if (rowdata[i] instanceof Number) {
				cell.setCellValue(((Number)rowdata[i]).doubleValue());
			} else {
				cell.setCellValue(rowdata[i].toString());  
			}
		}
	}

	/**
	 * 传入一个对象和下标index(0开始)，修改第index行数据。
	 * @param index
	 * @param data
	 */
	public void writeRowData(int index, Object data) {
		Class c = data.getClass();
		Field[] fields = c.getDeclaredFields();
		Object[] rowData = new Object[fields.length];
		for (int i=0; i<fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				rowData[i] = fields[i].get(data);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		writeRowData(index, rowData);
	}


	/**
	 * 将list数据写入一个新的excel
	 * @param data
	 * @param filePath
	 */
	public static void writeFile(List data, String filePath) {
		Workbook workbook = null;
		String suffix = filePath.substring(filePath.lastIndexOf("."));
		if (".xls".equals(suffix)) {  
			workbook = new HSSFWorkbook();
			workbook.createSheet();
		} else if(".xlsx".equals(suffix)) {  
			workbook = new XSSFWorkbook();
			workbook.createSheet();
		} else {  
			System.out.println("请输入正确的文件名！(只支持xls和xlsx)");
		}
		try {
			FileOutputStream fileOut = null;
			File f = new File(filePath);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			ExcelUtil eu = new ExcelUtil(filePath);
			for (int i=0; i<data.size(); i++) {
				Object rowdata = data.get(i);
				eu.writeRowData(i, rowdata);
			}
			eu.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将list数据写入一个新的out流
	 * @param data
	 * @param filePath
	 */
	public static void write2os(List data, OutputStream os, String type) {
		Workbook workbook = null;
		Sheet sheet = null;
		if ("xls".equals(type)) {  
			workbook = new HSSFWorkbook();
			workbook.createSheet();
		} else if("xlsx".equals(type)) {  
			workbook = new XSSFWorkbook();
			workbook.createSheet();
		} else {  
			System.out.println("请输入正确的类型！(只支持xls和xlsx)");
			return;
		}
		sheet = workbook.getSheetAt(DEFAUL_SHEET);
		for (int index=0; index<data.size(); index++) {
			Row row = sheet.createRow(index);
			Object obj = data.get(index);
			Class c = obj.getClass();
			Field[] fields = c.getDeclaredFields();
			for (int i=0; i < fields.length; i++) {
				Cell cell = row.createCell(i);  
				fields[i].setAccessible(true);
				try {
					Object value = fields[i].get(obj);
					if (value instanceof Number) {
						cell.setCellValue(((Number)value).doubleValue());
					} else {
						cell.setCellValue(value.toString());  
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {
		List data = new ArrayList<>();
		for (int i=0; i<10; i++) {
			data.add(new Test());
		}
		ExcelUtil.writeFile(data, "D:\\hange_file\\test.xlsx");
		ExcelUtil eu = new ExcelUtil("D:\\hange_file\\test.xlsx");
		eu.writeRowData(20, new String[]{"哈哈哈", "呵呵"});
		eu.write();
		System.out.println(Arrays.toString(eu.readRowData(0)));
	}

}

class Test {
	private String a;
	public int b;
	protected double c;
	public int d;

	public Test() {
		a = "123中文";
		b = 123;
		c = 123.456;
		d = 520;
	}
}
