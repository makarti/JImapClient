package code.dto;

import java.util.Date;

public class Letter {

	public int id;
	public String msgId;
	public String from;
	public String to;
	public String subject;
	public String body;
	public Boolean IsSeen;
	public String created;
	public String received;
	
	public Letter()
	{
		
	}
	
	public Letter(int id, String msgId, String from, String to, String subject, String body, Date created, Date received)
	{
		this.id = id;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.received = received.toLocaleString();
		this.created = created==null? this.received: created.toLocaleString();
	}
	
}
