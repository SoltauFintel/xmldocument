# XMLDocument #

Wrapper of [DOM4J](https://github.com/dom4j/dom4j) for easy DOM- and XPath-based XML access.

Advantages:
* easy navigation through XML document
* getValue(attributeName) returns "" if attribute does not exist
* add(elementName) creates and appends new XML element in one operation
* easy XMLDocument to/from XML conversions
* many ways to save and load XMLDocument
* easy XPath access using selectNodes() or selectSingleNode()
* and some more special functions.

Use with Gradle:
<pre>compile 'de.mwvb:xmldocument:0.2.3'</pre>

Use with Maven:
<pre>&lt;dependency>
    &lt;groupId>de.mwvb&lt;/groupId>
    &lt;artifactId>xmldocument&lt;/artifactId>
    &lt;version>0.2.3&lt;/version>
&lt;/dependency></pre>
