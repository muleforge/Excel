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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

public class BeanToXlsTransformer extends AbstractTransformer {
	// Transformer is used to create XLS
	private XLSTransformer transformer = new XLSTransformer();
	private String template;
	private InputStream templateInputStream;
	
	protected Object doTransform(Object src, String encoding) throws TransformerException {
		Map beans = (Map) src;
		ByteArrayOutputStream out = null;
		try{
			templateInputStream = new FileInputStream(template);
			out = new ByteArrayOutputStream();
			transformer.transformXLS(templateInputStream, beans).write(out);
		}catch(Exception e){
			throw new TransformerException(this,e);
		}
		return out.toByteArray();
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
//	public Object clone() throws CloneNotSupportedException {
//		BeanToXlsTransformer clone = (BeanToXlsTransformer) super.clone();
//        clone.setTemplate(template);
//        return clone;
//	}

}
