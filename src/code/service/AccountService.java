package code.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import code.common.IRepository;
import code.core.Setting;
import code.core.StaticData;
import code.dto.Account;

public class AccountService {

	private StaticData data = Setting.Instance();
	private IRepository repo = data.getRepository();
	
	public void RepaintAccount(Table table)
	{
		table.removeAll();
		for(Account account : data.getAccounts())
		{
			TableItem item = new TableItem(table, SWT.LEFT);			
			item.setText(new String[]{Integer.toString(account.getId()), account.getLogin(), account.getIsInclude().toString()});
		}
		table.select(0);
	}
	
	public void AddAccount(String server, String login, String pass)
	{
		repo.AddAccount(new Account(0, login, pass, server, true));
	}
	
	public void EditAccount(Account account)
	{
		UpdateAccount(account);
	}
	
	public void toogleIncludeAccount(String id)
	{
		Account account = getAccountById(id);
		account.setIsInclude(!account.IsInclude());
		UpdateAccount(account);
	}
	
	public Account getAccountById(String id)
	{
		int accountid = Integer.parseInt(id);
		return data.AccountById(accountid);
	}
	
	public void DeleteAccount(String id)
	{
		int accountid = Integer.parseInt(id);
		repo.DeleteAccount(accountid);
		data.refresh();
	}
	
	private void UpdateAccount(Account account)
	{		
		repo.EditAccount(account);
		data.refresh();
	}
}
