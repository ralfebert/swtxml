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
package com.swtxml.util.reflector;

public class TestVO extends BaseTestVO {
	private String text;
	private int counter;
	public String publicText;
	protected String baseProtectedText;
	public static final int SOME_CONSTANT = 5;

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

	@Override
	protected void doSomething(String test) {

	}

	@Override
	public void doSomethingMore(String test, int x) {

	}

}