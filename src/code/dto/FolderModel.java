package code.dto;

import java.util.Date;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

import code.core.Setting;

public class FolderModel extends AbstractTableModel {
	
	String[]	columnNames = { "id", "От", "Кому", "Тема", "Создано", "Получено"}; 
    Class[]	columnTypes = { Integer.class, String.class, String.class, String.class, Date.class }; 
    Map<Integer, Letter> letters;
	
    public void init(Boolean IsTrash)
    {
    	if(IsTrash)
    	{
    		letters = Setting.Instance().getTrash();
    	}
    	else
    	{
    		letters = Setting.Instance().getInbox();
    	}
    	fireTableDataChanged();
    }
    
	@Override	
    public String getColumnName(int column) {
    	return columnNames[column];
    }
	
	@Override
    public Class getColumnClass(int column) {
    	return columnTypes[column];
    }
	
	@Override
    public int getColumnCount() {
    	return columnNames.length; 
    }

	@Override
	public int getRowCount() {	
		return letters.size();
	}

	@Override
	public Object getValueAt(int aRow, int aColumn) {
		switch(aColumn) {
		case 0:	return letters.get(aRow).id;
		case 1: return letters.get(aRow).from;
		case 2: return letters.get(aRow).to;
		case 3: return letters.get(aRow).subject;
		case 4: return letters.get(aRow).created;
		case 5: return letters.get(aRow).received;
		default:
		    return "";		
	    }
	}
}