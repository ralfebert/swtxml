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
package com.swtxml.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * SwtResourceManager keeps track of resources which need to be disposed. For
 * sake of simplicity two maps are used instead of JFace ResourceRegistries.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class SwtResourceManager {

	private final Map<String, Image> images = new HashMap<String, Image>();
	private final Map<String, Color> colors = new HashMap<String, Color>();

	/**
	 * Creates a SwtResourceManager for which you need to call dispose manually
	 * to dispose all resources.
	 */
	public SwtResourceManager() {
	}

	/**
	 * Creates a SwtResourceManager which automatically disposes all resources
	 * when owner is disposed.
	 */
	public SwtResourceManager(Composite owner) {
		owner.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}

		});
	}

	public void dispose() {
		for (Image image : images.values()) {
			image.dispose();
		}
		for (Color color : colors.values()) {
			color.dispose();
		}
		colors.clear();
		images.clear();
	}

	public Map<String, Image> getImages() {
		return images;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

}