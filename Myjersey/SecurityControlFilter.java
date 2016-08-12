
	
	private void holdHttpInfo(ContainerRequestContext requestContext) throws IOException {
    	String method = requestContext.getMethod();
    	String url = requestContext.getUriInfo().getRequestUri().toString();
    	String contentType = requestContext.getHeaderString("Content-Type");
		if(StringUtils.equalsIgnoreCase("post", method) && StringUtils.startsWith(contentType, MediaType.APPLICATION_JSON)){
			InputStream inputStream = requestContext.getEntityStream();
			StringBuilder sb = new StringBuilder();
			InputStreamReader input = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader reader = new BufferedReader(input);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			requestContext.setEntityStream(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
			SecurityContextHolder.addHttpInfo(url, method, sb.toString());
		}
		else{
			SecurityContextHolder.addHttpInfo(url, method, null);
		}
    }
	
	
