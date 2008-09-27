package com.swtxml.swt.processors;

import com.swtxml.parser.ITagProcessor;
import com.swtxml.tag.Tag;
import com.swtxml.util.context.Context;

public class NodeContextTagProcessor implements ITagProcessor {

	private ITagProcessor[] processors;

	public NodeContextTagProcessor(ITagProcessor... processors) {
		this.processors = processors;
	}

	public void process(final Tag tag) {
		Context.runWith(new Runnable() {

			public void run() {
				Context.addAdapter(tag);
				for (ITagProcessor processor : processors) {
					processor.process(tag);
				}
			}
		});
	}

}
