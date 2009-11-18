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

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.core.runtime.Assert;

public class ContractProof {

	/**
	 * Puts key => value in map. If the key is already in the list with another
	 * value, a ContractException is thrown.
	 */
	public static <K, V> void safePut(Map<K, V> map, K key, V value) {
		V previousValue = map.put(key, value);
		if (previousValue != null && !ObjectUtils.equals(previousValue, value)) {
			throw new ContractException("May not overwrite " + key + " => " + previousValue
					+ " with " + value);
		}
	}
}
