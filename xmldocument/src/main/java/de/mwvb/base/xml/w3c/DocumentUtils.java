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
 * @since 0.2.0
 */
public final class DocumentUtils {

	private DocumentUtils() {
	}

	/**
	 * org.w3c.dom.Document to XML String
	 * 
	 * @param w3cDoc Document to be converted to XML String
	 * @return XML String representation of w3cDoc, or null if w3cDoc is null
	 * @throws ParserConfigurationException -
	 * @throws TransformerException -
	 */
	public static String toXML(org.w3c.dom.Document w3cDoc) throws ParserConfigurationException, TransformerException {
		if (w3cDoc == null) {
			return null;
		}
		DOMSource source = new DOMSource(w3cDoc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, result);
		return writer.toString();
	}
}
