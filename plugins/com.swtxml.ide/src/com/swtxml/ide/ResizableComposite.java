package com.swtxml.ide;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;

public class ResizableComposite extends Composite {

	private static final int HORIZONTAL_SASH_HEIGHT = 30;
	private static final int VERTICAL_SASH_WIDTH = 60;

	private Composite innerComposite;
	private GridData innerCompositeLayoutData;

	public ResizableComposite(Composite parent) {
		super(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).spacing(0, 0).applyTo(this);
		this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		innerComposite = new Composite(this, SWT.NONE);
		innerCompositeLayoutData = new GridData(100, 100);
		innerComposite.setLayoutData(innerCompositeLayoutData);

		Sash verticalSash = new Sash(this, SWT.SMOOTH | SWT.VERTICAL);
		GridDataFactory.fillDefaults().hint(VERTICAL_SASH_WIDTH, 0).applyTo(verticalSash);
		verticalSash.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		verticalSash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				innerCompositeLayoutData.widthHint = e.x;
				checkLayout();
			}
		});
		verticalSash.addPaintListener(getRightVerticalRulerPaintListener());
		verticalSash.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		verticalSash.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

		Sash horizontalSash = new Sash(this, SWT.SMOOTH);
		GridDataFactory.fillDefaults().span(2, 1).hint(0, HORIZONTAL_SASH_HEIGHT).applyTo(
				horizontalSash);
		horizontalSash.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		horizontalSash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				innerCompositeLayoutData.heightHint = e.y;
				ResizableComposite.this.layout();
			}
		});
		horizontalSash.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		horizontalSash.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		horizontalSash.addPaintListener(getBottomHorizontalRulerPaintListener());

		this.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				checkLayout();
			}

		});
	}

	protected void checkLayout() {
		innerCompositeLayoutData.widthHint = Math.min(innerCompositeLayoutData.widthHint, this
				.getBounds().width
				- VERTICAL_SASH_WIDTH);
		innerCompositeLayoutData.heightHint = Math.min(innerCompositeLayoutData.heightHint, this
				.getBounds().height
				- HORIZONTAL_SASH_HEIGHT);
		layout();
	}

	protected void resetSize() {
		this.getParent().layout();
		Point size = innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		innerCompositeLayoutData.widthHint = size.x;
		innerCompositeLayoutData.heightHint = size.y;
		checkLayout();
	}

	private PaintListener getBottomHorizontalRulerPaintListener() {
		return new PaintListener() {
			public void paintControl(PaintEvent e) {
				int w = innerComposite.getBounds().width;
				if (w == 0) {
					return;
				}
				int xStart = 3;
				int yStart = 5;
				int oldAntialias = e.gc.getAntialias();
				e.gc.setAntialias(SWT.ON);

				// Line
				e.gc.drawLine(xStart, yStart, xStart + w - 2, yStart);

				// Arrow left
				e.gc.drawLine(xStart, 0, xStart, 10);
				e.gc.drawLine(xStart, yStart, xStart + 8, yStart + 4);
				e.gc.drawLine(xStart, yStart, xStart + 8, yStart - 4);

				// // Arrow right
				e.gc.drawLine(xStart + w - 2, 0, xStart + w - 2, 10);
				e.gc.drawLine(xStart + w - 2, yStart, xStart + w - 10, yStart + 4);
				e.gc.drawLine(xStart + w - 2, yStart, xStart + w - 10, yStart - 4);

				// // text: 123 px
				e.gc.drawText(xStart + w + " px", (xStart + w / 2) - 13, 7);
				e.gc.setAntialias(oldAntialias);
			}
		};
	}

	private PaintListener getRightVerticalRulerPaintListener() {
		return new PaintListener() {
			public void paintControl(PaintEvent e) {
				int h = innerComposite.getBounds().height;
				if (h == 0) {
					return;
				}
				int xStart = 5;
				int yStart = 2;
				int oldAntialias = e.gc.getAntialias();
				e.gc.setAntialias(SWT.ON);

				// Line
				e.gc.drawLine(xStart, yStart, xStart, yStart + h - 1);

				// Arrow up
				e.gc.drawLine(0, yStart, 10, yStart);
				e.gc.drawLine(xStart, yStart, xStart + 4, yStart + 8);
				e.gc.drawLine(xStart, yStart, xStart - 4, yStart + 8);

				// Arrow down
				e.gc.drawLine(0, yStart + h - 1, 10, yStart + h - 1);
				e.gc.drawLine(xStart, yStart + h - 1, xStart + 4, yStart + h - 9);
				e.gc.drawLine(xStart, yStart + h - 1, xStart - 4, yStart + h - 9);

				// text: 123 px
				e.gc.drawText(h + " px", 10, yStart + (h / 2) - 8);
				e.gc.setAntialias(oldAntialias);
			}
		};
	}

	public Composite getResizeableComposite() {
		return innerComposite;
	}

}