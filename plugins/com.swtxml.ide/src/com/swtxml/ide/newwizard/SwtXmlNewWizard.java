package com.swtxml.ide.newwizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

@SuppressWarnings("restriction")
public class SwtXmlNewWizard extends NewElementWizard {

	private SwtXmlNewPage page;
	private IFile swtxmlFile;

	public SwtXmlNewWizard() {
		setWindowTitle("New SWT/XML Class");
	}

	@Override
	public void addPages() {
		super.addPages();
		page = new SwtXmlNewPage();
		page.init(getSelection());
		addPage(page);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
	}

	@Override
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		page.createType(monitor);

		IPackageFragment pkg = page.getPackageFragment();
		IContainer folder = (IContainer) pkg.getResource();

		IJavaElement element = getCreatedElement();
		swtxmlFile = folder.getFile(new Path(element.getElementName() + ".swtxml"));
		swtxmlFile.create(getClass().getResourceAsStream("template.swtxml"), IResource.NONE,
				monitor);
		folder.refreshLocal(1, monitor);
	}

	@Override
	public boolean performFinish() {
		boolean accepted = super.performFinish();
		if (accepted && swtxmlFile != null) {
			openResource(swtxmlFile);
			selectAndReveal(swtxmlFile);
		}
		return accepted;
	}

	@Override
	public IJavaElement getCreatedElement() {
		return page.getCreatedType();
	}

}
