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
package com.swtxml.swt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.swtxml.magic.MagicTagLibrary;
import com.swtxml.parser.IControllerObjectProvider;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagNode;

public class EventTagLibrary extends MagicTagLibrary {

	@Override
	public void foreignAttribute(final TagNode node, TagAttribute attr) {
		Button btn = node.get(Button.class);
		if (btn == null) {
			throw new TagLibraryException(node, "onClick event handler only works for Button");
		}

		if (!(attr.getParser() instanceof IControllerObjectProvider)) {
			throw new TagLibraryException(node,
					"event tags only work when the parser provides a controller!");
		}

		final Object controller = ((IControllerObjectProvider) attr.getParser()).getController();
		boolean found = false;
		// TODO: consider superclass methods
		for (final Method method : controller.getClass().getDeclaredMethods()) {
			final Class<?>[] parameterTypes = method.getParameterTypes();
			if (method.getName().equals(attr.getValue().trim())
					&& (parameterTypes.length == 0 || Arrays.equals(parameterTypes,
							new Class[] { SelectionEvent.class }))) {
				if (found) {
					throw new TagLibraryException(node, "Ambiguous event method: " + method);
				} else {
					found = true;
					attr.setProcessed();
					btn.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent selectionEvent) {
							try {
								method.setAccessible(true);
								method.invoke(controller,
										parameterTypes.length == 0 ? new Object[] {}
												: new Object[] { selectionEvent });
							} catch (InvocationTargetException e) {
								Throwable targetException = e.getTargetException();
								if (targetException instanceof RuntimeException) {
									throw (RuntimeException) targetException;
								}
								throw new TagLibraryException(node, targetException);
							} catch (RuntimeException e) {
								throw e;
							} catch (Exception e) {
								throw new TagLibraryException(node, e);
							}
						}

					});
				}
			}
		}
		if (!found) {
			throw new TagLibraryException(node, "No listener method " + attr.getValue()
					+ " found in " + controller);
		}

	}

}
