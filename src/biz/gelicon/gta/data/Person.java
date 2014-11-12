package biz.gelicon.gta.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import biz.gelicon.gta.view.NodeView;

public class Person  implements NodeView {
	private String nic;
	private String post;
	private boolean active;

	protected Person(String nic, String post) {
		this.nic = nic;
		this.post = post;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getNic() {
		return nic;
	}
	public String getPost() {
		return post;
	}
	
	public static List<Person> getDemoPersons() {
		List<Person> tp = new LinkedList<Person>();
		if(new Random().nextInt(2)>0) tp.add(new Person("John", "PM"));
		if(new Random().nextInt(2)>0) tp.add(new Person("Mary", "QA"));
		if(new Random().nextInt(2)>0) tp.add(new Person("Liza","Developer"));
		
		if(tp.size()>0)
			tp.get(new Random().nextInt(tp.size())).setActive(true);
		
		return tp;
	}

	@Override
	public String getText() {
		return nic+"#"+post;
	}

}
