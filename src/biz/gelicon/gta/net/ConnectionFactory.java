package biz.gelicon.gta.net;

public class ConnectionFactory {
	static private NetService instance;
	
	public static NetService getConnection(String url) {
		//if(instance==null) instance = new DemoNetService(url);
		if(instance==null) instance = new WorkNetService(url);
		return instance;
	}

	public static NetService getConnection() {
		return instance;
	}
}
