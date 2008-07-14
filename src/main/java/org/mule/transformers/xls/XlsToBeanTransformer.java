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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;

import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

public class XlsToBeanTransformer extends AbstractTransformer {
	// Mapping file that should be used to map external data to internal objects
	private volatile String mappingFile;
	// Map of mapping beans
	private volatile Map mappingBeans;
	//Map of result mapping beans
	private volatile Map resultMappingBeans = new HashMap();

	public XlsToBeanTransformer() {
		registerSourceType(File.class);
		registerSourceType(InputStream.class);
		registerSourceType(byte[].class);
	}
	
	public void initialise() throws InitialisationException {
		super.initialise();
		if(mappingBeans == null) throw new IllegalArgumentException("A map of mapping beans must be declared for the transformer");
		
	}
	
	private void initialiseBeanMap() throws IllegalAccessException, ClassNotFoundException, InstantiationException{
		//Initialize beans in map
		for(Object key : mappingBeans.keySet()){
			resultMappingBeans.put(key, Class.forName(mappingBeans.get(key).toString()).newInstance());
		}
	}

	protected Object doTransform(Object src, String encoding) throws TransformerException {
		try {
			InputStream data = null;
			if (src instanceof File) {
				data = new FileInputStream((File) src);
			} else if (src instanceof byte[]) {
				data = new ByteArrayInputStream((byte[]) src);
			}else{
				data = (InputStream)src;
			}

			InputStream mappingXml = new FileInputStream(new File(mappingFile));
			XLSReader mainReader = ReaderBuilder.buildFromXML(mappingXml);
			
			initialiseBeanMap();
			
			//Temp map contains filled instances of the XLS
			Map tempMap = new HashMap();
			tempMap.putAll(resultMappingBeans);
			//Clear resultMappingBeans in order to remove old values
			resultMappingBeans.clear();
			XLSReadStatus readStatus = mainReader.read(data, tempMap);
			if (readStatus.isStatusOK()){
				//The result map contains temporary instances. Return only the mapped beans
				//Therefore update all containing beans in mappingBeans
				for(Object key : mappingBeans.keySet()){
					resultMappingBeans.put(key, tempMap.get(key));
				}
				return resultMappingBeans;
			}
		} catch (Exception e) {
			logger.error(e);
			throw new TransformerException(this,e);
		}
		return null;
	}
	
	public Object clone() throws CloneNotSupportedException {
        XlsToBeanTransformer clone = (XlsToBeanTransformer) super.clone();
        clone.setMappingBeans(mappingBeans);
        clone.setMappingFile(mappingFile);
        return clone;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public void setMappingBeans(Map mappingBeans) {
		this.mappingBeans = mappingBeans;
	}
}
