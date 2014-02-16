package mail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import cop.swt.widgets.viewers.html.SimpleHtmlViewer;

public class SimpleViewer extends Composite
{
	private SimpleHtmlViewer htmlViewer;

	public SimpleViewer(Composite parent, int style)
	{
		super(parent, style);

		setLayout(new GridLayout());
		//setBackground(WHITE);

		createPartControl();
	}

	protected SimpleHtmlViewer getHtmlViewer()
	{
		return htmlViewer;
	}

	private void createPartControl()
	{
		createHtmlViewerPart();
		refresh();
	}

	private void createHtmlViewerPart()
	{
		htmlViewer = new SimpleHtmlViewer(this, SWT.NONE);
		htmlViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	public void refresh()
	{
		refreshCore();
	}

	protected void refreshCore()
	{
		htmlViewer.clear();
		htmlViewer.print(SimpleGenerator.html);
	}
}
