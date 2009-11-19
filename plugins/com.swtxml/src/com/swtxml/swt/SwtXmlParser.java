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

import java.util.Locale;

import org.eclipse.swt.widgets.Composite;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.INamespaceResolver;
import com.swtxml.events.internal.SwtEvents;
import com.swtxml.events.visitor.AddEventListeners;
import com.swtxml.extensions.DefaultNamespaceResolver;
import com.swtxml.extensions.ExtensionsNamespaceResolver;
import com.swtxml.i18n.ILabelTranslator;
import com.swtxml.i18n.ResourceBundleLabelTranslator;
import com.swtxml.resources.ClassResource;
import com.swtxml.resources.IDocumentResource;
import com.swtxml.swt.byid.ByIdInjector;
import com.swtxml.swt.visitor.BuildWidgets;
import com.swtxml.swt.visitor.CollectIds;
import com.swtxml.swt.visitor.SetAttributes;
import com.swtxml.swt.visitor.TagContextVisitor;
import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;
import com.swtxml.tinydom.TinyDomParser;
import com.swtxml.util.context.Context;
import com.swtxml.util.eclipse.EclipseEnvironment;

public class SwtXmlParser extends TinyDomParser implements IAdaptable {

	private Composite rootComposite;
	private Object view;
	private SwtResourceManager resourceManager;
	private ResourceBundleLabelTranslator labelTranslator;

	public SwtXmlParser(Composite rootComposite, IDocumentResource resource, Object view) {
		super(getSwtNamespaceResolver(), resource);
		this.rootComposite = rootComposite;
		this.view = view;
	}

	public SwtXmlParser(Composite rootComposite, Object view) {
		this(rootComposite, ClassResource.coLocated(view.getClass(), "swtxml"), view);
	}

	private static INamespaceResolver getSwtNamespaceResolver() {
		if (EclipseEnvironment.isAvailable()) {
			return new ExtensionsNamespaceResolver();
		} else {
			return new DefaultNamespaceResolver();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> A getAdapter(Class<A> adapterClass) {
		Object result = super.getAdapter(adapterClass);
		if (result != null) {
			return (A) result;
		}

		if (SwtResourceManager.class.isAssignableFrom(adapterClass)) {
			if (this.resourceManager == null) {
				this.resourceManager = new SwtResourceManager(rootComposite);
			}
			return (A) this.resourceManager;
		}

		if (ILabelTranslator.class.isAssignableFrom(adapterClass)) {
			if (this.labelTranslator == null) {
				this.labelTranslator = new ResourceBundleLabelTranslator(document, Locale
						.getDefault());
			}
			return (A) this.labelTranslator;
		}

		return null;
	}

	@Override
	protected void onParseCompleted(final Tag root) {
		final CollectIds ids = new CollectIds();
		final ITagVisitor buildWidgets = new TagContextVisitor(new BuildWidgets(rootComposite));
		final ITagVisitor setAttributes = new TagContextVisitor(new SetAttributes());

		root.visitDepthFirst(ids);

		Context.runWith(new Runnable() {
			public void run() {
				Context.addAdapter(ids);
				Context.addAdapter(SwtXmlParser.this);
				root.visitDepthFirst(buildWidgets);
				root.visitDepthFirst(setAttributes);
			}
		});

		if (view != null) {
			root.visitDepthFirst(new AddEventListeners(view, SwtEvents.getNamespace()));
			new ByIdInjector().inject(view, ids);
		}
	}

}