package com.nibado.zoo.listener;

import java.io.File;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.common.io.Files;
import com.nibado.zoo.Domain;

import static com.nibado.zoo.ZooHelper.*;

public class ZooListener implements CuratorListener, ConnectionStateListener, Runnable {

	private final Logger logger = Logger.getLogger(ZooListener.class);
	private CuratorFramework client;
	private final String connectString, templateFile, configFile;
	
	public ZooListener(String connectString, String templateFile, String configFile) throws Exception {
		this.connectString = connectString;
		this.templateFile = templateFile;
		this.configFile = configFile;
		
		if(!new File(templateFile).exists()) {
			throw new IllegalArgumentException(templateFile + " does not exist.");
		}
		
	}
	
	public void init() throws Exception {
		logger.debug("** init() start **");
		client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
		client.start();

		client.getCuratorListenable().addListener(this);
		client.getConnectionStateListenable().addListener(this);
        
        watch();
        logger.debug("** init() end **");
	}

    public void run() {
        try {
            synchronized (this) {
                while (client.getState() == CuratorFrameworkState.STARTED) {
                    wait();
                }
            }
        } 
        catch (InterruptedException e) {
        }
    }

	public void eventReceived(CuratorFramework client, CuratorEvent event) 	throws Exception {
		if(event.getPath() != null) {
			pathEventRecieved(event);
		}
		else {
			logger.debug("CE: " + event);
		}
	}
	
	private void pathEventRecieved(CuratorEvent event) throws Exception {
		if(PATH_DOMAINS.equals(event.getPath())) {
			List<String> domains = watch();
			writeConfig(domains);
		}
		else {
			logger.debug("CE: " + event);
		}
	}
	
	private void writeConfig(List<String> domains) throws Exception {
		StringBuilder builder = new StringBuilder(1000 * domains.size());
		String template = getTemplate(templateFile);
		for(String domain : domains) {
			byte[] buf = client.getData().watched().forPath(PATH_DOMAINS + '/' + domain);
			Domain domainObj = decodeDomain(buf);
			addConfig(builder, template, domainObj);
		}
		
		saveConfig(builder, configFile);
	}

	private List<String> watch() throws Exception {
		return client.getChildren().watched().forPath(PATH_DOMAINS);
	}

	public void stateChanged(CuratorFramework client, ConnectionState state) {
		logger.debug("ConState: " + state);
		
	}
	
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		if(args.length < 3)
			throw new IllegalArgumentException("Too few arguments.");
		String connectString = args[0];
		String templateFile = args[1];
		String configFile = args[2];
		
		ZooListener listener = new ZooListener(connectString, templateFile, configFile);
		listener.init();
		listener.run();
		
	}	

}
