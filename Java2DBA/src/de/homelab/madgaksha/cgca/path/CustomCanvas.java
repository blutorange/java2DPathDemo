package de.homelab.madgaksha.cgca.path;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import de.homelab.madgaksha.cgca.path.IPathPoint.PathPoint;

class CustomCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	private Image dbImage;
	private Graphics dbg;
	private final List<IPathCommand> pathCommandList = new ArrayList<>();
	private boolean closePath;
	private boolean fillPath;
	private IPathBuilder pathBuilder;
	private EWindingRule windingRule = EWindingRule.NON_ZERO;
	private Paint paint;

	public CustomCanvas() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent ev) {
				initBuffer();
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(final MouseEvent ev) {
				repaint();
			}

			@Override
			public void mouseDragged(final MouseEvent ev) {
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(final MouseEvent ev) {
			}

			@Override
			public void mousePressed(final MouseEvent ev) {
				if (pathBuilder != null) {
					pathBuilder.mouseClick(ev.getPoint());
					checkBuilderState();
				}
			}

			@Override
			public void mouseExited(final MouseEvent ev) {
			}

			@Override
			public void mouseEntered(final MouseEvent ev) {
			}

			@Override
			public void mouseClicked(final MouseEvent ev) {
			}
		});
	}

	protected void checkBuilderState() {
		if (pathBuilder != null && pathBuilder.isFinished()) {
			pathCommandList.add(pathBuilder.build());
			final IPathBuilder newPathBuilder = pathBuilder.getNew();
			pathBuilder = null;
			this.beginPath(newPathBuilder);
		}
	}

	@Override
	public void update(final Graphics g) {
		if (dbImage == null)
			initBuffer();

		// clear screen in background
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// draw elements in background
		dbg.setColor(getForeground());
		paint(dbg);

		// draw image on the screen
		g.drawImage(dbImage, 0, 0, this);
	}

	private void initBuffer() {
		if (getSize().width > 0 && getSize().height > 0) {
			dbImage = createImage(getSize().width, getSize().height);
			dbg = dbImage.getGraphics();
		}
	}

	@Override
	public void paint(final Graphics g) {
		if (!(g instanceof Graphics2D))
			throw new RuntimeException("needs a Graphics2D object: " + g.getClass().getCanonicalName()); //$NON-NLS-1$
		final Graphics2D g2d = (Graphics2D) g;
		drawPath(g2d);
		drawPoints(g2d);
	}

	private void drawPoints(final Graphics2D g2d) {
		final Point p = getMousePosition();
		if (pathBuilder != null) {
			if (p != null)
				drawPathPoint(g2d, new PathPoint(p, pathBuilder.getName()));
			for (final IPathPoint pp : pathBuilder.getPathPointSet())
				drawPathPoint(g2d, pp);
		}
		for (final IPathCommand pc : pathCommandList)
			for (final IPathPoint pp : pc.getPathPointSet())
				drawPathPoint(g2d, pp);
	}

	private void drawPath(final Graphics2D g2d) {
		g2d.setColor(Color.red);
		g2d.setPaint(paint);
		final GeneralPath path = new GeneralPath();
		path.setWindingRule(windingRule.getInt());
		path.moveTo(0f, 0f);
		for (final IPathCommand pc : pathCommandList)
			pc.apply(path);
		if (closePath)
			path.closePath();
		if (fillPath)
			g2d.fill(path);
		else
			g2d.draw(path);
	}

	private static void drawPathPoint(final Graphics2D g2d, final IPathPoint pp) {
		g2d.setColor(Color.BLACK);
		g2d.fill(new Ellipse2D.Float(pp.getPointX() - 5f, pp.getPointY()-5f, 10f,10f));
		g2d.drawString(String.format("%s(%.01f,%.01f)", pp.getLabel(), pp.getPointX(), pp.getPointY()), pp.getPointX(), //$NON-NLS-1$
				pp.getPointY());
	}

	public void setClosePath(final boolean closePath) {
		this.closePath = closePath;
		repaint();
	}
	public void setFillPath(final boolean fillPath) {
		this.fillPath = fillPath;
		repaint();
	}

	public void clearPathList() {
		pathCommandList.clear();
		pathBuilder = null;
		repaint();
	}

	public void beginPath(final IPathBuilder pathBuilder) {
		if (pathBuilder != null)
			this.pathBuilder = pathBuilder;
	}

	public void setWindingRule(final EWindingRule windingRule) {
		if (windingRule != null)
			this.windingRule = windingRule;
	}

	public Object getWindingRule() {
		return windingRule;
	}

	public void setFillPaint(final TexturePaint paint) {
		this.paint = paint;
	}
}