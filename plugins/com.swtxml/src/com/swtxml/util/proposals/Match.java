package com.swtxml.util.proposals;

public class Match {

	private int cursorPos;
	private String text;
	private int startOffset;
	private int endOffset;

	public Match(String text, int cursorPos) {
		this.text = text;
		this.cursorPos = cursorPos;
	}

	public Match(String str) {
		this.cursorPos = str.indexOf('§');
		this.text = str.substring(0, cursorPos) + str.substring(cursorPos + 1);
	}

	public Match(Match m) {
		this.cursorPos = m.cursorPos;
		this.text = m.text;
		this.startOffset = m.startOffset;
		this.endOffset = m.endOffset;
	}

	public int getCursorPos() {
		return cursorPos;
	}

	public void setCursorPos(int cursorPos) {
		this.cursorPos = cursorPos;
	}

	public String getText() {
		return text.substring(startOffset, text.length() + endOffset);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text.substring(0, cursorPos) + "§" + text.substring(cursorPos);
	}

	private void stripQuotesR() {
		if (!text.startsWith("\"")) {
			insertR("\"", 0);
		}
		if (!text.endsWith("\"")) {
			insertR("\"", text.length());
		}

		startOffset = 1;
		endOffset = -1;
	}

	public Match stripQuotes() {
		Match m = new Match(this);
		m.stripQuotesR();
		return m;
	}

	public Match insert(String str, int i) {
		Match m = new Match(this);
		m.insertR(str, i);
		return m;
	}

	public Match replace(String str, int i, int length) {
		Match m = new Match(this);
		m.replaceR(str, i, length);
		return m;
	}

	private void insertR(String str, int i) {
		replaceR(str, i, 0);
	}

	private void replaceR(String str, int i, int length) {
		i += startOffset;
		if (i + length < cursorPos) {
			cursorPos -= (length - str.length());
		}
		text = text.substring(0, i) + str + text.substring(i + length);
	}

	public String getReplacementText() {
		return text;
	}

}