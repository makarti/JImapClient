package code.service;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import code.core.*;

public class QueryAccountTable extends AbstractTableModel {

	private StaticData data = Setting.Instance();
	//private Vector textData = new Vector();
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.getAccounts().size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return data.getAccounts().get(arg0);
	}

}
