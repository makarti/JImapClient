package window;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

import code.core.MailBox;
import code.core.Setting;
import code.core.SortListenerFactory;
import code.service.AccountService;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.swt.custom.ScrolledComposite;

public class MainView implements IDisposable{

	private MailBox box;
	protected Shell shlMailview;
	public static Table lettersTable;
	public static ProgressBar progressBar;
	private Table accountsTable;
	private AccountService service = new AccountService();
	private Thread myThready;
	public static ScrolledComposite sc;
	
	private Runnable timer = new Runnable() {
		
		@Override
		public void run() {
			while(true)
			{
				box.start();
				try {
					Thread.sleep(Setting.Instance().getInterval()*60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	public static Text headerMessage;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainView window = new MainView();			
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlMailview.open();
		shlMailview.layout();
		
		box = new MailBox(display);		
		
		myThready = new Thread(timer);
		myThready.setDaemon(true);
        myThready.start();
		
		while (!shlMailview.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shlMailview = new Shell();
		shlMailview.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				dispose();
			}
		});
		shlMailview.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		shlMailview.setSize(1124, 800);
		shlMailview.setText("MailView");
		shlMailview.setLayout(new BorderLayout(0, 0));		
		
		Menu menu = new Menu(shlMailview, SWT.BAR);
		shlMailview.setMenuBar(menu);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("\u0413\u043B\u0430\u0432\u043D\u0430\u044F");
		
		Menu mainMenu = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(mainMenu);
		
		MenuItem menuItem = new MenuItem(mainMenu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myThready.stop();// interrupt();
				myThready = new Thread(timer);
				myThready.setDaemon(true);
				myThready.start();
			}
		});
		menuItem.setText("\u041E\u0431\u043D\u043E\u0432\u0438\u0442\u044C");
		
		MenuItem exitMenu = new MenuItem(mainMenu, SWT.NONE);
		exitMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				shlMailview.close();
			}
		});
		exitMenu.setText("\u0412\u044B\u0445\u043E\u0434");
		
		MenuItem filtrMenu = new MenuItem(menu, SWT.NONE);
		filtrMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filterDialog fd = new filterDialog(shlMailview, 0);
				fd.open();
			}
		});
		filtrMenu.setText("\u0424\u0438\u043B\u044C\u0442\u0440");
		
		MenuItem settingsMenu = new MenuItem(menu, SWT.NONE);
		settingsMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				settingDialog sd = new settingDialog(shlMailview, 0);
				sd.open();
			}
		});
		settingsMenu.setText("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		
		final TabFolder tabFolder = new TabFolder(shlMailview, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tabFolder.getSelectionIndex() == 1)
				{
					service.RepaintAccount(accountsTable);
				}
			}
		});
		tabFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		
		TabItem lettersTab = new TabItem(tabFolder, SWT.NONE);
		lettersTab.setText("\u041F\u0438\u0441\u044C\u043C\u0430");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		lettersTab.setControl(composite_2);
		composite_2.setLayout(new FormLayout());
		
		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		FormData fd_composite_4 = new FormData();
		fd_composite_4.bottom = new FormAttachment(0, 81);
		fd_composite_4.top = new FormAttachment(0);
		fd_composite_4.left = new FormAttachment(0);
		fd_composite_4.right = new FormAttachment(0, 1100);
		composite_4.setLayoutData(fd_composite_4);
		
		Composite composite_5 = new Composite(composite_2, SWT.NONE);
		composite_5.setLayout(new BorderLayout(0, 0));
		FormData fd_composite_5 = new FormData();
		fd_composite_5.bottom = new FormAttachment(composite_4, 281, SWT.BOTTOM);
		fd_composite_5.left = new FormAttachment(composite_4, 0, SWT.LEFT);
		fd_composite_5.right = new FormAttachment(composite_4, 0, SWT.RIGHT);
		fd_composite_5.top = new FormAttachment(composite_4, 6);
		composite_5.setLayoutData(fd_composite_5);
		
		Composite composite_10 = new Composite(composite_2, SWT.NONE);
		FormData fd_composite_10 = new FormData();
		fd_composite_10.top = new FormAttachment(composite_5, 6);
		fd_composite_10.bottom = new FormAttachment(100, -10);
		
		lettersTable = new Table(composite_5, SWT.BORDER | SWT.FULL_SELECTION);
		lettersTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lettersTable.getItems().length>0)
				{					
					TableItem item = lettersTable.getSelection()[0];
					item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NONE));
					box.Preview(Integer.parseInt(item.getText(0)));
				}
			}
		});
		lettersTable.setLinesVisible(true);
		lettersTable.setHeaderVisible(true);
		TableColumn msgId = new TableColumn(lettersTable, SWT.NONE);
		msgId.setResizable(false);
		msgId.setText("ID");
		
		TableColumn from = new TableColumn(lettersTable, SWT.NONE);
		from.setWidth(105);
		from.setText("\u041E\u0442");
		from.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
		
		TableColumn to = new TableColumn(lettersTable, SWT.NONE);
		to.setWidth(111);
		to.setText("\u041A\u043E\u043C\u0443");
		to.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
		
		TableColumn subject = new TableColumn(lettersTable, SWT.NONE);
		subject.setWidth(406);
		subject.setText("\u0422\u0435\u043C\u0430");
		subject.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.STRING_COMPARATOR));
		
		TableColumn created = new TableColumn(lettersTable, SWT.NONE);
		created.setWidth(176);
		created.setText("\u0421\u043E\u0437\u0434\u0430\u043D\u043E");
		created.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.DATE_COMPARATOR));
		
		TableColumn received = new TableColumn(lettersTable, SWT.NONE);
		received.setWidth(194);
		received.setText("\u041F\u043E\u043B\u0443\u0447\u0435\u043D\u043E");
		received.addListener(SWT.Selection, SortListenerFactory.getListener(SortListenerFactory.DATE_COMPARATOR));		
		
		Menu popupMenuLetter = new Menu(lettersTable);
		lettersTable.setMenu(popupMenuLetter);
		
		MenuItem miUpdate = new MenuItem(popupMenuLetter, SWT.NONE);
		miUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myThready.stop();// interrupt();
				myThready = new Thread(timer);
				myThready.setDaemon(true);
				myThready.start();
			}
		});
		miUpdate.setText("\u041E\u0431\u043D\u043E\u0432\u0438\u0442\u044C");
		
		final MenuItem miDelete = new MenuItem(popupMenuLetter, SWT.NONE);
		miDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lettersTable.getItems().length>0)
				{				
					TableItem item = lettersTable.getSelection()[0];
					box.deleteMessage(Integer.parseInt(item.getText(0)), lettersTable.getSelectionIndex());
				}
			}
		});
		miDelete.setText("\u0412 \u043A\u043E\u0440\u0437\u0438\u043D\u0443");
		fd_composite_10.right = new FormAttachment(composite_4, 0, SWT.RIGHT);
		
		final Button btnInbox = new Button(composite_4, SWT.NONE);
		final Button btnTrash = new Button(composite_4, SWT.NONE);
		
		btnInbox.setBounds(10, 10, 146, 39);
		btnInbox.setEnabled(false);
		btnInbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Setting.Instance().SetTab(false);				
				btnInbox.setEnabled(false);
				miDelete.setText("\u0412 \u043A\u043E\u0440\u0437\u0438\u043D\u0443");
				btnTrash.setEnabled(true);
				box.rePaint();
			}
		});
		btnInbox.setText("\u0412\u0445\u043E\u0434\u044F\u0449\u0438\u0435");
		btnTrash.setLocation(170, 10);
		btnTrash.setSize(146, 39);
		btnTrash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Setting.Instance().SetTab(true);
				btnInbox.setEnabled(true);
				miDelete.setText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C");
				btnTrash.setEnabled(false);
				box.rePaint();
			}
		});
		btnTrash.setText("\u041A\u043E\u0440\u0437\u0438\u043D\u0430");
		
		Button deleteLetter = new Button(composite_4, SWT.NONE);
		deleteLetter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lettersTable.getItems().length>0)
				{				
					TableItem item = lettersTable.getSelection()[0];
					box.deleteMessage(Integer.parseInt(item.getText(0)), lettersTable.getSelectionIndex());
				}
			}
		});
		deleteLetter.setLocation(327, 11);
		deleteLetter.setSize(146, 37);
		deleteLetter.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		deleteLetter.setText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C");
		
		Label label = new Label(composite_4, SWT.NONE);
		label.setLocation(501, 17);
		label.setSize(74, 27);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		label.setText("\u0410\u043A\u043A\u0430\u0443\u043D\u0442");
		
		Combo listAccounts = new Combo(composite_4, SWT.NONE);
		listAccounts.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		listAccounts.setLocation(583, 19);
		listAccounts.setSize(180, 23);
		listAccounts.setItems(new String[] {"\u0412\u0441\u0435"});
		listAccounts.select(0);
		
		progressBar = new ProgressBar(composite_4, SWT.NONE);
		progressBar.setBounds(10, 64, 263, 17);
		fd_composite_10.left = new FormAttachment(0);
		composite_10.setLayoutData(fd_composite_10);
		
		headerMessage = new Text(composite_10, SWT.BORDER);
		headerMessage.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		headerMessage.setBounds(0, 0, 1100, 79);
		
		sc = new ScrolledComposite(composite_10, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setBounds(0, 85, 1100, 230);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);		
		
		TabItem accountsTab = new TabItem(tabFolder, SWT.NONE);
		accountsTab.setText("\u0410\u043A\u043A\u0430\u0443\u043D\u0442\u044B");
		
		Composite accountComposite = new Composite(tabFolder, SWT.NONE);
		accountsTab.setControl(accountComposite);
		accountComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		
			Label labelListAccounts = new Label(accountComposite, SWT.NONE);
			labelListAccounts.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
			labelListAccounts.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
			labelListAccounts.setBounds(10, 37, 148, 31);
			labelListAccounts.setText("\u0421\u043F\u0438\u0441\u043E\u043A \u0430\u043A\u043A\u0430\u0443\u043D\u0442\u043E\u0432");
			
			Composite Actions = new Composite(accountComposite, SWT.NONE);
			Actions.setBounds(412, 71, 163, 234);
			
			Button addAccount = new Button(Actions, SWT.NONE);
			addAccount.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					AccountDialog ad = new AccountDialog(shlMailview, accountsTable, true);
					ad.open();
				}
			});
			addAccount.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			addAccount.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
			addAccount.setBounds(10, 68, 141, 47);
			addAccount.setText("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C");
			
			Button editAccount = new Button(Actions, SWT.NONE);
			editAccount.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(accountsTable.getItems().length>0)
					{
					AccountDialog ad = new AccountDialog(shlMailview, accountsTable, false);
					ad.open();
					}
				}
			});
			editAccount.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
			editAccount.setBounds(10, 121, 141, 47);
			editAccount.setText("\u0418\u0437\u043C\u0435\u043D\u0438\u0442\u044C");
			
			Button deleteAccount = new Button(Actions, SWT.NONE);
			deleteAccount.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(accountsTable.getItems().length>0)
					{
						TableItem item = accountsTable.getSelection()[0];
						if(MessageDialog.openConfirm(shlMailview, "Удаление", "Вы хотите удалить учетную запись " + item.getText(1)+"?"))
						{
							service.DeleteAccount(item.getText(0));
							service.RepaintAccount(accountsTable);
						}
					}
				}
			});
			deleteAccount.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));			
			deleteAccount.setBounds(10, 174, 141, 47);
			deleteAccount.setText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C");
			
			final Button Include = new Button(Actions, SWT.NONE);
			Include.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(accountsTable.getItems().length>0)
					{
						TableItem item = accountsTable.getSelection()[0];
						service.toogleIncludeAccount(item.getText(0));
						service.RepaintAccount(accountsTable);
					}
				}
			});
			Include.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
			Include.setBounds(10, 10, 141, 47);
			Include.setText("\u0412\u043A\u043B\u044E\u0447\u0438\u0442\u044C");

			accountsTable = new Table(accountComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
			accountsTable.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(accountsTable.getSelection()[0].getText(2)=="Включен")
					{
						Include.setText("\u0412\u044B\u043A\u043B\u044E\u0447\u0438\u0442\u044C");
					}
					else
					{
						Include.setText("\u0412\u043A\u043B\u044E\u0447\u0438\u0442\u044C");
					}
				}
			});

			accountsTable.setBounds(10, 74, 396, 296);
			accountsTable.setHeaderVisible(true);
			accountsTable.setLinesVisible(true);
			
			TableColumn tblclmnId = new TableColumn(accountsTable, SWT.NONE);
			tblclmnId.setWidth(35);
			tblclmnId.setText("ID");
			//accountsTable.setRedraw(false);
			
			TableColumn columnLogin = new TableColumn(accountsTable, SWT.LEFT);
			columnLogin.setWidth(244);
			columnLogin.setText("\u0410\u043A\u043A\u0430\u0443\u043D\u0442");
			
			TableColumn columnState = new TableColumn(accountsTable, SWT.LEFT);
			columnState.setWidth(100);
			columnState.setText("\u0421\u043E\u0441\u0442\u043E\u044F\u043D\u0438\u0435");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		box.close();
		this.myThready.interrupt();
		System.exit(0);
	}
}
