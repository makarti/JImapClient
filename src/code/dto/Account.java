package code.dto;

import javax.mail.Store;

public class Account {
	private int id;
	private String login;
	private String pass;
	private String server;
	private Boolean isInclude;
	private Store store;
	
	public Account()
	{
		
	}
	
	public Account(int id, String login, String pass, String server, Boolean isInclude)
	{
		this.id = id;
		this.login = login;
		this.pass = pass;
		this.server = server;
		this.isInclude = isInclude;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getIsInclude() {
		return isInclude?"Включен":"Выключен";
	}
	
	public Boolean IsInclude() {
		return isInclude;
	}
	
	public void setIsInclude(Boolean isInclude) {
		this.isInclude = isInclude;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}
