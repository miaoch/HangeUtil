package org.docx;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.wml.*;

public interface Docx {
	
	/** 
     *  ������������Ԫ��, �����ʽ����ӵ�������� 
     */
	public void addStyledTableCell(Tr tableRow, String content,  
            boolean bold, long fontSize);
	
	public Tbl createTbl();
	
	public Tr createTr();
	public Tr createTr(Tbl tbl);
	
	public Tc createTc();
	public Tc createTc(Tr tr);
	public Tc createTc(String content);
	public Tc createTc(Tr tr, String content);
	public Tc createTc(String content, Boolean bold, int fontSize);
	public Tc createTc(Tr tr, String content, Boolean bold, int fontSize);
	
	@Deprecated
	public ObjectFactory getFactory();
	
	public void save(String path) throws Docx4JException;
}
