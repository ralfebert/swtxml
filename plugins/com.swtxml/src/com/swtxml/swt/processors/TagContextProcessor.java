package com.swtxml.swt.processors;

import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.context.Context;

public class TagContextProcessor implements ITagProcessor {

	private ITagProcessor processor;

	public TagContextProcessor(ITagProcessor processor) {
		super();
		this.processor = processor;
	}

	public void process(final Tag tag) {
		Context.runWith(new Runnable() {
			public void run() {
				Context.addAdapter(tag);
				processor.process(tag);
			}
		});
	}

}
