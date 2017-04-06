package de.mwvb.base.xml;

import java.util.List;
import java.util.Map;

/**
 * XML element interface for DOM based XML access
 *  
 * @author Marcus Warm
 * @since  2008
 */
public interface XMLElement {
	String NEWLINE = "[^NEWLINE^]";

	/**
	 * Returns value of given attribute.
	 * 
	 * @param attributeName name of the attribute
	 * @return content of attribute
	 * <br>- or "" if attributeName was not found
	 * <br>Return value will never be null.
	 */
	String getValue(String attributeName);

	/**
	 * Returns all child elements.
	 * 
	 * @return XMLElement list
	 */
	List<XMLElement> getChildren();

	/**
	 * Returns name of this element.
	 * 
	 * @return tag name
	 */
	String getName();

	/**
	 * Changes name of this element.
	 * 
	 * @param name new tag name
	 */
	void setName(String name);

	/**
	 * Returns number of attributes of this element.
	 * 
	 * @return 0 or more
	 */
	int getAttributeCount();
	
	/**
	 * Returns name of given attribute.
	 * Throws exception if index is wrong.
	 * 
	 * @param index attribute index from 0
	 * @return attribute name
	 */
	String getAttributeName(int index);
	
	/**
	 * Sets value of given attribute. If value is null the attribute is removed.
	 * 
	 * @param attributeName attribute name
	 * @param value new attribute value
	 */
	void setValue(String attributeName, String value);
	
	/**
	 * Sets value of given attribute. If value is null there will be no operation.
	 * 
	 * @param attributeName attribute name
	 * @param value new attribute value
	 */
	void setValueIfNotNull(String attributeName, String value);
	
	/**
	 * Sets multi line value of given attribute.
	 * 
	 * @param attributeName attribute name
	 * @param multiLineValue attribute value with all "\r" removed and all "\n" translated to NEWLINE
	 * @deprecated This method could be removed in a future release. New code should not use it any more.
	 */
	void setMultiLineValue(String attributeName, String multiLineValue);
	
	/**
	 * Returns multi line value of given attribute.
	 * 
	 * @param attributeName attribute name
	 * @return attribute value with all NEWLINE translated to "\n"
	 * @deprecated This method could be removed in a future release. New code should not use it any more.
	 */
	String getMultiLineValue(String attributeName);
	
	/**
	 * @return XML String
	 */
	String getXML();

	/**
	 * XML element selection using XPath (Dokumentebene)
	 * <p>An exception will be thrown if the XPath statement is incorrect.
	 * 
	 * @param pXPath XPath String, e.g. "//addresses/person[@surname='Doe']"
	 * <p>"//abc" searches in the whole document. ".//abc" searches below the element.</p>
	 * @return XMLElement Liste
	 */
	List<XMLElement> selectNodes(String pXPath);

	/**
	 * XML element selection using XPath (Dokumentebene)
	 * <p>An exception will be thrown if the XPath statement is incorrect.
	 * 
	 * @param pXPath XPath String, e.g. "//addresses/person[@id='4711']"
	 * <br>The XPath String should deliver only one element.
	 * @return XMLElement or null if no element was found
	 */
	XMLElement selectSingleNode(String pXPath);

	/**
	 * @return inner text of element and all child elements
	 */
	String getText();

	/**
	 * Sets inner text of element.
	 * 
	 * @param text inner text, must not be null
	 */
	void setText(String text);

	/**
	 * Creates a new element with given element name.
	 * The new element will be appended to this element.
	 * 
	 * @param elementName name of new element
	 * @return new XMLElement
	 */
	XMLElement add(String elementName);

	/**
	 * Creates a new element with given element name and given inner text.
	 * The new element will be appended to this element.
	 * <p>This is a combination of add(elementName) and setText(text).
	 * 
	 * @param elementName name of new element
	 * @param text inner text, must not be null
	 * @return new XMLElement
	 */
	XMLElement add(String elementName, String text);

	/**
	 * Returns an array of all values of the given attribute of all child elements.
	 * <br>If an child element has not the attribute the value of "" will be added
	 * to the returned array.
	 * 
	 * @param attributeName attribute name
	 * @return list of attribute values of all child elements
	 */
	List<String> getArray(String attributeName);

