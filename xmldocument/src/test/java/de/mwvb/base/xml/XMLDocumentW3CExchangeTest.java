package de.mwvb.base.xml;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;

import de.mwvb.base.xml.w3c.DocumentUtils;

/**
 * XMLDocument <-> W3C Document Exchange Test
 * 
 * @author Marcus Warm
 */
public class XMLDocumentW3CExchangeTest {
	private static final String XML = "<doc><a name=\"Roger\"/></doc>";

	@Test
	public void w3cToThis() throws Exception {
		// prepare
	    final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    final org.w3c.dom.Document w3cDoc = builder.parse(new ByteArrayInputStream(XML.getBytes()));
	    
	    // test this
	    final XMLDocument doc = new XMLDocument(w3cDoc);
	    
	    // verify
	    Assert.assertEquals(XML, shorten(doc.getXML()));
	}
	
	@Test
	public void thisToW3c() throws Exception {
		// prepare
		final XMLDocument doc = new XMLDocument(XML);
		
		// test this
		final org.w3c.dom.Document w3cDoc = doc.getW3CDocument();

	    // verify
		final String result = DocumentUtils.toXML(w3cDoc);
		Assert.assertEquals(XML, shorten(result));
	}
	
	private String shorten(final String xml) {
		return xml.substring(xml.indexOf("<doc"));
	}
}
