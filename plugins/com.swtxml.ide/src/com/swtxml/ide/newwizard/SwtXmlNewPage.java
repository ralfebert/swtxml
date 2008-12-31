/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/

package com.swtxml.ide.newwizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtxml.views.SwtXmlComposite;
import com.swtxml.views.SwtXmlDialog;
import com.swtxml.views.SwtXmlEditorPart;
import com.swtxml.views.SwtXmlTitleAreaDialog;
import com.swtxml.views.SwtXmlViewPart;
import com.swtxml.views.SwtXmlWindow;
import com.swtxml.views.SwtXmlWizardPage;

public class SwtXmlNewPage extends NewTypeWizardPage {

	private Class<?>[] swtXmlClasses = new Class<?>[] { SwtXmlComposite.class, SwtXmlDialog.class,
			SwtXmlEditorPart.class, SwtXmlTitleAreaDialog.class, SwtXmlViewPart.class,
			SwtXmlWindow.class, SwtXmlWizardPage.class };

	public SwtXmlNewPage() {
		super(true, "New SWT/XML Class");
		setTitle("New SWT/XML Class");
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());

		int nColumns = 4;
		composite.setLayout(new GridLayout(nColumns, false));

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);

		createSeparator(composite, nColumns);

		createTypeNameControls(composite, nColumns);

		Label type = new Label(composite, SWT.NONE);
		type.setText("Base class:");

		Composite group = new Composite(composite, SWT.NONE);
		group.setLayout(new RowLayout());

		SelectionAdapter selectSuperClassListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object data = ((Button) e.widget).getData();
				String classname = ((Class<?>) data).getName();
				setSuperClass(classname, false);
			}
		};

		for (Class<?> clazz : swtXmlClasses) {
			Button btn = new Button(group, SWT.RADIO);
			btn.setText(clazz.getSimpleName().replace("SwtXml", ""));
			btn.setData(clazz);
			btn.addSelectionListener(selectSuperClassListener);
			if (SwtXmlComposite.class.equals(clazz)) {
				btn.setSelection(true);
				setSuperClass(SwtXmlComposite.class.getName(), false);
			}
		}

		setControl(composite);
	}

	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();
	}

	private void doStatusUpdate() {
		updateStatus(new IStatus[] { fContainerStatus, fPackageStatus, fTypeNameStatus });
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		doStatusUpdate();
	}

}