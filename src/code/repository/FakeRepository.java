package code.repository;

import java.util.Arrays;
import java.util.List;

import code.common.IRepository;
import code.dto.*;


public class FakeRepository implements IRepository{

	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public List<String> getFilters() {
		// TODO Auto-generated method stub
		return Arrays.asList("spam", "trash");
	}

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		Account a1 = new Account();
		a1.setId(1);
		a1.setLogin("artur_inter@rambler.ru");
		a1.setPass("1ArtuR17CoM7");
		a1.setServer("imap.rambler.ru");
		a1.setIsInclude(true);
		
		Account a2 = new Account();
		a2.setId(3);
		a2.setLogin("makvearti@gmail.com");
		a2.setPass("1ArtuR17CoM7");
		a2.setServer("imap.gmail.com");
		a2.setIsInclude(false);
		
		return Arrays.asList(a1, a2);
	}

	

	@Override
	public int CountAccounts() {
		// TODO Auto-generated method stub
		return 2;
	}


	@Override
	public void setInterval(int period) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFilter(String filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editFilter(String filter, String old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFilter(String filter) {
		// TODO Auto-generated method stub
		
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

	@Override
	public Account AccountSingle(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void AddAccount(Account model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void EditAccount(Account model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteAccount(int id) {
		// TODO Auto-generated method stub
		
	}

	

}
