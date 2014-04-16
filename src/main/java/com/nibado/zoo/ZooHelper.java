package com.nibado.zoo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class ZooHelper {
	public final static String PATH_DOMAINS = "/domains";
	
	public static byte[] encodeDomain(Domain domain) {
		String domainString = domain.getDomain() + ":" + domain.getPort();
		
		return domainString.getBytes(Charsets.UTF_8);
	}
	
	public static Domain decodeDomain(byte[] buffer) {
		String[] domainParts = new String(buffer, Charsets.UTF_8).split(":");
		
		return new Domain(domainParts[0], Integer.parseInt(domainParts[1]));
	}
	
	public static String getTemplate(String templateFile) throws IOException {
		return Files.toString(new File(templateFile), Charsets.UTF_8);
	}
	
	public static void addConfig(StringBuilder builder, String template, Domain domain) {
		template = template.replace("{{domain}}", domain.getDomain());
		template = template.replace("{{port}}", Integer.toString(domain.getPort()));
		builder.append(template);
	}
	
	public static void saveConfig(StringBuilder builder, String configFile) throws IOException {
		Files.write(builder, new File(configFile), Charsets.UTF_8);
	}

}
