package biz.gelicon.gta.net;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.utils.Handler;

public interface NetService {
	
	public User connect(String user, String password);
	public void ping(Handler<List<Team>> handler);
	public List<Team> getTeams();
	public void postData(LocalDate beg,LocalDate finish, int key,int mouse,int mouseMove, 
			BufferedImage screenshot);

}
