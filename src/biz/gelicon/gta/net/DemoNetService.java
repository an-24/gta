package biz.gelicon.gta.net;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Message;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.utils.Handler;

public class DemoNetService implements NetService {
	private Logger log = Logger.getLogger("gta");

	public DemoNetService(String url) {
		// empty
	}

	@Override
	public User connect(String user, String password) {
		return new User(user);
	}

	@Override
	public void ping(Handler<NetService.NetState> handler) {
		try {
			handler.handle(NetService.NetState.sessionValid);
		} catch (Exception e) {
		}
	}

	@Override
	public List<Team> getTeams() {
		return Team.getDemoTeams();
	}

	@Override
	public void postData(Message message, Image img) {
		log.info("data posted "+message);
	}

}
