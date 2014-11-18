package biz.gelicon.gta.net;

import java.awt.Image;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jackson.JacksonFeature;

import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Message;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.utils.Handler;

public class WorkNetService implements NetService {
	private Logger log = Logger.getLogger("gta");
	private String url;
	private Client client;
	private String token;

	public WorkNetService(String url) {
		this.url = url;
	    client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .build();
	}
	
	@Override
	public User connect(String user, String password) throws Exception {
	    WebTarget target = client.target(new URI(url));
	    ArrayList<String> args = new ArrayList<>();
	    args.add(user);
	    args.add(password);
	    Response result = target.path("login")
	    	.request(MediaType.APPLICATION_JSON_TYPE)
	    	.accept(MediaType.APPLICATION_JSON_TYPE)
	    	.post(Entity.json(new GenericEntity<List<String>>(args) {}));
	    if(result.getStatus() != Status.OK.getStatusCode()) 
	    	throw new Exception(result.getStatus()+": "+Status.fromStatusCode(result.getStatus()).getReasonPhrase());
	    if(!result.hasEntity()) return null;
	    token = result.readEntity(String.class);
	    return new User(user);
	}

	@Override
	public void ping(Handler<NetState> handler) {
	    try {
			WebTarget target = client.target(new URI(url));
		    Response result = target.path("ping")
			    	.request(MediaType.APPLICATION_JSON_TYPE)
			    	.accept(MediaType.APPLICATION_JSON_TYPE)
			    	.post(Entity.json(token));
		    if(result.getStatus() != Status.OK.getStatusCode() || !result.hasEntity()) {
		    	handler.handle(NetState.networkNotAvailable);
		    } else {
			    Boolean b = result.readEntity(Boolean.class);
		    	handler.handle(b?NetState.sessionValid:NetState.sessionLost);
		    }
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			try {
				handler.handle(NetState.networkNotAvailable);
			} catch (Exception e1) {
				e1.initCause(e);
				log.log(Level.SEVERE, e1.getMessage(), e1);
			}
		};
	}

	@Override
	public List<Team> getTeams() {
		List<Team> teams = new ArrayList<>(); 
		// TODO Auto-generated method stub
		return teams;
	}

	@Override
	public void postData(Message message, Image img) {
		// TODO Auto-generated method stub
		log.info("Data posted in server");
	}

}
