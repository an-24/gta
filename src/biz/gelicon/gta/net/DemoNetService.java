package biz.gelicon.gta.net;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import biz.gelicon.gta.User;
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
	public void ping(Handler<List<Team>> handler) {
		// empty
	}

	@Override
	public List<Team> getTeams() {
		return Team.getDemoTeams();
	}

	@Override
	public void postData(LocalDate beg, LocalDate finish, int key, int mouse,
			int mouseMove, BufferedImage screenshot) {
		log.info("data posted ["+beg+"-"+finish+" key="+key+" mouse="+mouse+" mouseMove="+mouseMove+" image="+screenshot+"]");
	}

}
