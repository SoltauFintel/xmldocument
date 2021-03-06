package de.mwvb.base.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Internal XML element implementation for DOM based XML access
 * 
 * @author Marcus Warm
 * @since  2008
 */
class XMLElementImpl implements XMLElement {
	private final Element element;

	XMLElementImpl(final Element pElement) { 
		element = pElement;
	}
	
	@Override
	public String getName() {
		return element.getName();
	}
	
	@Override
	public void setName(final String name) {
		element.setName(name);
	}
	
	@Override
	public String getValue(final String pAttributname) {
		final String ret = element.attributeValue(pAttributname);
		return ret == null ? "" : ret;
	}
	
	@Override
	public String getMultiLineValue(final String pAttributname) {
		return getValue(pAttributname).replace(NEWLINE, "\n");
	}
	
	@Override
	public void setValue(final String pAttributname, final String pValue) {
		element.addAttribute(pAttributname, pValue);
	}
	
	@Override
	public void setMultiLineValue(final String pAttributname, final String pValue) {
		if (pValue == null) {
			setValue(pAttributname, pValue);
		} else {
			setValue(pAttributname, 
					pValue.replace("\r", "").replace("\n", NEWLINE));
		}
	}

	@Override
	public void setValueIfNotNull(final String attributeName, final String value) {
		if (value != null) {
			element.addAttribute(attributeName, value);
		}
	}
	
	@Override
	public List<XMLElement> getChildren() {
		return getChildElements(element.elements());
	}
	
	@Override
	public int getChildrenCount() {
		return element.elements().size();
	}
	
	@Override
	public boolean hasChildren() {
		return element.elements().size() > 0;
	}
	
	static List<XMLElement> getChildElements(final List<?> list) {
		final List<XMLElement> ret = new ArrayList<XMLElement>();
		for (Object e : list) {
			ret.add(create((Element) e));
		}
		return ret;
	}
	
	@Override
	public String getXML() {
		return element.asXML();
	}
	
	@Override
	public List<XMLElement> selectNodes(final String pXPath) {
		return getChildElements(element.selectNodes(pXPath));
	}
	
	@Override
	public XMLElement selectSingleNode(final String pXPath) {
		final Node node = element.selectSingleNode(pXPath);
		if (node == null || !(node instanceof Element)) {
			return null;
		} else {
			return create((Element) node);
		}
	}

	@Override
	public int getAttributeCount() {
		return element.attributeCount();
	}
	
	@Override
	public String getAttributeName(final int pIndex) {
		return element.attribute(pIndex).getName();
	}
	
	@Override
	public String getText() {
		return element.getText();
	}

	@Override
	public void setText(final String pText) {
		element.setText(pText);
	}
	
	@Override
	public XMLElement add(final String pElementName) {
		return create(element.addElement(pElementName));
	}
	
	@Override
	public XMLElement add(final String elementName, final String text) {
		final XMLElement newElement = add(elementName);
		newElement.setText(text);
		return newElement;
	}
	
	@Override
	public List<String> getArray(final String pAttributName) {
		final List<String> array = new ArrayList<String>();
		for (Iterator<?> iter = getChildren().iterator(); iter.hasNext();) {
			final XMLElement e = (XMLElement) iter.next();
			array.add(e.getValue(pAttributName));
		}
		return array;
	}
	
	@Override
	public Map<String, String> getMap() {
		final Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < element.attributeCount(); i++) {
			Attribute attr = element.attribute(i);
			map.put(attr.getName(), attr.getValue());
		}
		return map;
	}
	
	@Override
	public void append(final String pXML) {
		try {
			final Document doc = DocumentHelper.parseText(pXML);
			element.add(doc.getRootElement());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insertXMLBefore(int index, final String pXML) {
		try {
			final Document doc = DocumentHelper.parseText(pXML);
			Element ele = doc.getRootElement();
			element.content().add(index, ele);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void removeChildren(int von, int bis) {
		for (int i = element.elements().size() - 1; i >= 0; i--) {
			if (i >= von && i <= bis) {
				element.elements().remove(i);
			}
		}
	}

	@Override
	public void removeChildren(final String pElementName) {
		final List<?> list = element.selectNodes(pElementName);
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			element.remove((Element) iter.next());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public XMLElement insertBefore(final int pBeforeIndex, final String pNewElementName) {
		int myBeforeIndex = pBeforeIndex;
		final Element neu = new DocumentFactory().createElement(pNewElementName);
		int newIndex = -1;
		final List<?> c = element.content();
		for (int i = 0; i < c.size(); i++) {
			final String n = c.get(i).getClass().getName();
			if (n.endsWith("Element")) {
				newIndex++;
				if (newIndex == myBeforeIndex) {
					myBeforeIndex = i;
					break;
				}
			}
		}
		element.content().add(myBeforeIndex, neu);
		return create(neu);
	}

	@Override
	public int indexByName(final String pElementName, final int pStart) {
		final List<?> children = element.elements();
		for (int i = pStart; i < children.size(); i++) {
			if (((Element) children.get(i)).getName().equals(pElementName)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void removeAttribute(final String attributeName) {
		try {
			element.remove(element.attribute(attributeName));
		} catch (Exception ignored) { 
		}
	}
	
	static XMLElement create(final Element pElement) {
		return new XMLElementImpl(pElement);
	}
	
	@Override
	public boolean hasAttribute(final String pAttributname) {
		return element.attributeValue(pAttributname) != null;
	}

	@Override
	public void removeEmptyAttributes() {
		final List<String> zuLoeschendeAttribute = new ArrayList<String>();
		for (int i = 0; i < getAttributeCount(); i++) {
			String name = getAttributeName(i);
			if ("".equals(getValue(name))) {
				zuLoeschendeAttribute.add(name);
			}
		}
		for (String name : zuLoeschendeAttribute) {
			removeAttribute(name);
		}
	}

	@Override
	public void setCdata(final String cdata) {
		final String cdataEndString = "]]>";
		int start = 0;
		int o = cdata.indexOf(cdataEndString);
		while (o >= 0) {
			o += 2;
			element.addCDATA(cdata.substring(start, o));
			start = o;

			o = cdata.indexOf(cdataEndString, start);
		}
		element.addCDATA(start == 0 ? cdata : cdata.substring(start));
	}

	@Override
	public XMLElement addWithAttributes(String elementname, String... attr) {
		if (attr.length % 2 != 0) {
			throw new IllegalArgumentException("The number of attr-arguments must be even!");
		}
		XMLElement e = add(elementname);
		for (int i = 0; i < attr.length; i += 2) {
			e.setValue(attr[i], attr[i + 1]);
		}
		return e;
	}
	
	@Override
	public XMLElement getOrAdd(String elementname) {
		XMLElement ret = selectSingleNode("*[name()='" + elementname + "']");
		return ret == null ? add(elementname) : ret;
	}

	@Override
	public String getPath() {
		return element.getPath();
	}

	@Override
	public String getPath(XMLElement context) {
		return element.getPath(((XMLElementImpl) context).element);
	}
	
	@Override
	public XMLElement getParent() {
		return selectSingleNode("..");
	}
	
	@Override
	public XMLElement getRoot() {
		XMLElement pick = this;
		while (pick.getParent() != null) {
			pick = pick.getParent();
		}
		return pick;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XMLElementImpl other = (XMLElementImpl) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		return true;
	}
	
	/**
	 * Implementation may change.
	 */
	@Override
	public String toString() {
		return getName() + getMap().toString();
	}
}
