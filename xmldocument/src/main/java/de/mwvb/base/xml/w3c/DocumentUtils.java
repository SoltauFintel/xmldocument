package de.mwvb.base.xml.w3c;

import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * org.w3c.dom.Document Utilities
 * 
 * @author Marcus Warm
 */
public class DocumentUtils {

	private DocumentUtils() {
	}

	/**
	 * org.w3c.dom.Document to XML String
	 */
	public static String toXML(org.w3c.dom.Document w3cDoc) throws ParserConfigurationException, TransformerException {
		final DOMSource source = new DOMSource(w3cDoc);
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, result);
		return writer.toString();
	}
}
