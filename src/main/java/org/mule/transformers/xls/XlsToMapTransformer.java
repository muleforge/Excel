/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.transformers.xls;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.mule.transformers.AbstractTransformer;
import org.mule.umo.transformer.TransformerException;

public class XlsToMapTransformer extends AbstractTransformer {
	private String sheet;
	private String keypattern = "R/C";
	
	
	@Override
	protected Object doTransform(Object src, String encoding) throws TransformerException {
		InputStream xls = (InputStream)src;
		List<Map> rows = new ArrayList<Map>();
		Map<String, Object> rowMap = new HashMap<String, Object>();
		
		try{
	        HSSFWorkbook workbook = new HSSFWorkbook(xls);
	        HSSFSheet s;
	        //If no sheet is set, take the first one
	        if(sheet == null){
	        	s = workbook.getSheetAt(0);	
	        }else{
	        	s = workbook.getSheet(sheet);
	        }
	        
	    	for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>)s.rowIterator(); rit.hasNext(); ) {
	    		HSSFRow row = rit.next();
	    		for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>)row.cellIterator(); cit.hasNext(); ) {
	    			HSSFCell cell = cit.next();
	    			String key = Long.toString(row.getRowNum()) + "/" + Long.toString(cell.getCellNum());
	    			if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
	    				rowMap.put(key, cell.getCellNum());
	    			}else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
	    				rowMap.put(key, cell.getCellNum());
	    			}else {
	    				rowMap.put(key, cell.toString());
	    			}
	    		}
	    		rows.add(rowMap);
	    	}
		}catch(IOException e){
			logger.error(e);
		}
		return rows;
	}

	public void setKeypattern(String keypattern) {
		this.keypattern = keypattern;
	}
}
