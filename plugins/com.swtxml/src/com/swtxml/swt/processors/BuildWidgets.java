package com.swtxml.swt.processors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.parser.ITagProcessor;
import com.swtxml.swt.SwtHandling;
import com.swtxml.swt.metadata.WidgetBuilder;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.tag.TagInformation;
import com.swtxml.util.parser.ParseException;

public class BuildWidgets implements ITagProcessor {

	private Composite parent;

	public BuildWidgets(Composite parent) {
		this.parent = parent;
	}

	public void process(TagInformation tag) {
		if (!(tag.getTagDefinition() instanceof WidgetTag)) {
			return;
		}

		if (tag.isRoot()) {
			if (!tag.getTagName().equals(Composite.class.getSimpleName())) {
				throw new ParseException("Invalid root tag " + tag.getTagName() + ", expected <"
						+ Composite.class.getSimpleName() + ">");
			}
			tag.makeAdaptable(parent);
			return;
		}

		WidgetTag widgetTag = (WidgetTag) tag.getTagDefinition();
		WidgetBuilder builder = new WidgetBuilder(widgetTag);

		Integer style = SwtHandling.SWT.getIntValue(tag.slurpAttribute("style"));
		Composite parent = (Composite) tag.parentRecursiveAdaptTo(builder.getParentClass());
		Widget widget = builder.build(parent, style == null ? SWT.NONE : style);
		tag.makeAdaptable(widget);
	}

}
