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
package com.swtxml.util.lang;

import org.apache.commons.lang.StringUtils;

/**
 * Common Collection Filters.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class Filters {

	public static <A> IFilter<A> and(final Iterable<IFilter<A>> filters) {
		return new IFilter<A>() {
			public boolean match(A obj) {
				for (IFilter<A> filter : filters) {
					if (!filter.match(obj)) {
						return false;
					}
				}
				return true;
			}

			@Override
			public String toString() {
				return "(" + StringUtils.join(filters.iterator(), " and ") + ")";
			}
		};
	}
}
