package de.mwvb.base.xml;

import org.junit.Assert;
import org.junit.Test;

public class XMLElementTest {

	@Test
	public void testCdata1() {
		XMLDocument dok = new XMLDocument("<test/>");
		try {
			XMLElement root = dok.getElement();

			XMLElement e = root.add("withCDATA");
			e.setCdata("This is &a test.");

			Assert.assertEquals("XML is wrong!", "<test><withCDATA><![CDATA[This is &a test.]]></withCDATA></test>",
					root.getXML());
			Assert.assertEquals("There must be no attributes or child elements!", 0,
					e.getChildrenCount() + e.getAttributeCount());
		} finally {
			dok.close();
		}
	}

	@Test
	public void testCdata2() {
		XMLDocument dok = new XMLDocument("<test><a/></test>");
		try {
			XMLElement root = dok.getElement();

			XMLElement e = root.add("withCDATA");
			e.setCdata("This is a test.");

			String cdata = dok.selectSingleNode("//withCDATA").getText();
			Assert.assertEquals("getting CDATA does not work!", "This is a test.", cdata);
			Assert.assertEquals("If there is no CDATA node, then \"\" should be returned!", "",
					dok.selectSingleNode("//a").getText());
			Assert.assertEquals("CDATA content of sub-elements must not be returned!", "", root.getText());

			e.setCdata(" & <more/> ]]>text.");
			cdata = dok.selectSingleNode("//withCDATA").getText();
			Assert.assertEquals("getting CDATA does not work!", "This is a test. & <more/> ]]>text.", cdata);
			new XMLDocument(dok.getElement().getXML()) // Test whether the XML can be parsed.
				.close();
			Assert.assertTrue("']]>' split test failed", dok.getElement().getXML().contains("![CDATA[>text"));
		} finally {
			dok.close();
		}
	}

	@Test
	public void testAddManyAttributes() {
		XMLDocument dok = new XMLDocument("<test/>");
		try {
			XMLElement root = dok.getElement();
	
			XMLElement e = root.addWithAttributes("properties", "attr1", "value1", "attr2", "value2");
			e.setValue("a", "b");
			
			Assert.assertEquals(3, root.selectSingleNode("properties").getAttributeCount());
		} finally {
			dok.close();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddManyAttributes_odd() {
		XMLDocument dok = new XMLDocument("<test/>");
		try {
			XMLElement root = dok.getElement();
	
			root.addWithAttributes("properties", "attr1"); // error: odd number of attrs-arguments
		} finally {
			dok.close();
		}
	}
	
	@Test
	public void testEquals() {
		String xml = "<doc><X><Y id='1' data='abc'/></X></doc>";
		XMLDocument dok = new XMLDocument(xml);
		XMLElement y;
		try {
			y = dok.selectSingleNode("//Y");
			Assert.assertTrue("Test 1 failed", dok.getChildren().get(0).getChildren().get(0).equals(y));
			Assert.assertFalse("Test 2 failed", dok.getElement().equals(y));
		} finally {
			dok.close();
		}
		XMLDocument dok2 = new XMLDocument(xml);
		try {
			XMLElement y2 = dok2.selectSingleNode("//Y");
			// It works only if the elements come from the same XMLDocument.
			Assert.assertFalse("Test 3 failed", y.equals(y2));
		} finally {
			dok2.close();
		}
	}
	
	@Test
	public void testGetRoot() {
		XMLDocument dok = new XMLDocument("<R><A><B><C><D id='1'/><D id='2'><below/></D></C></B></A></R>");
		try {
			XMLElement e = dok.byId("2");
			XMLElement root = dok.getElement();
			Assert.assertTrue("Test '2' failed", root.equals(e.getRoot()));
			Assert.assertTrue("root-Test failed", root.equals(root.getRoot()));
			Assert.assertTrue("1st children Test failed", root.equals(root.getChildren().get(0).getRoot()));
		} finally {
			dok.close();
		}
	}
}
