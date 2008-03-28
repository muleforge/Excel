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

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * This splitter can be used to split a excel into specific parts, e.g. rows,
 * columns,.. At present the splitter only allows to split the excel into rows
 * 
 */
public interface XlsSplitter {

	/**
	 * Creates a map, containing all cells of a row.
	 * 
	 * @return map containing all cells of a row
	 */
	public Map<String, HSSFCell> getNextRow();

	/**
	 * Read a range of rows
	 * 
	 * @param startRow starting index to begin to read the excel
	 * @param endRow end index
	 * @return map containing all cells of the selected rows
	 */
	public Map<String, HSSFCell> getRowRange(int startRow, int endRow);
	
	/**
	 * Read only the given row indexes
	 * @param rows a list of row indexes
	 * @return map containing all cells of the selected rows
	 */
	public Map<String, HSSFCell> getSelectedRows(List<Integer> rows);

}
