package code.repository;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import code.common.IRepository;
import code.core.Setting;
import code.dto.Account;

public class SqliteRepository implements IRepository{

	private SqliteBD db = SqliteBD.getInstance();
	private Enumeration<String> res;
	
	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		db.prepareQuery("select * from setting");
		if((res = db.fetch())!= null){
			res.nextElement();
			return Integer.parseInt(res.nextElement());
		}
		return 5;
	}
	
	@Override	
	public void setInterval(int period)
	{		
		db.exec("update setting set period="+period);
		Setting.Instance().refresh();
	}

	@Override
	public List<String> getFilters() {
		// TODO Auto-generated method stub
		db.prepareQuery("select * from filters");
		List<String> list = new ArrayList<String>();
		while((res = db.fetch())!= null){
			res.nextElement();
			list.add(res.nextElement());
		}
		return list;
	}
	
	@Override
	public void addFilter(String filter) {
		// TODO Auto-generated method stub
		db.exec("insert into filters values (NULL, '"+filter+"');");
		Setting.Instance().refresh();
	}

	@Override
	public void editFilter(String filter, String old) {
		// TODO Auto-generated method stub
		db.exec("update from filters set value = '"+filter+"' where value = '"+old+"';");
		Setting.Instance().refresh();
	}

	@Override
	public void deleteFilter(String filter) {
		// TODO Auto-generated method stub
		db.exec("delete from filters where value='"+filter+"';");
		Setting.Instance().refresh();
	}
	

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		List<Account> list = new ArrayList<Account>();
		db.prepareQuery("select * from Account");
		while((res=db.fetch())!=null)
		{
			Account account = new Account(Integer.parseInt(res.nextElement()), res.nextElement(), res.nextElement(), res.nextElement(), Boolean.parseBoolean(res.nextElement()));
			list.add(account);
		}
		return list;
	}

	@Override
	public int CountAccounts() {
		// TODO Auto-generated method stub
		return Setting.Instance().getAccounts().size();
	}

	@Override
	public Account AccountSingle(int id) {
		// TODO Auto-generated method stub
		db.prepareQuery("select * from Account where id='"+id+"'");
		if((res=db.fetch())!=null)
		{
			return new Account(Integer.parseInt(res.nextElement()), res.nextElement(), res.nextElement(), res.nextElement(), Boolean.parseBoolean(res.nextElement()));
		}
		return null;
	}

	@Override
	public void AddAccount(Account model) {
		// TODO Auto-generated method stub
		db.exec("INSERT INTO Account VALUES(NULL, '"+ model.getLogin()
				+"', '"+ model.getPass()
				+"', '" +model.getServer()
				+"', '" + model.IsInclude()
				+"');");
		Setting.Instance().refresh();
	}

	@Override
	public void EditAccount(Account model) {
		// TODO Auto-generated method stub
		db.exec("update Account set login = '"+ model.getLogin()
				+ "', pass = '"+model.getPass()
				+ "', server = '"+ model.getServer()
				+ "', isInclude = '" + model.IsInclude()
				+ "' where id = "+ model.getId()+";");
		Setting.Instance().refresh();
	}
	
	@Override
	public void DeleteAccount(int id) {
		// TODO Auto-generated method stub
		db.exec("delete from Account where id= '"+ id +";");
		Setting.Instance().refresh();
	}

	@Override
	public int getCountMessage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCountMessage(int period) {
		// TODO Auto-generated method stub
		
	}





}
