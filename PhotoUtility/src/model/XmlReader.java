package model;

import java.beans.Beans;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 取り急ぎ作ったきったないコードなので後で書き直す
 *
 */
public class XmlReader extends Beans {

	/**
	 * それぞれの保存先のベースとなるパス
	 */
	private String jpgBasePath;
	private String rawBasePath;
	private String movBasePath;

	public void domRead(String file) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);

		Element root = document.getDocumentElement();

		// ルート要素の子ノードを取得する
		NodeList rootChildren = root.getChildNodes();

		System.out.println("子要素の数：" + rootChildren.getLength());
		System.out.println("------------------");

		for (int i = 0; i < rootChildren.getLength(); i++) {
			Node node = rootChildren.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getNodeName().equals("type")) {
					if (element.getAttribute("name").equals("jpg")) {
						// jpg設定取得
						NodeList personChildren = node.getChildNodes();
						for (int j = 0; j < personChildren.getLength(); j++) {
							Node personNode = personChildren.item(j);
							if (personNode.getNodeType() == Node.ELEMENT_NODE) {
								if (personNode.getNodeName().equals("path")) {
									this.jpgBasePath = personNode.getTextContent();
								}
							}
						}
					} else if (element.getAttribute("name").equals("raw")) {
						// raw設定取得
						NodeList personChildren = node.getChildNodes();
						for (int j = 0; j < personChildren.getLength(); j++) {
							Node personNode = personChildren.item(j);
							if (personNode.getNodeType() == Node.ELEMENT_NODE) {
								if (personNode.getNodeName().equals("path")) {
									this.rawBasePath = personNode.getTextContent();
								}
							}
						}
					} else if (element.getAttribute("name").equals("mov")) {
						// mov設定取得
						NodeList personChildren = node.getChildNodes();
						for (int j = 0; j < personChildren.getLength(); j++) {
							Node personNode = personChildren.item(j);
							if (personNode.getNodeType() == Node.ELEMENT_NODE) {
								if (personNode.getNodeName().equals("path")) {
									this.movBasePath = personNode.getTextContent();
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * それぞれの保存先のベースとなるパスを取得します。
	 * @return それぞれの保存先のベースとなるパス
	 */
	public String getJpgBasePath() {
	    return jpgBasePath;
	}

	/**
	 * rawBasePathを取得します。
	 * @return rawBasePath
	 */
	public String getRawBasePath() {
	    return rawBasePath;
	}

	/**
	 * movBasePathを取得します。
	 * @return movBasePath
	 */
	public String getMovBasePath() {
	    return movBasePath;
	}


}
