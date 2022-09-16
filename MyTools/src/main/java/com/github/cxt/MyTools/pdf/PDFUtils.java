package com.github.cxt.MyTools.pdf;

import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

//html转pfd的删除先效果比较差。 代码位置可以通过搜索  IdentValue.LINE_THROUGH
public class PDFUtils {


    public static void html2pdf(String htmlContent, OutputStream out, File base) throws IOException {
        ITextRenderer renderer = new ITextRenderer();
        
        SharedContext sc = renderer.getSharedContext();
        ITextUserAgent uac = (ITextUserAgent) sc.getUserAgentCallback();
        
        
        sc.setUserAgentCallback(new CustomUserAgentCallback(uac));
        
        CustomFontResolver fontResolver = new CustomFontResolver(sc);
        sc.setFontResolver(fontResolver);
       
        renderer.setDocumentFromString(htmlContent);

        if(base != null) {
        	renderer.setDocumentFromString(htmlContent,  base.toURI().toString());
        }
        
        renderer.layout();
        try {
        	renderer.createPDF(out);
        }catch (DocumentException e) {
			throw new IOException(e);
		}
        finally {
        	IOUtils.closeQuietly(out);
		}

    }
    
}