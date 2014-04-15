package com.nibado.zoo;

import java.nio.charset.Charset;

public class ZooHelper {
	private final static Charset CHARSET = Charset.forName("UTF-8"); 
	public final static String PATH_DOMAINS = "/domains";
	
	public static byte[] encodeDomain(Domain domain) {
		String domainString = domain.getDomain() + ":" + domain.getPort();
		
		return domainString.getBytes(CHARSET);
	}
	
	public static Domain decodeDomain(byte[] buffer) {
		String[] domainParts = new String(buffer, CHARSET).split(":");
		
		return new Domain(domainParts[0], Integer.parseInt(domainParts[1]));
	}

}
