package com.swtxml.swt.types;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.swt.SwtHandling;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.parser.Strictness;
import com.swtxml.util.properties.IInjector;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class LayoutType implements IType<Layout>, IContentAssistable {

	private static final String LAYOUT_KEY = "layout";
	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();

	public Layout convert(String value) {
		return convert(value, Strictness.STRICT);
	}

	public Layout convert(String value, Strictness strictness) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value, strictness);

		String layoutName = layoutConstraints.remove(LAYOUT_KEY);
		if (layoutName == null) {
			if (strictness == Strictness.STRICT) {
				throw new ParseException("no layout specified");
			} else {
				return null;
			}
		}

		Layout layout = createLayout(layoutName, Strictness.STRICT);

		IInjector injector = SwtHandling.LAYOUT_PROPERTIES.getProperties(layout.getClass())
				.getInjector(layout);
		injector.setPropertyValues(layoutConstraints);

		return layout;
	}

	private Layout createLayout(String layoutName, Strictness strictness) {
		try {
			return getLayoutClass(layoutName, strictness).newInstance();
		} catch (Exception e) {
			if (strictness == Strictness.STRICT) {
				throw new ReflectorException(e);
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Layout> getLayoutClass(String layoutName, Strictness strictness) {
		String className = SWT_LAYOUT_PACKAGE + "." + StringUtils.capitalize(layoutName) + "Layout";
		try {
			return (Class<? extends Layout>) Class.forName(className);
		} catch (Exception e) {
			if (strictness == Strictness.STRICT) {
				throw new ReflectorException(e);
			}
			return null;
		}
	}

	public List<Match> getProposals(Match match) {
		PropertiesContentAssist assist = new PropertiesContentAssist(match);
		String layoutName = assist.getPropertyValues().get("layout");

		Class<? extends Layout> layoutClass = getLayoutClass(layoutName, Strictness.NICE);

		if (layoutClass == null) {
			return assist.getKeyValueMatch().propose(LAYOUT_KEY + ":");
		}

		return assist.getProposals(SwtHandling.LAYOUT_PROPERTIES.getProperties(layoutClass));
	}
}
