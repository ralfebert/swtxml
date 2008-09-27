package com.swtxml.swt.processors;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.parser.ITagProcessor;
import com.swtxml.swt.SwtHandling;
import com.swtxml.tag.TagInformation;

public class SetAttributesProcessor implements ITagProcessor {

	public void process(TagInformation tag) {
		Widget widget = tag.adaptTo(Widget.class);
		if (widget == null) {
			return;
		}

		if (widget instanceof Control) {
			Layout layout = ((Control) widget).getParent().getLayout();
			if (layout != null) {
				// TODO: try not to store an extra reference
				tag.makeAdaptable(layout);
			}
		}

		SwtHandling.WIDGET_PROPERTIES.getProperties(widget.getClass()).getInjector(widget)
				.setPropertyValues(tag.getAttributes());
	}

}
