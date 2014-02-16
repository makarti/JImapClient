package mail;

import javax.mail.MessagingException;
import javax.mail.Store;
import code.dto.Account;

public class StoreAccounts {
   private Account account; 
   protected Store	store = null;
   protected FolderReader	inbox = null;
   protected FolderReader trash = null;
   
   public StoreAccounts(Store what, Account account)
   {
	   this.store = what;	   
	   this.account = account;
   }
   
   public void start()//Boolean IsTrash)
   {
	   init();
	   try{
	   inbox.RemoveMessageByFilters(trash.getFolder());
	   inbox.read(false, account.getLogin());
	   trash.read(true, account.getLogin());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
   }   
   
   public void init(){
	   try {
		   if(!store.isConnected())
		   {
			   store.connect(account.getServer(), account.getLogin(), account.getPass());
			   inbox = new FolderReader(store.getFolder("Inbox"));
			   trash = new FolderReader(store.getFolder(getNameTrash()));
		   }
	   } catch (MessagingException e) {
			// TODO Auto-generated catch block
		   e.printStackTrace();
	   }
   }
   
   public String GetMessage(String msgId, Boolean IsTrash)
   {
	   try
	   {
	   init();
	   if(IsTrash)
	   {
		   return trash.getContent(msgId);
	   }
	   else
	   {
		   return inbox.getContent(msgId);
	   }
	   }
	   catch(Exception e)
	   {
		   return "Ресурс занят, запросите позже.";
	   }
   }
   
   public void delete(String msgId, Boolean IsTrash)
   {
	   init();
	   if(IsTrash)
	   {
		   trash.delete(msgId);
	   }
	   else
	   {
		   inbox.mouve(msgId, trash.getFolder());
	   }
   }
   
   
   protected String getNameTrash()
   {
	   switch(account.getServer())
	   {
	   		case "imap.gmail.com": return "[Gmail]/Корзина";
	   		case "imap.mail.ru": return "Корзина";
	   		default : return "Trash";
	   }
   }
   
   public void close()
   {
	   if(this.store!=null)
	   {
		   try {
			store.close();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
   }
}
