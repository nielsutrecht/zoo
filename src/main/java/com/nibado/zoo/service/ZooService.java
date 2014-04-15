package com.nibado.zoo.service;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.nibado.zoo.Domain;
import com.nibado.zoo.ZooHelper;

import static com.nibado.zoo.ZooHelper.*;

public class ZooService implements Watcher {

	private ZooKeeper zk;
	public void init() throws IOException {
		zk = new ZooKeeper("127.0.0.1:3000", 60000, this);
	}

	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void addDomain(Domain domain) {
		byte[] bytes = encodeDomain(domain);
		try {
			zk.create(ZooHelper.PATH_DOMAINS + "/" + domain.getDomain(), bytes, null, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		 ZooService service = new ZooService();
		 service.init();

	}	
	


}
