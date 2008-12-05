package org.mule.module.excel.config;

import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.config.spring.parsers.collection.ChildMapEntryDefinitionParser;
import org.mule.config.spring.parsers.specific.RouterDefinitionParser;
import org.mule.config.spring.parsers.specific.TransformerDefinitionParser;
import org.mule.routing.outbound.XlsRowMessageSplitter;
import org.mule.transformers.xls.BeanToXlsTransformer;
import org.mule.transformers.xls.XlsToBeanTransformer;

public class ExcelNamespaceHandler extends AbstractMuleNamespaceHandler {

	public void init() {
		registerBeanDefinitionParser("row-splitter",new RouterDefinitionParser(XlsRowMessageSplitter.class));
		registerBeanDefinitionParser("bean-to-xls-transformer",new TransformerDefinitionParser(BeanToXlsTransformer.class));
		registerBeanDefinitionParser("xls-to-bean-transformer",new TransformerDefinitionParser(XlsToBeanTransformer.class));
        registerBeanDefinitionParser("mapping-bean", new ChildMapEntryDefinitionParser("mappingBeans", "key", "value"));
	}
}
