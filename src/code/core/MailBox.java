package code.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import window.MainView;

import mail.SimpleGenerator;
import mail.SimpleViewer;
import mail.StoreAccounts;

import code.dto.Account;
import code.dto.Letter;
import code.repository.SqliteBD;

public class MailBox {
	private Map<Integer, StoreAccounts> stories;
	private Session session;
	private Display display;
	
	public MailBox(Display display)
	{
		stories = new HashMap<Integer, StoreAccounts>();
		this.session = Session.getDefaultInstance(System.getProperties());
		this.display = display;
		init();
	}
	
	public void init()
	{
		StaticData data = Setting.Instance();
		for (Account account: data.getAccounts()) {
			addStore(account);
		}	
	}
	
	public void addStore(Account account)
	{
		try {
			if(account.getServer().equals("imap.s4ab.ru"))
				stories.put(account.getId(), new StoreAccounts(session.getStore("imap"), account));
			else
				stories.put(account.getId(), new StoreAccounts(session.getStore("imaps"), account));					
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		if(Setting.Instance().getAccounts().size()>0)
		{
			final int countmails = stories.size();
			int ck = 2;
			this.display.syncExec(new Runnable() {
				
				@Override
				public void run() {
					MainView.progressBar.setMaximum(countmails+1);
					MainView.progressBar.setSelection(1);
				}
			});
			StaticData data = Setting.Instance();
			for (Account account: data.getAccounts()){
				if(account.IsInclude())
				{
					if(!stories.containsKey(account.getId())){
						addStore(account);
			    	}
					ChangeBar(ck++);
					stories.get(account.getId()).start();
				}
			}		
			ChangeBar(countmails);
			rePaint();
		}
		
	}
	
	public void close()
	{
		for (StoreAccounts store: stories.values()) {	
			if(store!= null){
				store.close();
	    	}
		}
		SqliteBD.getInstance().close();		
	}
	
	public void ChangeBar(final int i)
	{
		this.display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				MainView.progressBar.setSelection(i);
				
			}
		});
	}
	
	public void rePaint()
	{
		final StaticData data = Setting.Instance();
		data.UpdateSort();
		this.display.asyncExec(new Runnable() {			
			//@Override
			public void run() {				
				MainView.lettersTable.removeAll();
				Iterator<Integer> keys = data.getKeysIterator();
				int key = 0;
				int it = 0;
				while(keys.hasNext())
					{			
						try
						{
							key = keys.next();
							TableItem item = new TableItem(MainView.lettersTable, SWT.LEFT);
							if(!data.isSeen(key)){
								item.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
							}
							item.setText(data.getLetterByString(key));
							it++;
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				if(MainView.lettersTable.getItems().length>0){
					MainView.lettersTable.showItem(MainView.lettersTable.getItem(it-1));
					MainView.progressBar.setSelection(0);
				}
			}
		});
	}
	
	public void deleteMessage(final int index, final int item)
	{
		final StaticData data = Setting.Instance();
		final Letter letter = data.getLetter(index);
		final Account account = data.AccountByLogin(letter.to);
		this.display.asyncExec(new Runnable() {			
			//@Override
			public void run() {	
				stories.get(account.getId()).delete(letter.msgId, data.IsTrash());
				MainView.lettersTable.getItem(item).dispose();
			}});
		data.deleteLetter(index, data.IsTrash());
	}
	
	public void Preview(int index)
	{
		final StaticData data = Setting.Instance();
		final Letter letter = data.getLetter(index);
		letter.IsSeen = true;
		final Account account = data.AccountByLogin(letter.to);
		this.display.asyncExec(new Runnable() {			
			//@Override
			public void run() {	
				MainView.headerMessage.setText(letter.from+ " | " +letter.to +" | "+letter.subject+" | "+ letter.received);
				SimpleGenerator.html=stories.get(account.getId()).GetMessage(letter.msgId, data.IsTrash());
				SimpleViewer html = new SimpleViewer(MainView.sc, SWT.NONE);
				MainView.sc.setContent(html);
			}});
	}
}