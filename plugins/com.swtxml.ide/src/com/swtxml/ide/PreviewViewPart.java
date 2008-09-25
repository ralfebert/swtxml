package com.swtxml.ide;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class PreviewViewPart extends ViewPart implements ISelectionListener {

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		new Button(parent, SWT.NONE);
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		getSite().getWorkbenchWindow().getSelectionService().removePostSelectionListener(this);
	}

	@Override
	public void setFocus() {

	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println(part);
		System.out.println(selection);
	}

}
