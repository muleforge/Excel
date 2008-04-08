/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.routing.outbound;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Default implementation of the XlsSplitter
 *
 */
public class SimpleXlsSplitter implements XlsSplitter {
	private InputStream xlsStream;
	private String sheetName;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private Iterator rowIterator;
	
	/**
	 * Constructor takes the excel sheet as input stream and the sheet
	 * @param stream input stream
	 * @param sheetName name of the sheet. If null the first sheet will be used
	 */
	public SimpleXlsSplitter(InputStream stream, String sheetName){
		xlsStream = stream;
		InputStream xls = (InputStream)getXlsStream();
		try{
	        workbook = new HSSFWorkbook(xls);
	        if(sheetName != null && !sheetName.equals("")){
		        sheet = workbook.getSheet(sheetName);
	        }else{
		        sheet = workbook.getSheetAt(0);
	        }
	        rowIterator = sheet.rowIterator();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Map<String, HSSFCell> getNextRow() {
		Map<String, HSSFCell> rowMap = new HashMap<String, HSSFCell>();
		if(rowIterator.hasNext()){
    		HSSFRow row = (HSSFRow)rowIterator.next();
    		for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>)row.cellIterator(); cit.hasNext(); ) {
    			HSSFCell cell = cit.next();
    			rowMap.put(Short.toString(cell.getCellNum()), cell);
    		}
		}else{
			return null;
		}
		return rowMap;
	}


	public InputStream getXlsStream() {
		return xlsStream;
	}
	public void setXlsStream(InputStream xlsStream) {
		this.xlsStream = xlsStream;
	}

	public Map<String, HSSFCell> getRowRange(int startRow, int endRow) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, HSSFCell> getSelectedRows(List<Integer> rows) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
