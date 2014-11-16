package biz.gelicon.gta.net;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.utils.Handler;

public class WorkNetService implements NetService {
	private Logger log = Logger.getLogger("gta");
	private String url;
	private Client client;
	private String token;

	public WorkNetService(String url) {
		this.url = url;
		ClientConfig config = new ClientConfig();
	    client = ClientBuilder.newClient(config);
	}
	
	@Override
	public User connect(String user, String password) throws Exception {
	    WebTarget target = client.target(new URI(url));
	    ArrayList<String> args = new ArrayList<>();
	    args.add(user);
	    args.add(password);
	    Response result = target.path("login")
	    	.request(MediaType.APPLICATION_JSON_TYPE)
	    	.post(Entity.json(args));
	    token = (String) result.getEntity();
	    if(token!=null || !token.isEmpty()) return new User(user);
		return null;
	}

	@Override
	public void ping(Handler<List<Team>> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Team> getTeams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postData(LocalDate beg, LocalDate finish, int key, int mouse,
			int mouseMove, BufferedImage screenshot) {
		// TODO Auto-generated method stub

	}

}
