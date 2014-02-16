package code.common;

import java.util.List;

import code.dto.*;

public interface IRepository {

	//���������
	int getInterval();
	void setInterval(int period);
	
	int getCountMessage();
	void setCountMessage(int period);
	
	List<String> getFilters();
	void addFilter(String filter);
	void editFilter(String filter, String old);
	void deleteFilter(String filter);
	
	//��������� ��� ������ � ����������
	List<Account> getAccounts();
	int CountAccounts();
	Account AccountSingle(int id);
	void AddAccount(Account model);
	void EditAccount(Account model);
	void DeleteAccount(int id);
	
}
