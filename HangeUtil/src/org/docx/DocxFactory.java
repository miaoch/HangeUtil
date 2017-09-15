package org.docx;

import java.io.File;
import java.math.BigInteger;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

public class DocxFactory {
	private DocxFactory() {}
	public static Docx getNewInstance() throws InvalidFormatException {
		return new DocxObj();
	}

	private static class DocxObj implements Docx {
		private static WordprocessingMLPackage wordMLPackage;  
		private static ObjectFactory factory;
		public DocxObj() throws InvalidFormatException {
			
			wordMLPackage = WordprocessingMLPackage.createPackage();
			factory = Context.getWmlObjectFactory();
		}

		@Override
		public ObjectFactory getFactory() {
			return factory;
		} 

		@Override
		public Tbl createTbl() {
			Tbl table = factory.createTbl();
			DocxUtil.addBorders(table);
			wordMLPackage.getMainDocumentPart().addObject(table);
			return table;
		}

		@Override
		public Tr createTr() {
			return factory.createTr();
		}

		@Override
		public Tr createTr(Tbl tbl) {
			Tr tr = createTr();
			tbl.getContent().add(tr);
			return tr;
		}

		@Override
		public Tc createTc() {
			return factory.createTc();
		}

		@Override
		public Tc createTc(Tr tr) {
			Tc tc = createTc();
			tr.getContent().add(tc);
			return tc;
		}

		@Override
		public Tc createTc(String content) {
			Tc tc = createTc();
			P paragraph = factory.createP();  
	        Text text = factory.createText();  
	        text.setValue(content);  
	        R run = factory.createR();  
	        run.getContent().add(text);  
	        paragraph.getContent().add(run);  
	        RPr runProperties = factory.createRPr();
	        run.setRPr(runProperties);  
	        tc.getContent().add(paragraph);
			return tc;
		}

		@Override
		public Tc createTc(Tr tr, String content) {
			Tc tc = createTc(content);
			tr.getContent().add(tc);
			return tc;
		}

		@Override
		public Tc createTc(String content, Boolean bold, int fontSize) {
			Tc tc = createTc();
			P paragraph = factory.createP();  
	        Text text = factory.createText();  
	        text.setValue(content);  
	        R run = factory.createR();  
	        run.getContent().add(text);  
	        paragraph.getContent().add(run);  
	        RPr runProperties = factory.createRPr();
	        if (bold) {  
	            DocxUtil.addBoldStyle(runProperties);  
	        }  
	        if (fontSize != 0) {  
	        	DocxUtil.setFontSize(runProperties, fontSize);  
	        }
	        run.setRPr(runProperties);  
	        tc.getContent().add(paragraph);
			return tc;
		}

		@Override
		public Tc createTc(Tr tr, String content, Boolean bold, int fontSize) {
			Tc tc = createTc(content, bold, fontSize);
			tr.getContent().add(tc);
			return tc;
		}

		/** 
		 *  ������������Ԫ��, �����ʽ����ӵ�������� 
		 */
		@Override
		public void addStyledTableCell(Tr tableRow, String content,  
				boolean bold, long fontSize) {  
			Tc tableCell = factory.createTc();  
			DocxUtil.addStyling(tableCell, content, bold, fontSize);  
			tableRow.getContent().add(tableCell);  
		}

		//����
		@Override
		public void save(String path) throws Docx4JException {
			wordMLPackage.save(new File(path));
		}

	}
	//�ڲ��� ���߷�����
	private static class DocxUtil {
		private static ObjectFactory factory = DocxObj.factory;
		/** 
		 *  ������Ϊ�����п���������С��Ϣ. ���ȴ���һ��"���"�������, Ȼ������fontSize 
		 *  ������Ϊ�ö����ֵ, ������Ƿֱ�����sz��szCs�������С. 
		 */  
		private static void setFontSize(RPr runProperties, long fontSize) {  
			HpsMeasure size = new HpsMeasure();  
			size.setVal(new BigInteger(String.valueOf(fontSize)));  
			runProperties.setSz(size);  
			runProperties.setSzCs(size);  
		}  

		/** 
		 *  �������������п�������Ӵ�������. BooleanDefaultTrue������b���Ե�Docx4j����, �ϸ� 
		 *  ��˵���ǲ���Ҫ��ֵ����Ϊtrue, ��Ϊ��������Ĭ��ֵ. 
		 */  
		private static void addBoldStyle(RPr runProperties) {  
			BooleanDefaultTrue b = new BooleanDefaultTrue();  
			b.setVal(true);  
			runProperties.setB(b);  
		}

		/** 
		 *  �������������ӱ߿� 
		 */  
		private static void addBorders(Tbl table) {  
			table.setTblPr(new TblPr());  
			CTBorder border = new CTBorder();  
			border.setColor("auto");  
			border.setSz(new BigInteger("4"));  
			border.setSpace(new BigInteger("0"));  
			border.setVal(STBorder.SINGLE);  

			TblBorders borders = new TblBorders();  
			borders.setBottom(border);  
			borders.setLeft(border);  
			borders.setRight(border);  
			borders.setTop(border);  
			borders.setInsideH(border);  
			borders.setInsideV(border);  
			table.getTblPr().setTblBorders(borders);  
		}
		/** 
		 *  �����������ʵ�ʵ���ʽ��Ϣ, ���ȴ���һ������, Ȼ�󴴽��Ե�Ԫ��������Ϊֵ���ı�����;  
		 *  ������, ����һ������Ϊ���п�Ķ���, ����һ�����ӵ�й�ͬ���Ե��ı�������, �����ı�������� 
		 *  ������. ������ǽ����п�R��ӵ�����������. 
		 *  ֱ���������������Ļ�û������κ���ʽ, Ϊ�˴ﵽĿ��, ���Ǵ������п����Զ��󲢸�����Ӹ�����ʽ. 
		 *  ��Щ���п�����������ӵ����п�. �����䱻��ӵ����ĵ�Ԫ����. 
		 */  
		public static void addStyling(Tc tableCell, String content, boolean bold, long fontSize) {  
			P paragraph = factory.createP();  
			Text text = factory.createText();  
			text.setValue(content);  
			R run = factory.createR();  
			run.getContent().add(text);  
			paragraph.getContent().add(run);  
			RPr runProperties = factory.createRPr();  
			if (bold) {  
				DocxUtil.addBoldStyle(runProperties);  
			}  
			if (fontSize != 0) {  
				DocxUtil.setFontSize(runProperties, fontSize);  
			}  
			run.setRPr(runProperties);  
			tableCell.getContent().add(paragraph);  
		}
	}
}
