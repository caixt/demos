package com.github.cxt.MyTools.pdf;

import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.CSSResource;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.AWTFSImage;
import org.xhtmlrenderer.util.XRLog;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;


public class CustomUserAgentCallback implements UserAgentCallback {
	
	public ITextUserAgent uac = null;
	
	public CustomUserAgentCallback(ITextUserAgent uac) {
		this.uac = uac;
	}

	@Override
	public CSSResource getCSSResource(String uri) {
		return uac.getCSSResource(uri);
	}

	@Override
	public ImageResource getImageResource(String uri) {
		ImageResource resource = getImageResourceInternal(uri);
		if(resource != null) {
			FSImage image = resource.getImage();
			if(image.getWidth() > 13000) {
				image.scale(13000, image.getHeight() * 13000 / image.getWidth());
			}
		}
		return resource;
	}
	
	private ImageResource getImageResourceInternal(String uri) {
		String src = uri;
		if (src.startsWith("data:image/")) {
			final String base64Data = src.substring(src.indexOf(",") + 1);
			Image img = null;
			try {
//				img = Image.getInstance(java.util.Base64.getDecoder().decode(base64Data.replace("\r\n", "")));
				img = Image.getInstance(Base64.decode(base64Data));
				scaleToOutputResolution(img);
				return new ImageResource(uri, new ITextFSImage(img));
			} catch (Exception e) {
				 XRLog.exception("Can't read image file; unexpected problem for URI '" + uri + "'", e);
			}
			return null;
		}
		return uac.getImageResource(uri);
	}
	
	private void scaleToOutputResolution(Image image) {
        float factor = uac.getSharedContext().getDotsPerPixel();
        if (factor != 1.0f) {
            image.scaleAbsolute(image.getPlainWidth() * factor, image.getPlainHeight() * factor);
        }
    }

	@Override
	public XMLResource getXMLResource(String uri) {
		return uac.getXMLResource(uri);
	}

	@Override
	public byte[] getBinaryResource(String uri) {
		return uac.getBinaryResource(uri);
	}

	@Override
	public boolean isVisited(String uri) {
		return uac.isVisited(uri);
	}

	@Override
	public void setBaseURL(String url) {
		uac.setBaseURL(url);
	}

	@Override
	public String getBaseURL() {
		return uac.getBaseURL();
	}

	@Override
	public String resolveURI(String uri) {
		return uac.resolveURI(uri);
	}
	
	protected ImageResource createImageResource(String uri, 
			java.awt.Image img) {
		return new ImageResource(uri, AWTFSImage.createImage(img));
	}
	
}