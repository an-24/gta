package biz.gelicon.gta;

import biz.gelicon.gta.data.Person;

public class User extends Person {

	public User(String nic) {
		super(nic, null);
	}
	
	public static User login(String nic,String pswd) {
		//TODO login process
		return new User(nic);
	} 

}
