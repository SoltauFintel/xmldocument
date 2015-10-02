package de.mwvb.base.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * XML document
 * 
 * <p>Wrapper of DOM4J for easy DOM- and XPath-based XML access.
 * 
 * <p>Advantages:<ul>
 * <li>easy navigation through XML document</li>
 * <li>getValue(attributeName) returns "" if attribute does not exist</li>
 * <li>add(elementName) creates and appends new XML element in one operation</li>
 * <li>easy XMLDocument to/from XML conversions</li>
 * <li>many ways to save and load XMLDocument</li>
 * <li>easy XPath access using selectNodes() or selectSingleNode()</li>
 * <li>and some more special functions.</li>
 * </ul>
 * 
 * @author Marcus Warm
 * @since  2008
 */
public class XMLDocument {
	private Document doc;
	
	/**
	 * Default constructor
	 * <p>Document will not be initialized.
	 */
	public XMLDocument() {
	}

	/**
	 * XML String constructor
	 * 
	 * @param xml valid XML String
	 */
	public XMLDocument(final String xml) {
		if (xml == null) {
			throw new IllegalArgumentException("XMLDocument argument xml must not be null!");
		}
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Load XML file constructor
	 * 
	 * @param isResource true if it is a resource file in a package,<br/>false if it is a file in file system
	 * @param fileName file name incl. path
	 * <br/>File name can begin with "file:" or "http:". In that case the isResource option will be ignored.
	 */
	public XMLDocument(final boolean isResource, final String fileName) {
		if (fileName.startsWith("file:") || fileName.startsWith("http:")) {
			try {
				URL url = new URL(fileName);
				loadStream(url.openConnection().getInputStream());
			} catch (Throwable e) {
				throw new RuntimeException("Error loading XML file '" + fileName + "'!", e);
			}
		} else if(isResource) {
			loadResource(fileName);
		} else {
			loadFile(fileName);
		}
	}

	/**
	 * Load XML file constructor
	 * 
	 * @param file file in file system
	 */
	public XMLDocument(final File file) {
		this(false, file.getPath());
	}
	
	/**
	 * XML stream constructor
	 * 
	 * @param stream InputStream
	 */
	public XMLDocument(final InputStream stream) {
		loadStream(stream);
	}

	/**
	 * Load XML file constructor
	 * 
	 * @param clazz        class to get package path 
	 * @param resourceName file name of the resource without path
	 */
	public XMLDocument(final Class<?> clazz, final String resourceName) {
		final char slash = '/';
		final String path = clazz.getPackage().getName().replace('.', slash);
		loadResource(slash + path + slash + resourceName);
	}

	/**
	 * Load XML file
	 * 
	 * @param fileName name of file in file system
	 * @return XMLDocument
	 */
	public static XMLDocument load(final String fileName) {
		final XMLDocument ret = new XMLDocument();
		ret.loadFile(fileName);
		return ret;
	}

	/**
	 * Load XML file
	 * 
	 * @param fileName name of file in file system
	 * @return XML String
	 */
	public static String loadXML(final String fileName) {
		final XMLDocument ret = new XMLDocument();
		ret.loadFile(fileName);
		return ret.getXML();
	}

	/**
	 * Load XML file
	 * 
	 * @param fileName name of in file system
	 */
	public void loadFile(final String fileName) {
		try {
			final SAXReader r = new SAXReader();
			doc = r.read(fileName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Load XML file
	 * 
	 * @param resourceName
	 */
	public void loadResource(final String resourceName) {
		final InputStream stream = getClass().getResourceAsStream(resourceName);
		if (stream == null) {
			throw new RuntimeException("Error loading resource file '" + resourceName + "'!");
		}
		loadStream(stream); // <- closes stream
	}
	
	/**
	 * Load XML file
	 * 
	 * @param stream InputStream
	 */
	public void loadStream(final InputStream stream) {
		try {
			final SAXReader r = new SAXReader();
			doc = r.read(stream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
	
	/**
	 * Save XML document to file using pretty print format
	 * 
	 * @param fileName name of file in file system
	 */
	public void saveFile(final String fileName) {
		saveFile(fileName, OutputFormat.createPrettyPrint());
	}

	/**
	 * Save XML document to file using compact format
	 * 
	 * @param fileName name of file in file system
	 */
	public void saveFileCompact(final String fileName) {
		saveFile(fileName, OutputFormat.createCompactFormat());
	}
	
	private void saveFile(final String fileName, final OutputFormat format) {
        try {
			final FileWriter writer = new FileWriter(fileName);
			try {
				format.setEncoding(getEncoding());
				new XMLWriter(writer, format).write(doc);
			} finally {
				writer.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getEncoding() {
		return "windows-1252";
	}

	/**
	 * @return true if document is valid and root element has child elements
	 */
	public boolean isOkay() {
		return doc != null
			&& doc.getRootElement() != null
			&& doc.getRootElement().elements() != null
			&& doc.getRootElement().elements().size() > 0;
	}
	
	/**
	 * @return root element, null if document was not initialized
	 */
	public XMLElement getElement() {
		return doc == null ? null : XMLElementImpl.create(doc.getRootElement());
	}
	
	/**
	 * Returns XML child elements
	 * 
	 * @return XMLElement list
	 */
	public List<XMLElement> getChildren() {
		return XMLElementImpl.getChildElements(doc.getRootElement().elements());
	}
	
	/**
	 * XML element selection using XPath (Dokumentebene)
	 * <p>An exception will be thrown if the XPath statement is incorrect.
	 * 
	 * @param pXPath XPath String, e.g. "//addresses/person[@surname='Doe']"
	 * @return XMLElement Liste
	 */
	public List<XMLElement> selectNodes(final String pXPath) {
		return XMLElementImpl.getChildElements(doc.selectNodes(pXPath));
	}
	
	/**
	 * XML element selection using XPath (Dokumentebene)
	 * <p>An exception will be thrown if the XPath statement is incorrect.
	 * 
	 * @param pXPath XPath String, e.g. "//addresses/person[@id='4711']"
	 * <br/>The XPath String should deliver only one element.
	 * @return XMLElement or null if no element was found
	 */
	public XMLElement selectSingleNode(final String pXPath) {
		final Node node = doc.selectSingleNode(pXPath);
		if (node == null) {
			return null;
		} else {
			return XMLElementImpl.create((Element) node);
		}
	}
	
	/**
	 * Returns a element which has the given value in attribute "id".
	 * It is assumed that there is only one element with that id.
	 * 
	 * @param id id value
	 * @return XMLElement or null if no element was found
	 */
	public XMLElement byId(final String id) {
		return selectSingleNode("//*[@id='" + id + "']");
	}
	
	/**
	 * Removes a non-root-element in the whole document with given value in attribute "id".
	 * 
	 * @param id id value
	 * @return true: element was removed, false: element was not found
	 */
	public boolean removeChildById(final String id) {
		final String xpath = "*[@id='" + id + "']";
		XMLElement p = selectSingleNode("//" + xpath + "/..");
		if (p != null) {
			p.removeChildren(xpath);
		}
		return p != null;
	}

	/**
	 * @return XML String
	 */
	public String getXML() {
		return doc.asXML();
	}
	
	@Override
	public String toString() {
		return getXML();
	}
}
