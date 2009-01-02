package com.swtxml.ide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;

import org.eclipse.jface.text.IDocument;
import org.xml.sax.InputSource;

import com.swtxml.resources.IDocumentResource;

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
		File f = null;

		if (path.startsWith(IDocumentResource.SCHEME_BUNDLE)) {
			f = new File(rootOfProject, path.substring(IDocumentResource.SCHEME_BUNDLE.length()));
		} else if (file != null) {
			f = new File(file.getParent(), path);
		}

		if (f != null && f.exists()) {
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				return null;
			}
		}

		return null;
	}

}
