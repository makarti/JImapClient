package mail;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;

import org.eclipse.ui.services.IDisposable;
import org.jsoup.Jsoup;

import code.core.Setting;
import code.core.StaticData;
import code.dto.Letter;

public class FolderReader implements IDisposable{
	
	private Folder folder = null;
    private Message[]	messages;
    //private MimeMessage ms;
	
	public FolderReader(Folder folder)
	{
		this.folder = folder;
	}
	
	public Folder getFolder()
	{
		return folder;
	}
	
	public void read(Boolean isTrash, String login)
	{
		StaticData data = Setting.Instance();
		try {
			
			if (!folder.isOpen()) {
				folder.open(Folder.READ_WRITE);			
			}    
		    // get the messages
			messages = folder.getMessages();
			
			int count = messages.length;
			int start = 0;//messages.length-10>0?messages.length-10:0;
			for (int i = start; i < count ; i++)
			{
				try
				{
					Message msg = messages[i];
					Letter letter = new Letter();
					letter.subject = msg.getSubject();
	       	 		letter.id = i;
	       	 		letter.to = login;
	       	 		
	       	 		if (msg.getFrom().length >0)
	       	 			letter.from =((InternetAddress)msg.getFrom()[0]).getAddress();
	       	 		else
	       	 			letter.from = "unknown";
	       	 		letter.IsSeen = msg.isSet(Flags.Flag.SEEN);       	 		
	       	 		letter.received = msg.getReceivedDate().toLocaleString();
	       	 		letter.created = msg.getSentDate() == null?letter.received :msg.getSentDate().toLocaleString();
	       	 		letter.subject = msg.getSubject();
	       	 		try{
	       	 			letter.msgId = ((MimeMessage)msg).getMessageID();
	       	 			if(letter.msgId == null)
	       	 				letter.msgId = letter.from+letter.received;
	       	 		}
	       	 		catch(Exception iderror)
	       	 		{
	       	 			letter.msgId = letter.from+letter.received;
	       	 		}
	       	 		data.addLetter(letter, isTrash);
				}
				catch(Exception e)
				{
					continue;
				}
       	 				
			}
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(final String msgId)
	{
		try {			
			if (!folder.isOpen()) {
				folder.open(Folder.READ_WRITE);			
			}			
			SearchTerm term = new MessageIDTerm(msgId);
			
			Message[] msgs = folder.search(term);
			if(msgs.length>0){
		        folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
			}
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void mouve(final String msgId, Folder dfolder)
	{
		try {			
			if (!folder.isOpen()) {
				folder.open(Folder.READ_WRITE);			
			}    
			if (!dfolder.isOpen()) {
				dfolder.open(Folder.READ_WRITE);			
			}
			
			SearchTerm term = new MessageIDTerm(msgId);
			
			Message[] msgs = folder.search(term);
			if(msgs.length>0){
				folder.copyMessages(msgs, dfolder);
		        folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
			}
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void RemoveMessageByFilters(Folder dfolder)
	{
		StaticData data = Setting.Instance();
		String pattern = Setting.Joiner(data.getFilters());
		if(pattern==null)
		{
			return;
		}
		final Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		try {
			
			if (!folder.isOpen()) {
				folder.open(Folder.READ_WRITE);			
			}    
			if (!dfolder.isOpen()) {
				dfolder.open(Folder.READ_WRITE);			
			}
			
			SearchTerm term = new SearchTerm() {
			    public boolean match(Message message) {
			        try {			      
			        	String subject = message.getSubject();
			            if (subject!=null && p.matcher(subject).find()) {
			                return true;
			            }
			        } catch (MessagingException ex) {
			            ex.printStackTrace();
			        }
			        return false;
			    }
			};
			
			Message[] msgs = folder.search(term);
			if(msgs.length>0){
				folder.copyMessages(msgs, dfolder);
		        folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
			}
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

 	public String getContent(final String msgId)
	{
		StaticData data = Setting.Instance();
		try {
			
			if (!folder.isOpen()) {
				folder.open(Folder.READ_WRITE);			
			}    
			SearchTerm searchTerm = new MessageIDTerm(msgId);
			Message[] messages = folder.search(searchTerm);
			Message message = null;
			if(messages.length<1)
			{
				SearchTerm term = new SearchTerm() {
				    public boolean match(Message message) {
				        try {
				        	String msg = ((InternetAddress)message.getFrom()[0]).getAddress()+message.getReceivedDate().toLocaleString();
				            if (msg.equals(msgId)) {
				                return true;
				            }
				        } catch (MessagingException ex) {
				            ex.printStackTrace();
				        }
				        return false;
				    }
				};
				messages = folder.search(term);
			}			
			if(messages.length<1){
				return "";
			}
			try
			{
				message = messages[0];
				message.setFlag(Flag.SEEN, true);
				StringBuilder sb = new StringBuilder();
				return parseContent(message, null);
			}
			catch(Exception e)
			{
				return "";
			}
			
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}		
	}
	
	private String parseContent(Message message, String result){
		try {
			if(message instanceof MimeMessage)
		    {
		        MimeMessage m = (MimeMessage)message;
		        Object contentObject;
					contentObject = m.getContent();
		        if(contentObject instanceof Multipart)
		        {
		            BodyPart clearTextPart = null;
		            BodyPart htmlTextPart = null;
		            Multipart content = (Multipart)contentObject;
		            int count = content.getCount();
		            for(int i=0; i<count; i++)
		            {
		                BodyPart part =  content.getBodyPart(i);
		                if(part.isMimeType("text/plain"))
		                {
		                    clearTextPart = part;
		                    break;
		                }
		                else if(part.isMimeType("text/html"))
		                {
		                    htmlTextPart = part;
		                }
		            }
		
		            if(clearTextPart!=null)
		            {
		                result = (String) clearTextPart.getContent();
		            }
		            else if (htmlTextPart!=null)
		            {
		                //String html = (String) htmlTextPart.getContent();
		                //result = Jsoup.parse(html).text();
		            	result = (String) htmlTextPart.getContent();
		            }
		
		        }
		        else if (contentObject instanceof String) // a simple text message
		        {
		            result = (String) contentObject;
		        }
		        else // not a mime message
		        {            
		            result = "not a mime part or multipart "+message.toString();
		        }
		    }
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "can't read this message";
    }
	
	@Override
	public void dispose() {
		if (folder != null)
			try {
				folder.close(true);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
}
