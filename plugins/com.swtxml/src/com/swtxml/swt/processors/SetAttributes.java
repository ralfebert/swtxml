package com.swtxml.swt.processors;

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
			Control control = null;
			for (Tag children : tag.getChildren()) {
				Control childNodeControl = children.adaptTo(Control.class);
				if (childNodeControl != null) {
					if (control != null) {
						throw new ParseException("TabItems may have only one control inside!");
					} else {
						control = childNodeControl;
					}
				}
			}
			if (control != null) {
				((TabItem) widget).setControl(control);
			}

		}

		for (String name : tag.getAttributes().keySet()) {
			SwtInfo.WIDGET_PROPERTIES.getProperties(widget.getClass()).getInjector(widget)
					.setPropertyValue(name, tag.getAttribute(name));
		}
	}

}
