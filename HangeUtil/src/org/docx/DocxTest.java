package org.docx;

import java.io.*;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.docx4j.dml.CTPositiveSize2D;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

public class DocxTest {
	private static Object[] value = {
		"2017013015020303（测试）", 
		"已结案 2000-01-30 14:21",
		null, 
		null,
		null,
		null,
		"问题来源测试",
		"123456.jpg",//地图
		null,
		"测试反映人（13888888888）",
		null,
		null,
		"大类",
		null,
		null,
		"小类",
		null,
		null,
		"类型说明",
		null,
		null,
		"问题性质",
		null,
		null,
		null,
		"测试街道",
		null,
		"测试社区",
		null,
		"测试网格地址",
		null,
		"事件等级",
		null,
		null,//33
		null,
		null,//35
		null,
		null,//37
		null,
		"事发地址",
		null,
		null,
		"问题描述",
	};
	private static String[][] ajlc = new String[][]{
		new String[]{"区中心", "受理员001", "2016-1-3 17:05", "备注: 受理员001执行了立案操作", "意见：尽快处理"},
		new String[]{"区中心", "受理员001", "2016-1-2 17:04", "备注: 受理员001执行了立案操作", "意见：尽快处理"},
		new String[]{"区中心", "受理员001", "2016-1-1 17:03", "备注: 受理员001执行了立案操作", "意见：尽快处理"}
	};
	//用流数组替换
	private static String[][] sb = new String[][]{
		new String[]{"123456.jpg", null, "123456.jpg"},
		new String[]{"123456.jpg", "123456.jpg", "123456.jpg"}
	};
	private static String[][] cz = new String[][]{
		new String[]{null, "123456.jpg", "123456.jpg"},
	};
	private static String[][] hc = new String[][]{
		new String[]{null, "123456.jpg", "123456.jpg"},
		new String[]{"123456.jpg", "123456.jpg", null}
	};
	//涉事人数：   123   重点场所：xxx
	private static String[] zzxx = new String[]{"5", "有", "当事人1（15000000000） 当事人2（1515151515）当事人1（15000000000） 当事人2（1515151515）当事人1（15000000000） 当事人2（1515151515）"};
	private static boolean isZZ = false;
	
	private static final int index_zz = 33;//每隔2个
	private static final int beginZZ = 13;//综治
	private static final int beginLC = 21;//流程
	private static final int beginSB = 26;//上报
	private static final int beginCZ = 28;//处置
	private static final int beginHC = 30;//核查
	private static final String mbPath = "打印模板.docx";
	private static final String localPrefix = "";
	private static final String webPrefix = "";
	private static int zl = 0;
	private static WordprocessingMLPackage wordMLPackage;
	private static ObjectFactory factory; 

	public static void main(String[] args) throws Exception {  
		wordMLPackage = WordprocessingMLPackage.load(new File(mbPath));
		factory = Context.getWmlObjectFactory();
		MainDocumentPart part = wordMLPackage.getMainDocumentPart();
		List<Object> obj = part.getContent();
		JAXBElement<Tbl> test = (JAXBElement<Tbl>) obj.get(0);
		Tbl table = test.getValue();
		int i = 0;
		if (isZZ) {
			value[index_zz] = zzxx[0];
			value[index_zz + 2] = zzxx[1];
			value[index_zz + 4] = zzxx[2];
		}
		for (Object obj2 : table.getContent()) {
			if (obj2 instanceof Tr) {
				Tr tr = (Tr) obj2;
				for (Object obj3 : tr.getContent()) {
					Tc tc = ((JAXBElement<Tc>)obj3).getValue();
					P p = (P) tc.getContent().get(0);
					if (i < value.length && value[i] != null) {
						setPContent(p, value[i]);
					}
					i++;
				}
			}
		}
		//案卷流程
		for (String[] strs : ajlc) {
			Tr tr2 = cloneTr((Tr) (table.getContent().get(beginLC + zl)), strs, false);
			table.getContent().add(beginLC + zl, tr2);
			zl++;
		}
		table.getContent().remove(beginLC + zl);
		zl--;
		//上报图片
		for (String[] strs : sb) {
			Tr tr2 = cloneTr((Tr) (table.getContent().get(beginSB + zl)), strs, true);
			table.getContent().add(beginSB + zl, tr2);
			zl++;
		}
		table.getContent().remove(beginSB + zl);
		zl--;
		//处置图片
		for (String[] strs : cz) {
			Tr tr2 = cloneTr((Tr) (table.getContent().get(beginCZ + zl)), strs, true);
			table.getContent().add(beginCZ + zl, tr2);
			zl++;
		}
		table.getContent().remove(beginCZ + zl);
		zl--;
		//核查图片
		for (String[] strs : hc) {
			Tr tr2 = cloneTr((Tr) (table.getContent().get(beginHC + zl)), strs, true);
			table.getContent().add(beginHC + zl, tr2);
			zl++;
		}
		table.getContent().remove(beginHC + zl);
		zl--;
		if (!isZZ) { //删除综治信息
			table.getContent().remove(beginZZ + 1);
			table.getContent().remove(beginZZ);
		}
		long time = System.currentTimeMillis();
		wordMLPackage.save(new File(localPrefix + time + ".docx"));
		wordMLPackage = null;
		//return webPrefix + time + ".docx";
	}  

