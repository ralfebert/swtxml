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
package com.swtxml.magic;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;
import com.swtxml.tag.TagNodeEmpty;

public abstract class MagicTagLibrary implements ITagLibrary {

	public static final Object NOTBOUND = "---object could not be bound---";

	public class ParameterBinding {

		private Method method;
		private Class[] types;
		private Object[] boundParameters;
		private Annotation[][] annotations;
		private TagInformation tagInfo;

		private boolean possible = true;

		public ParameterBinding(Method method, TagInformation tagInfo) {
			this.method = method;
			this.tagInfo = tagInfo;
			this.types = method.getParameterTypes();
			this.boundParameters = new Object[types.length];
			this.annotations = method.getParameterAnnotations();

			for (int i = 0; i < boundParameters.length; i++) {
				Object bindForParam = bindParameter(types[i], annotations[i]);
				if (bindForParam == NOTBOUND) {
					possible = false;
					return;
				}
				boundParameters[i] = bindForParam;
			}
		}

		private Object bindParameter(Class type, Annotation[] annotations) {
			if (type.equals(TagInformation.class)) {
				return tagInfo;
			}

			if (annotations.length == 0) {
				throw new TagLibraryException(tagInfo, "Unannotated method parameter in " + method);
			}

			for (Annotation annotation : annotations) {
				if (annotation instanceof Parent) {
					Object parentObject = null;
					if (((Parent) annotation).recursive()) {
						parentObject = tagInfo.findParentRecursive(type);
					} else {
						parentObject = tagInfo.findParent(type);
					}
					return (parentObject == null) ? NOTBOUND : parentObject;
				}
				throw new TagLibraryException(tagInfo, "Invalid annotation " + annotation
						+ " in tag method " + method);
			}

			return NOTBOUND;
		}

		public boolean isPossible() {
			return possible;
		}

		public TagNode execute() {
			try {
				Object result = method.invoke(MagicTagLibrary.this, boundParameters);
				TagNode tagResult;
				if (result == null) {
					tagResult = new TagNodeEmpty(tagInfo);
				} else if (result instanceof TagNode) {
					tagResult = (TagNode) result;
				} else {
					// TODO: is this still used?
					tagResult = new MagicTagNodeObjectProxy(null, tagInfo, result);
				}
				return tagResult;
			} catch (InvocationTargetException e) {
				throw new TagLibraryException(tagInfo, e.getTargetException());
			} catch (Exception e) {
				throw new TagLibraryException(tagInfo, e);
			}

		}

	}

	public class TagDescription {

		private final String tagName;
		private final List<Method> methods = new ArrayList<Method>();

		public TagDescription(String tagName) {
			this.tagName = tagName;
		}

		public void addMethod(Method method) {
			methods.add(method);
		}

		public TagNode tag(TagInformation tagInfo) {
			// TODO: current strategy: take the first matching method is not
			// magic enough (best matching)
			TagNode result = null;
			for (Method method : methods) {
				ParameterBinding binding = new ParameterBinding(method, tagInfo);
				if (binding.isPossible()) {
					if (result == null) {
						result = binding.execute();
					} else {
						throw new TagLibraryException(tagInfo,
								"more than one tag method matching for " + tagInfo);
					}
				}
			}

			if (result == null) {
				throw new TagLibraryException(tagInfo,
						"Tag not allowed here because no suitable tag method found for " + tagName
								+ "!");
			}

			return result;
		}

	}

	protected Map<String, TagDescription> tagDescriptionByName;

	public TagNode tag(TagInformation tagInfo) {
		checkTagDescriptions();

		TagDescription tagDesc = tagDescriptionByName.get(tagInfo.getTagName());
		if (tagDesc == null) {
			throw new TagLibraryException(tagInfo, "Unknown tag: " + tagInfo.getTagName() + " in "
					+ this.getClass());
		}

		return tagDesc.tag(tagInfo);
	}

	protected void checkTagDescriptions() {
		if (tagDescriptionByName != null) {
			return;
		}

		tagDescriptionByName = new HashMap<String, TagDescription>();
		Method[] methods = this.getClass().getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Tag.class)) {
				String tagName = method.getName();
				TagDescription tagDesc = tagDescriptionByName.get(tagName);
				if (tagDesc == null) {
					tagDesc = new TagDescription(tagName);
					tagDescriptionByName.put(tagName, tagDesc);
				}
				tagDesc.addMethod(method);
			}
		}
	}

	public void foreignAttribute(TagNode node, TagAttribute attr) {
		// TODO: magic processing of foreign attributes
	}

}
