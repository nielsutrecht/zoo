package com.nibado.zoo.service;

import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.nibado.zoo.Domain;

import static com.nibado.zoo.ZooHelper.*;

public class ZooService {

	private final String connectString;
	private final Logger logger = Logger.getLogger(ZooService.class);
	private CuratorFramework client;
	
	public ZooService(String connectString) {
		this.connectString = connectString;
	}
	
	public void init() throws IOException {
		logger.debug("** init() start **");
		client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
		client.start();
        logger.debug("** init() end **");
	}

	public void addDomain(Domain domain) throws Exception {
		byte[] bytes = encodeDomain(domain);

		client.create().forPath(PATH_DOMAINS + '/' + domain.getDomain(), bytes);
	}
	
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		if(args.length < 3)
			throw new IllegalArgumentException("Too few arguments.");
		String connectString = args[0];
		String domain = args[1];
		String port = args[2];
		
		Domain domainObj = new Domain(domain, Integer.parseInt(port));
		
		 ZooService service = new ZooService(connectString);
		 service.init();
		 service.addDomain(domainObj);

	}	
	


}
