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
	 * Returns name of element.
	 */
	String getName();

	/**
	 * @param name new element name
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
	 */
	String getAttributeName(int index);
	
	/**
	 * Sets value of given attribute.
	 */
	void setValue(String attributeName, String value);
	
	/**
	 * Sets value of given attribute. If value is null there will be no operation.
	 */
	void setValueIfNotNull(String attributeName, String value);
	
	/**
	 * Sets multi line value of given attribute.
	 * <p>This removes all "\r" and translates all "\n" to NEWLINE.
	 */
	void setMultiLineValue(String attributeName, String multiLineValue);
	
	/**
	 * Returns multi line value of given attribute.
	 * <p>This translates all NEWLINE to "\n".
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
	 * @return XMLElement Liste
	 */
	List<XMLElement> selectNodes(String pXPath);

	/**
	 * XML element selection using XPath (Dokumentebene)
	 * <p>An exception will be thrown if the XPath statement is incorrect.
	 * 
	 * @param pXPath XPath String, e.g. "//addresses/person[@id='4711']"
	 * <br/>The XPath String should deliver only one element.
	 * @return XMLElement or null if no element was found
	 */
	XMLElement selectSingleNode(String pXPath);

	/**
	 * @return inner text of element and all child elements
	 */
	String getText();

	/**
	 * Sets inner text of element.
	 */
	void setText(String text);

	/**
	 * Creates a new XML element with given element name.
	 * The new XML element will be appended to this element.
	 * 
	 * @return new XMLElement
	 */
	XMLElement add(String elementName);

	/**
	 * Creates a new XML element with given element name and given inner text.
	 * The new XML element will be appended to this element.
	 * <p>This is a combination of add(elementName) and setText(text).
	 * 
	 * @return new XMLElement
	 */
	XMLElement add(String elementName, String text);

	/**
	 * Returns an array of all values of the given attribute of all child elements.
	 * <br>If an child element has not the attribute the value of "" will be added
	 * to the returned array.
	 */
	List<String> getArray(String attributeName);

	/**
	 * Returns a map of all attribute name/value pairs of this element.
	 * <br>There is no special order of the map members.
	 * <br>Inner text and attributes of child elements will not be added to the returned map.
	 */
	Map<String, String> getMap();
	
	/**
	 * Appends XML String as a child to this element.
	 * This can help to add a XML document to this XML document.
	 */
	void append(String xmlString);

	/**
	 * Removes all direct child elements using element name.
	 * 
	 * @param elementName Strictly speaking this is a selectNodes argument.
	 * So it could be XPath, e.g. "person[@city='Hamburg']"
	 */
	void removeChildren(String elementName);

	/**
	 * Inserts a new created XML element before a XML element.
	 * 
	 * @param beforeIndex index from 0 of the existing element 
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
	 */
	void removeAttribute(String attributeName);
	
	/**
	 * Returns number of child elements.
	 */
	int getChildrenCount();

	/**
	 * @return true wenn Kindelemente vorhanden sind, sonst false
	 */
	boolean hasChildren();
	
	/**
	 * @return true if attribute exists, otherwise false
	 */
	boolean hasAttribute(String attributeName);
	
	/**
	 * Removes all empty attributes.
	 */
	void removeEmptyAttributes();
}
