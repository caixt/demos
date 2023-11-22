package com.github.cxt.springboot2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.github.cxt.springboot2.entities.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "文件接口")
@RestController
@RequestMapping(value="/file")
public class FileController {

	@ApiOperation(value = "文件上传")
	@PutMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, path = "put/{name}")
	public R putFile(InputStream inputStream, @PathVariable("name") String name, HttpServletRequest request) throws IOException {
//		String path = request.getServletContext().getRealPath("/");
//		path += File.separator + "data" + File.separator + name;
		String path = "data" + File.separator + name;
		File file = new File(path);
		FileUtils.copyInputStreamToFile(inputStream, file);
		return R.success();
	}
	
	@RequestMapping("/download")
	public ResponseEntity<StreamingResponseBody> download() throws FileNotFoundException {
		final File file = new File("pom.xml");
		final InputStream responseStream = new FileInputStream(file);
		
		HttpHeaders headers = new HttpHeaders();  
		headers.setContentDispositionFormData("attachment", file.getName());
//        headers.setContentDispositionFormData("attachment", file.getName(), Charset.forName("utf-8"));  
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
