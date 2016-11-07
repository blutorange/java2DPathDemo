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
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListModel;

import de.homelab.madgaksha.cgca.path.IPathPoint.PathPoint;

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
					mouseRight(ev);
				break;
				}
			}

			private void mouseRight(final MouseEvent ev) {
				switch (mode) {
				case PATH_BUILD:
					break;
				case POINT_EDIT:
					IPathPoint p = selectedPoint;
					if (p == null)
						p = getClosestNearMouse();
					if (p != null && selectedPath != null) {
						pathCommandList.remove(selectedPath);
						selectedPath = null;
						selectedPoint = null;
						if (pathCommandList.isEmpty())
							setMode(EMode.PATH_BUILD);
						repaint();
					}
					break;
				default:
					break;
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
						selectedPoint = getClosestNearMouse();
						if (selectedPoint != null) {
							list.setModel(selectedPoint);
							list.setSelectionModel(selectedPoint);
						}
					}
					else {
						final Point p = getMousePosition();
						if (p != null) {
							selectedPoint.setPoint((float)p.getX(), (float)p.getY());
							selectedPoint = null;
							selectedPath = null;
							repaint();
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

	private static void drawPathPoint(final Graphics2D g2d, final IPathPoint pp, final boolean highlight) {
		g2d.setColor(highlight ? Color.GREEN : Color.BLACK);
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
		if (windingRule != null)
			this.windingRule = windingRule;
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
			list.setModel(DummyModel.INSTANCE);
			list.setSelectionModel(new DefaultListSelectionModel());
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

	public void addKeyFrame() {
		final ListModel<?> model = list.getModel();
		if (model instanceof IPathPoint) {
			final IPathPoint p = (IPathPoint)model;
			p.addKeyFrame();
		}
	}

	public void setTime(final float f) {
		final ListModel<?> model = list.getModel();
		if (model instanceof IPathPoint) {
			final IPathPoint p = (IPathPoint)model;
			p.setTime(f);
		}
	}

	public AnimCanvas getAnimationCanvas() {
		return new AnimCanvas(pathCommandList, closePath, fillPath, windingRule, paint);
	}
}