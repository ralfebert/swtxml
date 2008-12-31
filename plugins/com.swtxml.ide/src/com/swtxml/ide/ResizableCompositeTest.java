package com.swtxml.ide;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ResizableCompositeTest {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());

		ResizableComposite comp = new ResizableComposite(shell);
		Composite resizeableComposite = comp.getResizeableComposite();
		resizeableComposite.setLayout(new FillLayout());
		Button btn = new Button(resizeableComposite, SWT.NONE);
		btn.setText("Test");
		comp.resetSize();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
