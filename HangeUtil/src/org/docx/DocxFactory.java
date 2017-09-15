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
		 *  本方法创建单元格, 添加样式后添加到表格行中 
		 */
		@Override
		public void addStyledTableCell(Tr tableRow, String content,  
				boolean bold, long fontSize) {  
			Tc tableCell = factory.createTc();  
			DocxUtil.addStyling(tableCell, content, bold, fontSize);  
			tableRow.getContent().add(tableCell);  
		}

		//保存
		@Override
		public void save(String path) throws Docx4JException {
			wordMLPackage.save(new File(path));
		}

	}
	//内部类 工具方法类
	private static class DocxUtil {
		private static ObjectFactory factory = DocxObj.factory;
		/** 
		 *  本方法为可运行块添加字体大小信息. 首先创建一个"半点"尺码对象, 然后设置fontSize 
		 *  参数作为该对象的值, 最后我们分别设置sz和szCs的字体大小. 
		 */  
		private static void setFontSize(RPr runProperties, long fontSize) {  
			HpsMeasure size = new HpsMeasure();  
			size.setVal(new BigInteger(String.valueOf(fontSize)));  
			runProperties.setSz(size);  
			runProperties.setSzCs(size);  
		}  

		/** 
		 *  本方法给可运行块属性添加粗体属性. BooleanDefaultTrue是设置b属性的Docx4j对象, 严格 
		 *  来说我们不需要将值设置为true, 因为这是它的默认值. 
		 */  
		private static void addBoldStyle(RPr runProperties) {  
			BooleanDefaultTrue b = new BooleanDefaultTrue();  
			b.setVal(true);  
			runProperties.setB(b);  
		}

		/** 
		 *  本方法给表格添加边框 
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
		 *  这里我们添加实际的样式信息, 首先创建一个段落, 然后创建以单元格内容作为值的文本对象;  
		 *  第三步, 创建一个被称为运行块的对象, 它是一块或多块拥有共同属性的文本的容器, 并将文本对象添加 
		 *  到其中. 随后我们将运行块R添加到段落内容中. 
		 *  直到现在我们所做的还没有添加任何样式, 为了达到目标, 我们创建运行块属性对象并给它添加各种样式. 
		 *  这些运行块的属性随后被添加到运行块. 最后段落被添加到表格的单元格中. 
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
