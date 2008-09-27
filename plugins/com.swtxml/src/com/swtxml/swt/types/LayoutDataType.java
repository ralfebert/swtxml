package com.swtxml.swt.types;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.context.Context;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class LayoutDataType implements IType<Object>, IContentAssistable {

	private PropertyRegistry layoutProperties;

	public LayoutDataType(PropertyRegistry layoutProperties) {
		this.layoutProperties = layoutProperties;
	}

	public Object convert(String value) {
		Layout layout = Context.adaptTo(Layout.class);
		if (layout == null) {
			throw new ParseException("LayoutData can only be used in a Layout context");
		}
		return createLayoutData(layout, value);
	}

	public Object createLayoutData(Layout parentLayout, String value) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value);

		Class<?> layoutDataClass = getLayoutClass(parentLayout);
		if (layoutDataClass == null) {
			throw new ParseException("Layout " + layoutDataClass.getSimpleName()
					+ " doesn't allow layout data!");
		}

		Object layoutData;
		try {
			layoutData = layoutDataClass.newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
		// TODO: straighten out this "LayoutData" needs to see its parent thing
		// and is a setter for that reason
		layoutProperties.getProperties(layoutData.getClass()).getInjector(layoutData)
				.setPropertyValues(layoutConstraints);
		return layoutData;
	}

	private Class<?> getLayoutClass(Layout layout) {
		if (layout == null) {
			return null;
		}
		if (layout instanceof RowLayout) {
			return RowData.class;
		}
		if (layout instanceof GridLayout) {
			return GridData.class;
		}
		if (layout instanceof FormLayout) {
			return FormData.class;
		}
		return null;
	}

	public List<Match> getProposals(Match match) {
		Layout layout = Context.adaptTo(Layout.class);
		// if (layout==null)
		// return
		return Collections.emptyList();
	}

}
