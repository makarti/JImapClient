package code.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import code.common.DateComparator;
import code.common.IRepository;
import code.dto.Account;
import code.dto.Letter;
import code.repository.SqliteRepository;

public class StaticData {
	
	private IRepository repository;
	
	private int interval;
	
	private List<String> filters;
	
	private List<Account> accounts;
	
	public int getInboxcount() {
		return inboxcount;
	}
	
	public int getCount()
	{
		return this.active.size();
	}
	
	public void setInboxcount(int inboxcount) {
		this.inboxcount = inboxcount;
	}

	public int getTrashcount() {
		return trashcount;
	}

	public void setTrashcount(int trashcount) {
		this.trashcount = trashcount;
	}

	private int inboxcount = 0;
	private int trashcount = 0;

	private Map<Integer, Letter> inbox;	
	
	private Map<Integer, Letter> trash;
	
	private Map<Integer, Letter> active;
	
	private Boolean isTrash = false;
	
	public void SetTab(Boolean isTrash)
	{
		this.isTrash = isTrash;
		if(isTrash)
		{Sorting(this.trash);}else{ Sorting(this.inbox);}
	}
	
	public Boolean anyLetter(String id, Boolean isTrash)
	{		
		if(isTrash)
		{
			return trash.containsKey(id);
		}
		else
		{
			return inbox.containsKey(id);
		}
	}
	
	public Boolean anyLetter(String id)
	{
		return active.containsKey(id);	
	}
	
	public Letter getLetter(int row)
	{
		return this.active.get(row);
	}
	
	public String[] getLetterByString(int row){
		Letter l = active.get(row);
		return new String[]{Integer.toString(row), l.to, l.from, l.subject, l.created, l.received};
	}	
	
	public Iterator getKeysIterator(){
		//Collection c = this.active.keySet();
		return this.active.keySet().iterator();
	}

	public void addLetter(Letter letter, Boolean isTrash)
	{
		if(isTrash)
			trash.put(trashcount++, letter);
		else
			inbox.put(inboxcount++, letter);
	}
	
	public Map<Integer, Letter> getInbox() {
		return inbox;
	}

	public void setInbox(Map<Integer, Letter> inbox) {
		this.inbox = inbox;
	}

	public Map<Integer, Letter> getTrash() {
		return trash;
	}

	public void setTrash(Map<Integer, Letter> trash) {
		this.trash = trash;
	}
	
	public Boolean isSeen(int key)
	{
		return active.get(key).IsSeen;
	}
	
	public void UpdateSort()
	{
		if(isTrash)
		{Sorting(this.trash);}else{ Sorting(this.inbox);}
	}
	
	public void Sorting(Map<Integer, Letter> m)
	{
		this.active = new TreeMap<Integer, Letter>(new DateComparator(m));
		active.putAll(m);
	}
	
	public void deleteLetter(int id, Boolean IsTrash)
	{
		if(isTrash)
		{
			trash.remove(id);
		}
		else
		{
			this.addLetter(inbox.get(id), true);
			inbox.remove(id);
		}
		this.UpdateSort();
	}
	
	private Boolean isViewAccount = false;
	
	public StaticData()
	{
		this.repository = new SqliteRepository();
		this.inbox = new HashMap<Integer, Letter>();
		this.trash = new HashMap<Integer, Letter>();				
	}
	
	public void refresh()
	{
		this.interval = repository.getInterval();
		this.filters = repository.getFilters();
		this.accounts = repository.getAccounts();
	}
	
	public Boolean getStatusTabAccount()
	{
		return this.isViewAccount;
	}
	
	public void setStatusTabAccount()
	{
		this.isViewAccount = true;
	}
	
	public IRepository getRepository() {
		return repository;
	}

	public int getInterval() {
		return interval;
	}

	public List<String> getFilters() {
		return filters;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	public Boolean AnyAccounts(String login, Boolean isDetect)
	{
		int count = isDetect?0:1;
		for(Account account : accounts)
		{
			if(account.getLogin().compareToIgnoreCase(login)==0 && --count<0)
				return true;
		}
		return false;
	}
	
	public Account AccountById(int id)
	{
		for(Account account : accounts)
		{
			if(account.getId()==id)
				return account;
		}
		return null;
	}
	
	public Account AccountByLogin(String login)	
	{
		for(Account account : accounts)
		{
			if(account.getLogin()==login)
				return account;
		}
		return null;
	}
	public Boolean IsTrash()
	{
		return isTrash;
	}
}
