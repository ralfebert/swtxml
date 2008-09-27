package com.swtxml.util.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class IOUtils {

	public static String toString(InputStream input) {
		try {
			StringWriter writer = new StringWriter();
			InputStreamReader in = new InputStreamReader(input);
			char[] buffer = new char[2048];
			int i = 0;
			while ((i = in.read(buffer)) > 0) {
				writer.write(buffer, 0, i);
			}
			input.close();
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeIOException(e.getMessage(), e);
		}
	}

}
