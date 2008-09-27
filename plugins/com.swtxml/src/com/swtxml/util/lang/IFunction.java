package com.swtxml.util.lang;

public interface IFunction<FROM, TO> {

	public TO apply(FROM obj);

}
