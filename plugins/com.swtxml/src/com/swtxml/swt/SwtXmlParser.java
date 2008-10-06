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

import org.eclipse.swt.widgets.Composite;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.INamespaceResolver;
import com.swtxml.events.visitor.CreateEventListeners;
import com.swtxml.extensions.DefaultNamespaceResolver;
import com.swtxml.extensions.ExtensionsNamespaceResolver;
import com.swtxml.i18n.EclipsePluginLabelTranslator;
import com.swtxml.i18n.GracefulBundleLabelTranslator;
import com.swtxml.i18n.ILabelTranslator;
import com.swtxml.i18n.ResourceBundleLabelTranslator;
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

	public SwtXmlParser(Composite rootComposite, Object view) {
		super(getSwtNamespaceResolver());
		this.rootComposite = rootComposite;
		this.view = view;
	}

	private static INamespaceResolver getSwtNamespaceResolver() {
		if (EclipseEnvironment.isAvailable()) {
			return new ExtensionsNamespaceResolver();
		} else {
			return new DefaultNamespaceResolver();
		}
	}

	@SuppressWarnings("unchecked")
	public <A> A adaptTo(Class<A> adapterClass) {
		if (ILabelTranslator.class.isAssignableFrom(adapterClass)) {
			if (view != null) {
				if (EclipseEnvironment.isAvailable()) {
					return (A) new GracefulBundleLabelTranslator(new EclipsePluginLabelTranslator(
							view.getClass()));
				} else {
					return (A) new GracefulBundleLabelTranslator(new ResourceBundleLabelTranslator(
							view.getClass()));
				}
			}
			return (A) new GracefulBundleLabelTranslator();
		}
		return null;
	}

	public Tag parse() {
		return super.parse(view.getClass(), "swtxml");
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
			root.visitDepthFirst(new CreateEventListeners(view));
			new ByIdInjector().inject(view, ids);
		}
	}

}