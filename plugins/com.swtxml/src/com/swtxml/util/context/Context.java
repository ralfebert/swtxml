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
package com.swtxml.util.context;

import java.util.ArrayList;
import java.util.List;

import com.swtxml.adapter.IAdaptable;

/**
 * Context is a threadlocal list of adapter objects. This is used while parsing
 * or processing nodes if context information is required but the object needing
 * the information is not responsible for obtaining it. Then some outer object
 * needs to establish the Context by calling Context.addAdapter().
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class Context {

	private static final ThreadLocal<Context> context = new ThreadLocal<Context>();

	private final List<IAdaptable> adapters;

	Context() {
		this.adapters = new ArrayList<IAdaptable>();
	}

	Context(Context ctx) {
		this.adapters = new ArrayList<IAdaptable>(ctx.adapters);
	}

	public static void dump() {
		System.out.println(context.get());
	}

	/**
	 * Checks if an adapter to clazz is available in this context. Returns this
	 * adapter or null if none available.
	 */
	@SuppressWarnings("unchecked")
	public static <A> A adaptTo(Class<A> clazz) {
		Context ctx = context.get();
		if (ctx == null) {
			return null;
		}
		for (IAdaptable adapter : ctx.adapters) {
			Object obj = adapter.getAdapter(clazz);
			if (obj != null) {
				return (A) obj;
			}
		}
		return null;
	}

	/**
	 * Makes the context adaptable using the given adapter object. Calls to
	 * Context.adaptTo will be delegated to all added adapter adaptTo methods.
	 */
	public static void addAdapter(IAdaptable adapter) {
		Context ctx = context.get();
		if (ctx == null) {
			ctx = new Context();
			context.set(ctx);
		}
		ctx.adapters.add(adapter);
	}

	/**
	 * Runs the given runnable with an inner Context. You will see everything
	 * which was in the outer (original) Context, but changes will be only
	 * visible in the inner context.
	 */
	public static void runWith(Runnable runnable) {
		Context oldContext = Context.context.get();
		if (oldContext != null) {
			Context.context.set(new Context(oldContext));
		}
		try {
			runnable.run();
		} finally {
			Context.context.set(oldContext);
		}
	}

	public static void clear() {
		context.set(null);
	}

	@Override
	public String toString() {
		return "Context[" + adapters + "]";
	}

}
