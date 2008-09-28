package com.swtxml.util.lang;

public class ContractProof {

	public static void notNull(Object obj, String name) {
		if (obj == null) {
			throw new IllegalArgumentException(name + " may not be null");
		}
	}

}
