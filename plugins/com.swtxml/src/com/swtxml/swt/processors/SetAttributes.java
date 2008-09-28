package com.swtxml.swt.processors;

import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtInfo;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.parser.ParseException;

public class SetAttributes implements ITagProcessor {

	public void process(Tag tag) {
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

		if (widget instanceof TabItem) {
			List<Control> controlChildren = tag.adaptChildren(Control.class);
			if (controlChildren.size() > 1) {
				throw new ParseException("TabItems may have only one control inside!");
			}
			if (controlChildren.size() == 1) {
				((TabItem) widget).setControl(controlChildren.get(0));
			}
		}

		for (String name : tag.getAttributes().keySet()) {
			SwtInfo.WIDGET_PROPERTIES.getProperties(widget.getClass()).getInjector(widget)
					.setPropertyValue(name, tag.getAttribute(name));
		}
	}

}
