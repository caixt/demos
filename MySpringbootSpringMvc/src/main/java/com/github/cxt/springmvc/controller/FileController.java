package com.github.cxt.springmvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping(value="/file")
public class FileController {

	
	@RequestMapping("/download")
	public ResponseEntity<StreamingResponseBody> handle(HttpServletRequest request) throws FileNotFoundException {
		final File file = new File("pom.xml");
		final InputStream responseStream = new FileInputStream(file);
		
		HttpHeaders headers = new HttpHeaders();  
        headers.setContentDispositionFormData("attachment", file.getName(), Charset.forName("utf-8"));  
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); 
		
	    return new ResponseEntity<StreamingResponseBody>(new StreamingResponseBody() {
	        @Override
	        public void writeTo(OutputStream out) throws IOException {
	        	try{
	                int length;
	                byte[] buffer = new byte[1024 * 10];
	                while((length = responseStream.read(buffer)) != -1) {
	                    out.write(buffer, 0, length);
	                    out.flush();
	                }
            	}finally{
            		responseStream.close();
            	}
	        }
	    }, headers, HttpStatus.OK);
	}
}
