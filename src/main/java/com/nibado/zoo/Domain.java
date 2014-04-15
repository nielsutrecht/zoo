package com.nibado.zoo;

public class Domain {
	private String domain;
	private int port;
	
	public Domain() {
		
	}
	
	public Domain(String domain, int port) {
		this.domain = domain;
		this.port = port;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
