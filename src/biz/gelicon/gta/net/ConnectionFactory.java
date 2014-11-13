package biz.gelicon.gta.net;

public class ConnectionFactory {
	static private NetService instance;
	
	public static NetService getConnection(String url) {
		//FIXME demo
		if(instance==null) instance = new DemoNetService(url);
		
		return instance;
	}

	public static NetService getConnection() {
		return instance;
	}
}
