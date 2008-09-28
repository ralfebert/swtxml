package com.swtxml.swt.byid;

public class ByIdView extends ByIdBaseView {

	@ById
	private String test;

	@SuppressWarnings("unused")
	private String otherField;

	public String getTestX() {
		return test;
	}

}
