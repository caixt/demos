package com.github.cxt.MyTools.pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.ICssApplierFactory;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.FontStyles;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.node.IStylesContainer;
import com.itextpdf.styledxmlparser.node.impl.jsoup.node.JsoupElementNode;


public class PDFUtils {
	
	private static FontProvider fontProvider = new FontProvider();
	
	
	static {
		FontProgramFactory.registerSystemFontDirectories();
		try {
			FontProgram  font = FontProgramFactory.createRegisteredFont("simsun");
			if(font!= null) {
				fontProvider.addFont(font);
			}
		} catch (IOException e) {
			System.err.println("找不到对应的字体");
		}
		try {
			FontProgram  font = FontProgramFactory.createRegisteredFont("simsun", FontStyles.BOLD);
			if(font!= null) {
				fontProvider.addFont(font);
			}
		} catch (IOException e) {
			System.err.println("找不到对应的字体");
		}
	}
	
	public static void html2pdf(String htmlContent, OutputStream out, File base) {
		htmlToPDF(new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8"))), out, base);
	}
	
	public static void htmlToPDF(InputStream htmlInputStream, OutputStream out) {
		htmlToPDF(PageSize.A4, htmlInputStream, out, null);
	}
	
	public static void htmlToPDF(InputStream htmlInputStream, OutputStream out, File base) {
		htmlToPDF(PageSize.A4, htmlInputStream, out, base);
	}
	
	public static void htmlToPDF(PageSize pageSize, InputStream htmlInputStream, OutputStream out, File base) {
		try {
			PdfWriter pdfWriter = new PdfWriter(out);
	        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
	        pdfDocument.setDefaultPageSize(pageSize);
	
	        ConverterProperties pro = new ConverterProperties();
        	pro.setFontProvider(fontProvider);
        	if(base != null) {
        		pro.setBaseUri(Paths.get(base.getPath()).toUri().toURL().toExternalForm());
        	}
        	pro.setCssApplierFactory(new ICssApplierFactory() {
				
				@Override
				public ICssApplier getCssApplier(IElementNode tag) {
					ICssApplier iccApplier = DefaultCssApplierFactory.getInstance().getCssApplier(tag);
					if(iccApplier == null) {
						return null;
					}
					return new GrayBackgroundBlockCssApplier(iccApplier);
				}
			});
	        HtmlConverter.convertToPdf(htmlInputStream, pdfDocument, pro);
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("生成pdf失败" + e.getMessage());
		}
	}
	
	static class GrayBackgroundBlockCssApplier implements ICssApplier {
		
		ICssApplier iccApplier;
		
		GrayBackgroundBlockCssApplier(ICssApplier iccApplier){
			this.iccApplier = iccApplier;
		}
		
		
		public void before(JsoupElementNode node) {
			Map<String, String> cssProps = node.getStyles();
			String value = node.getAttribute(CssConstants.HEIGHT);
	    	if(StringUtils.isNotBlank(value)) {
	    		cssProps.put(CssConstants.HEIGHT, StringUtils.substringBeforeLast(value, ";"));
	    	}
	    	value = node.getAttribute(CssConstants.WIDTH);
	    	if(StringUtils.isNotBlank(value)) {
	    		cssProps.put(CssConstants.WIDTH, StringUtils.substringBeforeLast(value, ";"));
	    	}
		}
		
	    public void apply(ProcessorContext context,
	        IStylesContainer stylesContainer, ITagWorker tagWorker){
	    	if(stylesContainer instanceof JsoupElementNode) {
	    		before((JsoupElementNode) stylesContainer);
	    	}
	    	iccApplier.apply(context, stylesContainer, tagWorker);
	    }
	}
}
