package window;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import swing2swt.layout.BorderLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import code.core.Setting;
import code.dto.Account;
import code.service.AccountService;

public class AccountDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textServer;
	private Text textLogin;
	private Text textPass;
	private Table table;
	private Account account;
	private Boolean isNeedAdd;
	
	private AccountService service = new AccountService();	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AccountDialog(Shell parent, Table table, Boolean isNeedAdd) {
		super(parent, 4);
		this.table = table;  
		this.isNeedAdd = isNeedAdd;
		if(table.getItemCount()>0)
		{
			account = service.getAccountById(table.getSelection()[0].getText(0));
		}
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

		shell.setSize(379, 234);
		shell.setText("\u0410\u043A\u043A\u0430\u0443\u043D\u0442");
		shell.setLayout(new BorderLayout(0, 0));
		
		Label dialogAccountHeader = new Label(shell, SWT.NONE);
		dialogAccountHeader.setAlignment(SWT.CENTER);
		dialogAccountHeader.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		dialogAccountHeader.setLayoutData(BorderLayout.NORTH);
		if(!this.isNeedAdd)
		{
			dialogAccountHeader.setText("\u0420\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435 \u0430\u043A\u043A\u0430\u0443\u043D\u0442\u0430");
		}
		else
		{
			dialogAccountHeader.setText("\u0414\u043E\u0431\u0430\u0432\u043B\u0435\u043D\u0438\u0435 \u0430\u043A\u043A\u0430\u0443\u043D\u0442\u0430");
		}
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		composite.setLayoutData(BorderLayout.CENTER);
		
		Label label = new Label(composite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		label.setBounds(10, 16, 106, 21);
		label.setText("\u0418\u043C\u044F \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
		
		textServer = new Text(composite, SWT.BORDER);
		textServer.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textServer.setBounds(122, 13, 241, 32);
		
	
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		label_1.setText("\u041B\u043E\u0433\u0438\u043D");
		label_1.setBounds(10, 58, 55, 21);
		
		textLogin = new Text(composite, SWT.BORDER);
		textLogin.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textLogin.setBounds(122, 55, 241, 32);
		textLogin.setFocus();
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		label_2.setText("\u041F\u0430\u0440\u043E\u043B\u044C");
		label_2.setBounds(10, 106, 55, 21);
		
		textPass = new Text(composite, SWT.PASSWORD | SWT.BORDER);
		textPass.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPass.setBounds(122, 103, 241, 32);
		
		if(isNeedAdd){
			textServer.setText("imap.mail.ru");
		}
		else
		{
			textServer.setText(this.account.getServer());
			textLogin.setText(account.getLogin());
			textPass.setText(account.getPass());
		}
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.SOUTH);
		
		Button btnSaveAccount = new Button(composite_1, SWT.NONE);
		btnSaveAccount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String login = textLogin.getText();
				if(textServer.getText()=="")
				{
					MessageDialog.openError(shell, "Ошибка", "Поле имя сервера не введен!");
					return;
				}
				if(textLogin.getText()=="")
				{
					MessageDialog.openError(shell, "Ошибка", "Поле логин не введен!");
					return;
				}
				else if (!login.matches("^([_A-Za-z0-9-]+)@([A-Za-z0-9]+)\\.([A-Za-z]{2,})$"))
				{
					MessageDialog.openError(shell, "Ошибка", "Поле логин введен некорректно!");
					return;					
				}
				else if(Setting.Instance().AnyAccounts(textLogin.getText(), isNeedAdd))
				{
					MessageDialog.openError(shell, "Ошибка", "такой логин уже существует!");
					return;
				}
				if(textPass.getText()=="")
				{
					MessageDialog.openError(shell, "Ошибка", "Поле пароль не введен!");
					return;
				}
				if(isNeedAdd)
				{
					service.AddAccount(textServer.getText(), textLogin.getText(), textPass.getText());
				}
				else
				{
					account.setLogin(textLogin.getText());
					account.setPass(textPass.getText());
					account.setServer(textServer.getText());	
					service.EditAccount(account);
				}
				service.RepaintAccount(table);
				shell.close();
			}
		});
		btnSaveAccount.setLocation(154, 0);
		btnSaveAccount.setSize(96, 32);
		btnSaveAccount.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		btnSaveAccount.setText("\u0421\u043E\u0445\u0440\u0430\u043D\u0438\u0442\u044C");
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setLocation(267, 0);
		btnCancel.setSize(96, 32);
		btnCancel.setText("\u041E\u0442\u043C\u0435\u043D\u0430");
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));

	}
}
