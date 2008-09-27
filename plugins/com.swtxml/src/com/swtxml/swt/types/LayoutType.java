package com.swtxml.swt.types;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.parser.Splitter;
import com.swtxml.util.parser.Strictness;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.IInjector;
import com.swtxml.util.properties.Property;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class LayoutType implements IType<Layout>, IContentAssistable {

	private static final String LAYOUT_KEY = "layout";
	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();

	private PropertyRegistry layoutProperties;

	public LayoutType(PropertyRegistry layoutProperties) {
		this.layoutProperties = layoutProperties;
	}

	public Layout convert(String value) {
		return convert(value, Strictness.STRICT);
	}

	public Layout convert(String value, Strictness strictness) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value, strictness);

		String layoutName = layoutConstraints.remove(LAYOUT_KEY);
		if (layoutName == null) {
			throw new ParseException("no layout specified");
		}

		Layout layout = getLayoutClass(layoutName);

		IInjector injector = layoutProperties.getProperties(layout.getClass()).getInjector(layout);
		injector.setPropertyValues(layoutConstraints);

		return layout;
	}

	private Layout getLayoutClass(String layoutName) {
		String className = SWT_LAYOUT_PACKAGE + "." + StringUtils.capitalize(layoutName) + "Layout";
		try {
			return (Layout) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	public List<Match> getProposals(Match match) {

		String text = match.getText();
		int cursor = match.getCursorPos();

		if (cursor >= text.length() || match.getText().charAt(match.getCursorPos()) != ';') {
			match = match.insert(";", cursor);
			match.moveCursor(-1);
		}

		Map<String, String> layoutConstraints = KeyValueParser.parse(match.getText(),
				Strictness.LAX);
		String layoutName = layoutConstraints.get("layout");
		match = match.restrict(KeyValueParser.VALUE_SPLITTER);

		text = match.getText();
		cursor = match.getCursorPos();

		if (layoutName == null) {
			return match.propose(LAYOUT_KEY + ":");
		} else {
			ClassProperties<? extends Layout> properties = layoutProperties
					.getProperties(getLayoutClass(layoutName).getClass());

			Splitter colon = KeyValueParser.KEY_VALUE_SPLITTER;
			int colonPos = text.indexOf(colon.getPreferredSeparator());

			if (colonPos < 0 || colonPos >= cursor) {
				Set<String> propertyNames = new HashSet<String>(properties.getProperties().keySet());
				propertyNames.removeAll(layoutConstraints.keySet());
				Collection<String> propertyProposals = Collections2.forIterable(Iterables
						.transform(propertyNames, new Function<String, String>() {
							public String apply(String s) {
								return s + ":";
							}
						}));
				return match.propose(propertyProposals);
			} else {
				String[] keyValue = colon.split(text);
				Property property = properties.getProperties().get(keyValue[0]);
				if (property != null) {
					IType<?> type = property.getType();
					if (type instanceof IContentAssistable) {
						List<Match> proposals = ((IContentAssistable) type).getProposals(match
								.restrict(colon));
						for (Match proposal : proposals) {
							// move cursor behind semicolon
							proposal.moveCursor(1);
						}
						return proposals;
					}
				}
			}
		}
		return Collections.emptyList();
	}
}
