/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.github.cxt.Myjersey.jerseycore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;  
import org.glassfish.jersey.media.multipart.FormDataParam;  

@Path("/file")
public class FileResource {
    
	@Path("upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadFile(@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition disposition, 
			@Context HttpServletRequest request) throws IOException {
		String fileName = new String(disposition.getFileName().getBytes("ISO8859-1"), "UTF-8");
		String name = Calendar.getInstance().getTimeInMillis() + fileName;
		String path = request.getServletContext().getRealPath("/");
		path += File.separator + "data" + File.separator + name;
		File file = new File(path);
		try {
			FileUtils.copyInputStreamToFile(inputStream, file);
		} catch (IOException ex) {
			ex.printStackTrace();
			return "{\"success\": false}";
		}
		return "{\"success\": true}";

	}
	
	@Path("download")
	@GET
	public Response downloadFile(@Context HttpServletRequest request) throws IOException {
		File file = new File(request.getServletContext().getRealPath("index.html"));
		String mt = new MimetypesFileTypeMap().getContentType(file);
        return Response
                .ok(file, mt)
                .header("Content-disposition","attachment;filename=" + file.getName()) 
                .header("ragma", "No-cache").header("Cache-Control", "no-cache").build();
		
	}
	
	@Path("download2")
	@GET
	public Response downloadFile(@Context HttpServletResponse response, @Context HttpServletRequest request) throws IOException {
		final File file = new File(request.getServletContext().getRealPath("index.html"));
		final InputStream responseStream = new FileInputStream(file);
		StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
            	try{
	                int length;
	                byte[] buffer = new byte[1024 * 10];
	                while((length = responseStream.read(buffer)) != -1) {
	                    out.write(buffer, 0, length);
	                    out.flush();
	                }
            	}finally{
            		responseStream.close();
            		//file.delete();
            	}
            }   
        };
        
        String filename = file.getName();
        String userAgent = request.getHeader("User-Agent");
        //IE11 User-Agent字符串:Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko
        //IE6~IE10版本的User-Agent字符串:Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.0; Trident/6.0)
         
        if (userAgent != null && (userAgent.toLowerCase().indexOf("msie") > 0 || userAgent.indexOf("like Gecko") > 0)){
        	filename = URLEncoder.encode(filename, "UTF-8");//IE浏览器
        }else {
        //先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,
        //这个文件名称用于浏览器的下载框中自动显示的文件名
        	filename = new String(filename.replaceAll(" ", "").getBytes(), "ISO8859-1");
        //firefox浏览器
        //firefox浏览器User-Agent字符串: 
        //Mozilla/5.0 (Windows NT 6.1; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0
        }
        
        return Response.ok(output).header("Content-Disposition", "attachment; filename=" + filename).build();
	}
	
	
    @Path("show")
    @GET
    public Response show(@Context HttpServletRequest request) throws FileNotFoundException {
    	final File file = new File(request.getServletContext().getRealPath("index.html"));
		final InputStream responseStream = new FileInputStream(file);
		StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
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
        };
        return Response.ok(output).header("Content-Type", "text/plain").build();
    }
}
