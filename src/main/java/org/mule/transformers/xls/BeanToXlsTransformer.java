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
import java.io.OutputStream;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.mule.transformers.AbstractTransformer;
import org.mule.umo.lifecycle.InitialisationException;
import org.mule.umo.transformer.TransformerException;

public class BeanToXlsTransformer extends AbstractTransformer {
	// Transformer is used to create XLS
	private XLSTransformer transformer = new XLSTransformer();
	private String template;
	private InputStream templateInputStream;
	

	public void initialise() throws InitialisationException {
		super.initialise();
		try{
			templateInputStream = new FileInputStream(template);
		}catch(Exception e){
			throw new InitialisationException(e,this);
		}
	}

	protected Object doTransform(Object src, String encoding) throws TransformerException {
		Map beans = (Map) src;
		OutputStream out = null;
		try{
			out = new ByteArrayOutputStream();
			transformer.transformXLS(templateInputStream, beans).write(out);
		}catch(Exception e){
			throw new TransformerException(this,e);
		}
		return out;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}

}
