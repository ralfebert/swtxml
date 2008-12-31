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
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

	private IEditorPart trackedPart;

	private final IPropertyListener updatePreviewOnSave = new IPropertyListener() {

		public void propertyChanged(Object source, int propId) {
			if (propId == IEditorPart.PROP_DIRTY) {
				if (!trackedPart.isDirty()) {
					updatePreview();
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
	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
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
		updatePreview();
	}

	public void clearConnection() {
		trackedPart.removePropertyListener(updatePreviewOnSave);
		setContent(null);
	}

	private void updatePreview() {
		IDocument doc = (IDocument) trackedPart.getAdapter(IDocument.class);
		IDocumentExtension4 document = (IDocumentExtension4) doc;
		if (document.getModificationStamp() != lastPreviewModificationStamp) {
			try {
				Composite newContent = new Composite(parent, SWT.None);
				newContent.setLayout(new FillLayout());
				IEditorInput editorInput = trackedPart.getEditorInput();
				ILocationProvider locationProvider = (ILocationProvider) editorInput
						.getAdapter(ILocationProvider.class);
				File file = (locationProvider == null) ? null : locationProvider.getPath(
						editorInput).toFile();

				SwtXmlParser parser = new SwtXmlParser(newContent, new PreviewResource(file, doc),
						null);
				parser.parse();
				setContent(newContent);
			} catch (Exception e) {
				Activator.getDefault().getLog().log(
						new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				setContent(new ErrorComposite(parent, SWT.NONE, e));
			} finally {
				lastPreviewModificationStamp = document.getModificationStamp();
				parent.layout();
			}
		}
	}

	/**
	 * Disposes all elements except the new content. content might be null to
	 * clear preview.
	 */
	private void setContent(Composite content) {
		for (Control c : parent.getChildren()) {
			if (c != content) {
				c.dispose();
			}
		}
	}

	@Override
	public void setFocus() {

	}

}
