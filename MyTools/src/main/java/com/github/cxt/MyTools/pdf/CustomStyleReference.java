package com.github.cxt.MyTools.pdf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.xhtmlrenderer.context.StyleReference;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.newmatch.CascadedStyle;
import org.xhtmlrenderer.css.parser.PropertyValue;
import org.xhtmlrenderer.css.sheet.PropertyDeclaration;
import org.xhtmlrenderer.extend.UserAgentCallback;

public class CustomStyleReference extends StyleReference {

	public CustomStyleReference(UserAgentCallback userAgent) {
		super(userAgent);
	}

	@Override
	public CascadedStyle getCascadedStyle(Element e, boolean restyle) {
		Node parent = e.getParentNode();
		CascadedStyle  cascadedStyle = super.getCascadedStyle(e, restyle);
		
		if (parent instanceof Document) {
			PropertyValue value = new PropertyValue(CSSPrimitiveValue.CSS_STRING, "SimSun", "SimSun"); 
			value.setStringArrayValue(new String[] {"SimSun"});
			PropertyDeclaration p = new PropertyDeclaration(CSSName.FONT_FAMILY, value, false, 2);
			cascadedStyle = CascadedStyle.createLayoutStyle(cascadedStyle, new PropertyDeclaration[] {p});
		}
		return cascadedStyle;
	}


	
}
