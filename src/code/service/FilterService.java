package code.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import code.common.IRepository;
import code.core.Setting;
import code.core.StaticData;

public class FilterService {
	private StaticData data = Setting.Instance();
	private IRepository repo = data.getRepository();
	
	public void RepaintAccount(Table table)
	{
		table.removeAll();
		for(String f : data.getFilters())
		{
			TableItem item = new TableItem(table, SWT.LEFT);			
			item.setText(f);
		}
		table.select(0);
	}
	
	public void AddFilter(String filter)
	{
		if(!AnyFilter(filter))
		{
			repo.addFilter(filter);
			data.refresh();
		}
	}
	
	public void DeleteFilter(String filter)
	{
		repo.deleteFilter(filter);
		data.refresh();
	}
	
	private Boolean AnyFilter(String filter)
	{
		for(String f : data.getFilters())
		{
			if(filter == f)
			{
				return true;
			}
		}
		return false;
	}
}
