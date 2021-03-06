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
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class EditCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	private final static float POINT_RADIUS = 5f;
	private final TimeList list;
	private Image dbImage;
	private Graphics dbg;
	private final List<IPathCommand> pathCommandList = new ArrayList<>();
	private boolean closePath;
	private boolean fillPath;
	private IPathBuilder pathBuilder;
	private EWindingRule windingRule = EWindingRule.NON_ZERO;
	private Paint paint;
	private EMode mode = EMode.PATH_BUILD;
	private IPathPoint selectedPoint;
	private IPathCommand selectedPath;

	public EditCanvas(final TimeList list) {
		super();
		this.list  = list;
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
				switch (ev.getButton()) {
				case (MouseEvent.BUTTON1):
					mouseLeft(ev);
				break;
				case (MouseEvent.BUTTON3):
					mouseRight();
				break;
				}
				repaint();
			}

			private void mouseRight() {
				final IPathPoint p = getClosestNearMouse();
				if (p != null) {
					pathCommandList.remove(selectedPath);
					selectedPath = null;
					selectedPoint = null;
					if (pathCommandList.isEmpty())
						setMode(EMode.PATH_BUILD);
				}
				else {
					setMode(EMode.PATH_BUILD);
					setMode(EMode.POINT_EDIT);
				}
			}

			private void mouseLeft(final MouseEvent ev) {
				switch (mode) {
				case PATH_BUILD:
					if (pathBuilder != null) {
						pathBuilder.mouseClick(ev.getPoint());
						checkBuilderState();
					}
					break;
				case POINT_EDIT:
					if (selectedPoint == null) {
						enterSelectMode(getClosestNearMouse());
					}
					else {
						final Point p = getMousePosition();
						if (p != null) {
							selectedPoint.setPoint((float)p.getX(), (float)p.getY());
							selectedPoint = null;
							selectedPath = null;
						}
					}
					break;
				default:
					break;
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

	protected IPathPoint getClosestNearMouse() {
		final Point p = getMousePosition();
		IPathPoint minP = null;
		float minL = Float.MAX_VALUE;
		if (p != null) {
			for (final IPathCommand pc : this.pathCommandList) {
				for (final IPathPoint pp : pc.getPathPointSet()) {
					final float x = pp.getPointX()-(float)p.getX();
					final float y = pp.getPointY()-(float)p.getY();
					if (x*x+y*y < minL) {
						selectedPath = pc;
						minP = pp;
						minL = x*x+y*y;
					}
				}
			}
		}
		return minL < POINT_RADIUS*POINT_RADIUS*2 ? minP : null;
	}

	protected void checkBuilderState() {
		setMode(EMode.PATH_BUILD);
		if (pathBuilder != null && pathBuilder.isFinished()) {
			pathCommandList.add(pathBuilder.build());
			final IPathBuilder newPathBuilder = pathBuilder.getNew();
			pathBuilder = null;
			this.beginPath(newPathBuilder);
			repaint();
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
		final IPathPoint s = getClosestNearMouse();
		for (final IPathCommand pc : pathCommandList)
			for (final IPathPoint pp : pc.getPathPointSet())
				drawPathPoint(g2d, pp, s==pp);
		if (pathBuilder != null) {
			if (p != null)
				drawPathPoint(g2d, new PathPoint(p, pathBuilder.getName()), false);
			for (final IPathPoint pp : pathBuilder.getPathPointSet())
				drawPathPoint(g2d, pp, s==pp);
		}
		if (selectedPoint != null) {
			if (p != null)
				drawPathPoint(g2d, new PathPoint(p, selectedPoint.getLabel()), true);
		}
	}

	private void drawPath(final Graphics2D g2d) {
		g2d.setColor(Color.red);
		g2d.setPaint(paint);
		final Path2D.Float path = new Path2D.Float();
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

	private void drawPathPoint(final Graphics2D g2d, final IPathPoint pp, final boolean highlight) {
		if (list.getSelectedPoint().map(p -> pp.equalsIPathPointSpatially(p)).orElse(Boolean.FALSE))
			g2d.setColor(highlight ? Color.RED : Color.GREEN);
		else
			g2d.setColor(highlight ? Color.BLUE : Color.BLACK);
		g2d.fill(new Ellipse2D.Float(pp.getPointX() - POINT_RADIUS, pp.getPointY()-POINT_RADIUS, 2f*POINT_RADIUS,2f*POINT_RADIUS));
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
		setMode(EMode.PATH_BUILD);
		pathCommandList.clear();
		pathBuilder = null;
		repaint();
	}

	public void beginPath(final IPathBuilder pathBuilder) {
		if (pathBuilder != null) {
			setMode(EMode.PATH_BUILD);
			this.pathBuilder = pathBuilder;
		}
	}

	public void setWindingRule(final EWindingRule windingRule) {
		if (windingRule != null) {
			this.windingRule = windingRule;
			repaint();
		}
	}

	public Object getWindingRule() {
		return windingRule;
	}

	public void setFillPaint(final TexturePaint paint) {
		this.paint = paint;
	}

	public void setMode(final EMode mode) {
		if (mode == null || mode == this.mode)
			return;
		switch (mode) {
		case PATH_BUILD:
			selectedPoint = null;
			selectedPath = null;
			list.forNothing();
			break;
		case POINT_EDIT:
			pathBuilder = null;
			break;
		default:
			return;
		}
		final boolean repaint = this.mode != mode;
		this.mode = mode;
		if (repaint)
			repaint();
	}

	public void enterSelectMode(final IPathPoint p) {
		setMode(EMode.POINT_EDIT);
		selectedPoint = p;
		if (selectedPoint != null) {
			list.forPathPoint(selectedPoint);
		}
		else {
			list.forNothing();
			selectedPoint = null;
			selectedPath = null;
		}
	}

	public void addKeyFrame() {
		list.getSelectedPoint().ifPresent(p -> p.addKeyFrame());
	}

	public void setKeyFrame(final float f) {
		list.getSelectedPoint().ifPresent(p -> p.setTime(f));
	}

	public void removeKeyFrame() {
		if (list.modelSize() <= 1 || list.getSelectedIndex() < 0)
			return;
		list.getSelectedPoint().ifPresent(p -> {
			p.removeKeyFrame();
			list.clearSelection();
		});
	}

	public AnimCanvas getAnimationCanvas() {
		return new AnimCanvas(pathCommandList, closePath, fillPath, windingRule, paint);
	}

	public float getMaximumTime() {
		float maxTime = 0f;
		for (final IPathCommand pc : pathCommandList) {
			for (final IPathPoint pp : pc.getPathPointSet()) {
				for (final Iterator<IKeyFramedPoint> it = pp.getElementIterator(); it.hasNext();) {
					final IKeyFramedPoint kfp = it.next();
					if (kfp.getTime() > maxTime)
						maxTime = kfp.getTime();
				}
			}
		}
		return maxTime;
	}
}