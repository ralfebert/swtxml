/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.ide;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.swtxml.swt.SwtXmlParser;

public class PreviewViewPart extends ViewPart implements ISelectionListener {

	private Composite parent;
	private String lastDocumentHash;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		this.parent = parent;
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
		Object document = part.getAdapter(IDocument.class);
		if (document != null) {
			Control[] children = parent.getChildren();
			byte[] documentBytes = ((IDocument) document).get().getBytes();
			String hash = sha1Hash(documentBytes);
			if (!hash.equals(lastDocumentHash)) {
				System.out.println("refreshing preview");
				lastDocumentHash = hash;
				new SwtXmlParser(parent, this).parse("xxx", new ByteArrayInputStream(
						documentBytes));
				for (Control c : children) {
					c.dispose();
				}
				parent.layout();
			}
		}
	}

	private static String sha1Hash(byte[] bytes) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA-1");
			md5.reset();
			md5.update(bytes);
			byte[] result = md5.digest();

			StringBuffer hexString = new StringBuffer();
			for (byte element : result) {
				hexString.append(Integer.toHexString(0xFF & element));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
