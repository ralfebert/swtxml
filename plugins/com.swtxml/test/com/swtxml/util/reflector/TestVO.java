/**
 * 
 */
package com.swtxml.util.reflector;


public class TestVO extends BaseTestVO {
	private String text;
	private int counter;
	public String publicText;
	protected String baseProtectedText;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	protected int getProtectedProperty() {
		return counter;
	}

	protected void setProtectedProperty(int counter) {
		this.counter = counter;
	}

	protected void setMulti(int counter, int xyz) {
		this.counter = counter;
	}

}