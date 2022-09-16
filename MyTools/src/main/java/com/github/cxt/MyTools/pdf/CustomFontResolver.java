package com.github.cxt.MyTools.pdf;

import org.apache.commons.io.FileUtils;
import org.xhtmlrenderer.css.value.FontSpecification;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.render.FSFont;
import com.itextpdf.text.pdf.BaseFont;

public class CustomFontResolver extends ITextFontResolver {

	public CustomFontResolver(SharedContext sharedContext) {
		super(sharedContext);
		if("linux".equals(System.getProperty("os.name").toLowerCase())){
			addFont("/usr/share/fonts/chiness/simsun.ttc");
		}else{
			addFont("c:/Windows/Fonts/simsun.ttc");
		}
	}

	@Override
	public FSFont resolveFont(SharedContext renderingContext, FontSpecification spec) {
		if(spec != null) {
			spec.families = new String[] {"SimSun"};
		}
		return super.resolveFont(renderingContext, spec);
	}
	
	
	private void addFont(String path) {
		try {
			addFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (Exception ignore) {
		
		}
	}
	

}
