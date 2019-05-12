package com.github.cxt.MyTools.ftp;

public class FtpFile {

	private String path;
	
	private Long lastModified;
	
	private Long length;

	public FtpFile(String path, Long lastModified, Long length) {
		super();
		this.path = path;
		this.lastModified = lastModified;
		this.length = length;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "FtpFile [path=" + path + ", lastModified=" + lastModified + ", length=" + length + "]";
	}
}
