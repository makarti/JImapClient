package window;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import code.service.FilterService;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class filterDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;
	private Text textFilter;
	private FilterService service = new FilterService();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public filterDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(444, 413);
		shell.setText("\u0424\u0438\u043B\u044C\u0442\u0440\u044B \u043F\u0438\u0441\u0435\u043C");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		composite.setBounds(0, 0, 440, 348);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("\u0424\u0438\u043B\u044C\u0442\u0440\u044B");
		label.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label.setBounds(10, 87, 148, 31);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL && MessageDialog.openConfirm(shell, "Удаление", "Вы точно хотите удалить?")) {
                    service.DeleteFilter(table.getSelection()[0].getText());
                    service.RepaintAccount(table);
                }
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 124, 406, 210);
		service.RepaintAccount(table);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(372);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblNewLabel.setBounds(10, 23, 119, 22);
		lblNewLabel.setText("\u041D\u043E\u0432\u044B\u0439 \u0444\u0438\u043B\u044C\u0442\u0440");
		
		textFilter = new Text(composite, SWT.BORDER);
		textFilter.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		textFilter.setBounds(10, 51, 297, 30);
		
		Button addFilter = new Button(composite, SWT.NONE);
		addFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.AddFilter(textFilter.getText());				
				service.RepaintAccount(table);
				textFilter.setText("");
			}
		});
		addFilter.setText("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C");
		addFilter.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		addFilter.setBounds(313, 50, 103, 31);
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.setBounds(312, 353, 103, 31);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnClose.setText("\u0417\u0430\u043A\u0440\u044B\u0442\u044C");
		btnClose.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
	}

}