	private static void setPContent(P p, Object content) throws Exception {
		List<Object> obj = p.getContent();
		Tc tc = (Tc) p.getParent();
		if (obj.size() == 0) return;
		R r = (R) obj.get(0);
		JAXBElement je = (JAXBElement) r.getContent().get(0);
		if (je.getValue() instanceof Text) {
			Text text = (Text) je.getValue();
			text.setValue(content.toString());
		} else {
			Drawing d = (Drawing) ((JAXBElement) r.getContent().get(0)).getValue();
			CTPositiveSize2D pos = ((Inline)d.getAnchorOrInline().get(0)).getExtent();
			P paragraphWithImage = addInlineImageToParagraph(new File(content.toString()), wordMLPackage, pos.getCx(), pos.getCy());
			tc.getContent().set(0, paragraphWithImage);
		}
	}

	/**
	 * 复制一行，如果是图片传入图片路径，后期可以改成  输入流数组
	 * @param tr
	 * @param strs
	 * @param isImage
	 * @return
	 * @throws Exception
	 */
	private static Tr cloneTr(Tr tr, String[] strs, boolean isImage) throws Exception {
		Tr result = factory.createTr();
		result.setRsidDel(tr.getRsidDel());
		result.setRsidR(tr.getRsidR());
		result.setRsidRPr(tr.getRsidRPr());
		result.setRsidTr(tr.getRsidTr());
		result.setTblPrEx(tr.getTblPrEx());
		result.setTrPr(tr.getTrPr());
		int i = 0;
		for (Object obj1 : tr.getContent()) {
			Tc tc = ((JAXBElement<Tc>)obj1).getValue();
			Tc tc_copy = factory.createTc();
			tc_copy.setTcPr(tc.getTcPr());
			result.getContent().add(tc_copy);
			for (Object obj2 : tc.getContent()) {
				P p = (P) obj2;
				P p_copy = factory.createP();
				if (p.getContent().isEmpty()) {
					tc_copy.getContent().add(p_copy);
					break;
				}
				p_copy.setPPr(p.getPPr());
				p_copy.setRsidDel(p.getRsidDel());
				p_copy.setRsidP(p.getRsidP());
				p_copy.setRsidR(p.getRsidR());
				p_copy.setRsidRDefault(p.getRsidRDefault());
				p_copy.setRsidRPr(p.getRsidRPr());
				R r = (R) p.getContent().get(0);
				R r_copy = factory.createR();
				r_copy.setRPr(r.getRPr());
				r_copy.setRsidDel(r.getRsidDel());
				r_copy.setRsidR(r.getRsidR());
				r_copy.setRsidRPr(r.getRsidRPr());
				p_copy.getContent().add(r_copy);
				if (isImage) {
					if (strs[i] == null) {
						tc_copy.getContent().add(p_copy);
					} else {
						Drawing d = (Drawing) ((JAXBElement) r.getContent().get(0)).getValue();
						CTPositiveSize2D pos = ((Inline)d.getAnchorOrInline().get(0)).getExtent();
						P paragraphWithImage = addInlineImageToParagraph(new File(strs[i]), wordMLPackage, pos.getCx(), pos.getCy());
						if (tc_copy.getContent().isEmpty()) {
							tc_copy.getContent().add(paragraphWithImage);
						} else {
							tc_copy.getContent().set(0, paragraphWithImage);
						}
					}
				} else {
					tc_copy.getContent().add(p_copy);
					Text text_copy = factory.createText();
					text_copy.setValue(strs[i]);
					r_copy.getContent().add(text_copy);
				}
				i++;
			}
		}
		return result;
	}
	
	//用于测试表格包含多少单元数据
	@Deprecated
	private static boolean test(P p, int i) throws Exception {
		List<Object> obj = p.getContent();
		Tc tc = (Tc) p.getParent();
		if (obj.size() == 0) return true;
		R r = (R) obj.get(0);
		JAXBElement je = (JAXBElement) r.getContent().get(0);
		if (je.getValue() instanceof Text) {
			Text text = (Text) je.getValue();
			System.out.println(i + ": " + text.getValue());
		} else if (je.getValue() instanceof Drawing){
			System.out.println(i + ": image");
			return false;
		}
		return true;
	}

	/** 
	 *  向新的段落中添加内联图片并返回这个段落. 
	 * @param inline 
	 * @return 
	 */  
	private static P addInlineImageToParagraph(File file, WordprocessingMLPackage wordMLPackage, long width, long height) throws Exception {  
		int docPrId = 1;  
		int cNvPrId = 2; 
		byte[] bytes = convertImageToByteArray(file);  
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);  
		Inline inline = imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, width, height, false);
		P paragraph = factory.createP();  
		R run = factory.createR();  
		paragraph.getContent().add(run);  
		Drawing drawing = factory.createDrawing();  
		run.getContent().add(drawing);  
		drawing.getAnchorOrInline().add(inline);  
		return paragraph;  
	} 

	/** 
	 * 将图片从文件转换成字节数组. 
	 * @param file 
	 * @return 
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */  
	private static byte[] convertImageToByteArray(File file) throws FileNotFoundException, IOException {  
		InputStream is = new FileInputStream(file);  
		long length = file.length();  
		if (length > Integer.MAX_VALUE) {  
			System.out.println("File too large!!");  
		}  
		byte[] bytes = new byte[(int)length];  
		int offset = 0;  
		int numRead = 0;  
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {  
			offset += numRead;  
		}  
		if (offset < bytes.length) {  
			System.out.println("Could not completely read file " + file.getName());  
		}  
		is.close();  
		return bytes;  
	}  
}
