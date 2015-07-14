package org.oss.samilgong.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Starter;

public class ServerMain extends AbstractVerticle {
	
	public static void main(String[] args) {
		String[] runInfo = {
			"run",
			ServerMain.class.getName()
		};
		Starter.main(runInfo); 
	}
	
	@Override
	public void start() throws Exception {
		
	}
}
