package biz.gelicon.gta.net;

import java.io.File;
import java.util.List;

import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Message;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.utils.Handler;

public interface NetService {
	public enum NetState {networkNotAvailable, sessionLost, sessionValid};
	
	public User connect(String user, String password) throws Exception;
	public void ping(Handler<NetState> handler);
	public List<Team> getTeams();
	public void postData(Message message, File imgfile) throws Exception;
	public void checkLimits(Team team) throws Exception;

}
