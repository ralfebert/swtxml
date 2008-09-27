package com.swtxml.util.context;

import java.util.ArrayList;
import java.util.List;

import com.swtxml.util.adapter.IAdaptable;

public class Context {

	private static final ThreadLocal<Context> context = new ThreadLocal<Context>();

	private List<IAdaptable> adapters = new ArrayList<IAdaptable>();

	private static Context get() {
		return context.get();
	}

	private static Context getOrCreate() {
		Context ctx = get();
		if (ctx == null) {
			ctx = new Context();
			context.set(ctx);
		}
		return ctx;
	}

	@SuppressWarnings("unchecked")
	public static <A> A adaptTo(Class<A> clazz) {
		Context ctx = get();
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

	public static void clear() {
		context.set(null);
	}
}
