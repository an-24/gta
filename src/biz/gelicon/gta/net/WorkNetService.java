package biz.gelicon.gta.net;

import java.io.File;
import java.io.FileInputStream;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import biz.gelicon.gta.User;
import biz.gelicon.gta.data.Message;
import biz.gelicon.gta.data.Team;
import biz.gelicon.gta.utils.Handler;
import biz.gelicon.gta.utils.MessageWrapper;

public class WorkNetService implements NetService {
	private Logger log = Logger.getLogger("gta");
	private String url;
	private Client client;
	private String token;

	public WorkNetService(String url) {
		this.url = url;
	    client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .register(MultiPartFeature.class)
                //.register(MultiPartWriter.class)
                .register(new LoggingFilter(log, true))
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
		try {
			WebTarget target = client.target(new URI(url));
			GenericType<List<Team>> teamsType = new GenericType<List<Team>>(){};
			Response result = target.path("teams")
			    	.request(MediaType.APPLICATION_JSON_TYPE)
			    	.accept(MediaType.APPLICATION_JSON_TYPE)
			    	.post(Entity.json(token));
		    if(result.getStatus() != Status.OK.getStatusCode()) 
		    	throw new Exception(result.getStatus()+": "+Status.fromStatusCode(result.getStatus()).getReasonPhrase());
		    else {
		    	return result.readEntity(teamsType);
		    }
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return new ArrayList<>();
		}
	}
	
	@Override
	public void postData(Message message, File img) throws Exception {
		WebTarget target = client.target(new URI(url));
		MessageWrapper arg = new MessageWrapper(token,message);
	    Response result = target.path("timing")
		    	.request(MediaType.APPLICATION_JSON_TYPE)
		    	.accept(MediaType.APPLICATION_JSON_TYPE)
		    	.post(Entity.json(arg));
	    if(result.getStatus() != Status.OK.getStatusCode()) 
	    	throw new Exception(result.getStatus()+": "+Status.fromStatusCode(result.getStatus()).getReasonPhrase());
	    Integer id = result.readEntity(Integer.class);
	    // screenshot
	    StreamDataBodyPart stream = new StreamDataBodyPart("picture", new FileInputStream(img));
	    MultiPart part = new FormDataMultiPart()
	    	.field("token", token)
	    	.field("id", id.toString())
	    	.bodyPart(stream);
	    result = target.path("timing/upload")
	    	.request(MediaType.MULTIPART_FORM_DATA_TYPE)
  			.accept(MediaType.APPLICATION_JSON_TYPE)
  			.post(Entity.entity(part, MediaType.MULTIPART_FORM_DATA_TYPE));
	    if(result.getStatus() != Status.OK.getStatusCode()) 
	    	throw new Exception(result.getStatus()+": "+Status.fromStatusCode(result.getStatus()).getReasonPhrase());
		log.info("Data posted in server");
	}

}
