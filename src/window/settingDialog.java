package window;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import code.service.SettingService;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class settingDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private SettingService service = new SettingService();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public settingDialog(Shell parent, int style) {
		super(parent, style);
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
		shell.setSize(355, 181);
		shell.setText("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		
		final Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setMinimum(5);
		spinner.setBounds(276, 44, 47, 22);
		spinner.setSelection(service.getInterval());
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblNewLabel.setBounds(32, 43, 238, 22);
		lblNewLabel.setText("\u0427\u0430\u0441\u0442\u043E\u0442\u0430 \u043E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u044F \u0432 \u043C\u0438\u043D\u0443\u0442\u0430\u0445");
		
		Button btnSavePeriod = new Button(shell, SWT.NONE);
		btnSavePeriod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.savePeriod(spinner.getText());
				shell.close();
			}
		});
		btnSavePeriod.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		btnSavePeriod.setBounds(32, 111, 168, 31);
		btnSavePeriod.setText("\u0421\u043E\u0445\u0440\u0430\u043D\u0438\u0442\u044C \u043D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		button.setText("\u041E\u0442\u043C\u0435\u043D\u0430");
		button.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		button.setBounds(212, 111, 111, 31);

	}
}
