package com.github.cxt.MyTools.pdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;


public class PDFUtils2 {

	public static void htmlToPDF(InputStream htmlInputStream, OutputStream out) {
		try {
			// 创建一个document对象实例
//			Document document = new Document(new com.itextpdf.text.RectangleReadOnly(842F, 595F));
			Document document = new Document();

			// 为该Document创建一个Writer实例
			PdfWriter pdfwriter = PdfWriter.getInstance(document, out);
			pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
			// 打开当前的document
			document.open();
			// BASE64图片处理
			final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
			tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
			tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);
			final CssFilesImpl cssFiles = new CssFilesImpl();
			cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
			final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
			final HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(new MyFontsProvider()));
			hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);

			final HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, pdfwriter));
			final Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

			final XMLWorker worker = new XMLWorker(pipeline, true);
			final Charset charset = Charset.forName("UTF-8");
			final XMLParser xmlParser = new XMLParser(true, worker, charset);

			xmlParser.parse(htmlInputStream, charset);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static class MyFontsProvider extends XMLWorkerFontProvider {

		public MyFontsProvider() {
			super(null, null);
		}

		@Override
		public Font getFont(final String fontname, String encoding, float size, final int style) {
			String fntname = fontname;
			if (fntname == null) {
				fntname = "宋体";// windows下
				// fntname = "fontFile/simsun.ttf";//linux系统下
			}
			if (size == 0) {
				size = 4;
			}
			return super.getFont(fntname, encoding, size, style);
		}
	}

	static class ImageTagProcessor extends com.itextpdf.tool.xml.html.Image {

		@Override
		public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
			final Map<String, String> attributes = tag.getAttributes();
			String src = attributes.get(HTML.Attribute.SRC);
			List<Element> elements = new ArrayList<Element>(1);
			if (null != src && src.length() > 0) {
				if (src.startsWith("data:image/")) {
					Image img = null;
					final String base64Data = src.substring(src.indexOf(",") + 1);
					try {
						img = Image.getInstance(Base64.decode(base64Data));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					if (img != null) {
						try {
							final HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
							elements.add(getCssAppliers().apply(new Chunk(
									(com.itextpdf.text.Image) getCssAppliers().apply(img, tag, htmlPipelineContext), 0,
									0, true), tag, htmlPipelineContext));
						} catch (NoCustomContextException e) {
							throw new RuntimeWorkerException(e);
						}
					}
				} else {
					elements = super.end(ctx, tag, currentContent);
				}
			}
			return elements;
		}
	}
}
