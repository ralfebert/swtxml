package com.swtxml.ide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;

import org.eclipse.jface.text.IDocument;
import org.xml.sax.InputSource;

import com.swtxml.resources.IDocumentResource;
import com.swtxml.util.parser.ParseException;

public class PreviewResource implements IDocumentResource {

	private File file;
	private IDocument document;
	private final File rootOfProject;

	public PreviewResource(File rootOfProject, File file, IDocument document) {
		this.rootOfProject = rootOfProject;
		this.file = file;
		this.document = document;
	}

	public String getDocumentName() {
		return file == null ? "unknown" : file.getName();
	}

	public InputSource getInputSource() {
		return new InputSource(new StringReader(document.get()));
	}

	public InputStream resolve(String path) {
		if (file == null) {
			throw new ParseException("No file was available for locating resources!");
		}

		// try Current folder
		File f = new File(file.getParent(), path);
		if (!f.exists()) {
			// try root of project folder
			f = new File(rootOfProject, path);
		}
		if (f.exists()) {
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		return null;
	}

}
