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
package com.swtxml.ide;

import java.io.File;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

import com.swtxml.swt.SwtXmlParser;

@SuppressWarnings("restriction")
public class PreviewViewPart extends ViewPart {

	public static final String VIEW_ID = PreviewViewPart.class.getName();

	private IEditorPart trackedPart;

	private final IPropertyListener updatePreviewOnSave = new IPropertyListener() {

		public void propertyChanged(Object source, int propId) {
			if (propId == IEditorPart.PROP_DIRTY) {
				if (!trackedPart.isDirty()) {
					updatePreview(false);
				}
			}
		}

	};

	private final IPartListener trackRelevantEditorsPartListener = new IPartListener() {

		public void partActivated(IWorkbenchPart part) {
			tryConnectTo(part);
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
			if (part == trackedPart) {
				clearConnection();
			}
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}

	};

	private long lastPreviewModificationStamp;
	private Composite container;

	private Action resizeAction;

	@Override
	public void createPartControl(final Composite parent) {
		resizeAction = new Action("Resize", SWT.TOGGLE) {

			@Override
			public void run() {
				updatePreview(true);
			}
		};
		resizeAction.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"/icons/resize.png"));

		GridLayoutFactory.fillDefaults().numColumns(2).spacing(0, 0).applyTo(parent);
		ToolBar toolbar = new ToolBar(parent, SWT.VERTICAL | SWT.FLAT);
		toolbar.setBackground(toolbar.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		ToolBarManager toolbarManager = new ToolBarManager(toolbar);
		toolbarManager.add(resizeAction);
		GridDataFactory.fillDefaults().grab(false, true).align(SWT.BEGINNING, SWT.FILL).applyTo(
				toolbar);
		toolbarManager.update(true);

		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
				.applyTo(container);

		getSite().getPage().addPartListener(trackRelevantEditorsPartListener);
		tryConnectTo(getSite().getPage().getActiveEditor());
	}

	@Override
	public void dispose() {
		getSite().getPage().removePartListener(trackRelevantEditorsPartListener);
		clearConnection();
		super.dispose();
	}

	private void tryConnectTo(IWorkbenchPart part) {
		if (part != trackedPart && part instanceof IEditorPart) {
			IStructuredModel model = (IStructuredModel) part.getAdapter(IStructuredModel.class);
			if (model != null
					&& SwtXmlModelHandler.associatedContentTypeID.equals(model
							.getContentTypeIdentifier())) {
				connectTo(part);
			}
		}
	}

	private void connectTo(IWorkbenchPart part) {
		if (trackedPart != null) {
			clearConnection();
		}
		trackedPart = (IEditorPart) part;
		part.addPropertyListener(updatePreviewOnSave);
		updatePreview(false);
	}

	public void clearConnection() {
		trackedPart.removePropertyListener(updatePreviewOnSave);
		setContent(null);
	}

	private void updatePreview(boolean force) {
		IDocument doc = (IDocument) trackedPart.getAdapter(IDocument.class);
		IDocumentExtension4 document = (IDocumentExtension4) doc;
		if (document.getModificationStamp() != lastPreviewModificationStamp || force) {
			try {
				ResizableComposite resizableComposite = null;
				Composite newComposite;
				Composite swtXmlComposite;

				if (resizeAction.isChecked()) {
					resizableComposite = new ResizableComposite(container);
					newComposite = resizableComposite;
					swtXmlComposite = resizableComposite.getResizeableComposite();
				} else {
					newComposite = swtXmlComposite = new Composite(container, SWT.None);
				}

				IEditorInput editorInput = trackedPart.getEditorInput();
				ILocationProvider locationProvider = (ILocationProvider) editorInput
						.getAdapter(ILocationProvider.class);
				File file = (locationProvider == null) ? null : locationProvider.getPath(
						editorInput).toFile();

				SwtXmlParser parser = new SwtXmlParser(swtXmlComposite, new PreviewResource(file,
						doc), null);
				parser.parse();
				setContent(newComposite);

				if (resizableComposite != null) {
					resizableComposite.resetSize();
				}
			} catch (Exception e) {
				Activator.getDefault().getLog().log(
						new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				setContent(new ErrorComposite(container, SWT.NONE, e));
			} finally {
				lastPreviewModificationStamp = document.getModificationStamp();
				container.layout();
			}
		}
	}

	/**
	 * Disposes all elements except the new content. content might be null to
	 * clear preview.
	 */
	private void setContent(Composite content) {
		for (Control c : container.getChildren()) {
			if (c != content) {
				c.dispose();
			}
		}
	}

	@Override
	public void setFocus() {
		if (container != null) {
			container.setFocus();
		}
	}

}
