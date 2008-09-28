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

import com.swtxml.contracts.IAdaptable;

public class Context {

	private static final ThreadLocal<Context> context = new ThreadLocal<Context>();

	private final List<IAdaptable> adapters;

	public Context() {
		this.adapters = new ArrayList<IAdaptable>();
	}

	public Context(Context ctx) {
		this.adapters = new ArrayList<IAdaptable>(ctx.adapters);
	}

	public static void dump() {
		System.out.println(context.get());
	}

	private static Context getOrCreate() {
		Context ctx = context.get();
		if (ctx == null) {
			ctx = new Context();
			context.set(ctx);
		}
		return ctx;
	}

	@SuppressWarnings("unchecked")
	public static <A> A adaptTo(Class<A> clazz) {
		Context ctx = context.get();
		if (ctx == null) {
			return null;
		}
		for (IAdaptable adapter : ctx.adapters) {
			Object obj = adapter.adaptTo(clazz);
			if (obj != null) {
				return (A) obj;
			}
		}
		return null;
	}

	public static void addAdapter(IAdaptable adapter) {
		Context ctx = getOrCreate();
		ctx.adapters.add(adapter);
	}

	public static void removeAdapter(IAdaptable adapter) {
		Context ctx = context.get();
		if (!ctx.adapters.remove(adapter)) {
			throw new ContextException("Could not remove: " + adapter
					+ " from Context adapter list!");
		}
	}

	public static void clear() {
		context.set(null);
	}

	@Override
	public String toString() {
		return "Context[" + adapters + "]";
	}

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

}