	/**
	 * Returns a map of all attribute name/value pairs of this element.
	 * <br>There is no special order of the map members.
	 * <br>Inner text and attributes of child elements will not be added to the returned map.
	 * 
	 * @return name/value Map for all attributes of this element
	 */
	Map<String, String> getMap();
	
	/**
	 * Appends XML String as a child to this element.
	 * This can help to add a XML document to this XML document.
	 * 
	 * @param xmlString valid XML String
	 */
	void append(String xmlString);

	/**
	 * @param index is the index of the child element before the pXML element is inserted
	 * @param xml to be inserted XML
	 */
	void insertXMLBefore(int index, String xml);
	
	/**
	 * Removes all direct child elements using element name.
	 * 
	 * @param elementName Strictly speaking this is a selectNodes argument.
	 * So it could be XPath, e.g. "person[@city='Hamburg']"
	 */
	void removeChildren(String elementName);

	/**
	 * Removes child elements with index startIndex to endIndex (incl.).
	 * If you have stored the children, you must refresh that list.
	 * 
	 * @param startIndex 0 based index
	 * @param endIndex 0 based index. Must not be smaller than startIndex
	 */
	void removeChildren(int startIndex, int endIndex);
	
	/**
	 * Inserts a new created XML element before a XML element.
	 * 
	 * @param beforeIndex index from 0 of the existing element
	 * @param newElementName new element name
	 * @return new XMLElement
	 */
	XMLElement insertBefore(int beforeIndex, String newElementName);

	/**
	 * @param elementName name of the searched XML element
	 * @param start index from 0
	 * @return index of the first found element with name elementName
	 * <br>or -1 if not found
	 */
	int indexByName(String elementName, int start);

	/**
	 * Removes given attribute.
	 * 
	 * @param attributeName attribute name
	 */
	void removeAttribute(String attributeName);
	
	/**
	 * Returns number of child elements.
	 * 
	 * @return number of children
	 */
	int getChildrenCount();

	/**
	 * @return true if child elements exist, otherwise false
	 */
	boolean hasChildren();
	
	/**
	 * @param attributeName attribute name
	 * @return true if attribute exists in this element, otherwise false
	 */
	boolean hasAttribute(String attributeName);
	
	/**
	 * Removes all empty attributes.
	 */
	void removeEmptyAttributes();

	/**
	 * <p>Adds CDATA node to element and sets content.</p>
	 * <p>If cdata contains the CDATA end string "]]&gt;" the string will be split. This function is "]]&gt;" safe.</p>
	 * <p>Read the CDATA content with getText()!</p>
	 * 
	 * @param cdata content of new CDATA node
	 */
	void setCdata(String cdata);

	/**
	 * Adds a new element and sets many attributes.
	 * 
	 * @param elementname name of new element
	 * @param attr even number of arguments, 1st argument is the attribute name, 2nd argument is the attribute value
	 * @return new XMLElement
	 */
	XMLElement addWithAttributes(String elementname, String... attr);
	
	/**
	 * Gets child element. If it not exist it will be created and returned.
	 * 
	 * @param elementname name of the child element
	 * @return already existing or new created XMLElement
	 */
	XMLElement getOrAdd(String elementname);
	
	/**
	 * Returns the XPath expression which will return a node set containing the
	 * given node such as /a/b/@c. No indexing will be used to restrict the path
	 * if multiple elements with the same name occur on the path.
	 */
	public String getPath();
	
	/**
	 * Returns the relative XPath expression which will return a node set
	 * containing the given node such as a/b/@c. No indexing will be used to
	 * restrict the path if multiple elements with the same name occur on the
	 * path.
	 * 
	 * @param context
	 *            is the parent context from which the relative path should
	 *            start. If the context is null or the context is not an
	 *            ancestor of this node then the path will be absolute and start
	 *            from the document and so begin with the '/' character.
	 */
	public String getPath(XMLElement context);

	/**
	 * Returns parent element. Same as selectSingleNode("..").
	 * 
	 * @return XMLElement, is null if the element is the root element.
	 */
	XMLElement getParent();
	
	/**
	 * Returns the root element of the document.
	 * 
	 * @return not null
	 */
	XMLElement getRoot();
}
