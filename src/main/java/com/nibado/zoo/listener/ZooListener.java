package com.nibado.zoo.listener;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooListener implements Watcher{

	private ZooKeeper zk;
	private static final String PATH_DOMAINS = "/domains";
	
	public void init() throws Exception {
		zk = new ZooKeeper("127.0.0.1:3000", 60000, this);
		Stat stat = zk.exists(PATH_DOMAINS, true);
		if(stat == null) {
			zk.create(PATH_DOMAINS, null, null, CreateMode.PERSISTENT);
		}
	}

	public void process(WatchedEvent event) {
		
		String path = event.getPath();
		System.out.println("Event: " + path);
		
		try {
			 zk.getChildren(path, true);
		} 
		catch (KeeperException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void handleChildren(List<String> children) {
		System.out.println("*** Children ***");
		for(String s : children)
			System.out.println(s);
	}
	
	public static void main(String[] args) throws Exception {
		ZooListener listener = new ZooListener();
		listener.init();

	}	

}
