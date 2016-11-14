package de.homelab.madgaksha.cgca.path;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

class AnimCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	private Image dbImage;
	private Graphics dbg;
	private final List<IPathCommand> list = new ArrayList<>();
	private final boolean closePath;
	private final boolean fillPath;
	private final EWindingRule windingRule;
	private final Paint paint;
	private float startTime;
	private float currentTime;
	private final Timer timer;
	private float repeat = -1f;
	private boolean forthAndBack = true;

	public AnimCanvas(final List<IPathCommand> list, final boolean closePath, final boolean fillPath, final EWindingRule windingRule, final Paint paint) {
		super();
		this.list.addAll(list);
		this.closePath = closePath;
		this.fillPath = fillPath;
		this.windingRule = windingRule;
		this.paint = paint;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent ev) {
				initBuffer();
			}
		});
		this.timer = new Timer(33, (final ActionEvent ev) ->  {
			repaint();
		});
	}

	@Override
	public void setVisible(final boolean v) {
		super.setVisible(v);
		if (v) {
			startTime = System.nanoTime()*1E-9f;
			startAnim();
		}
	}

	@Override
	public void update(final Graphics g) {
		if (startTime == 0f)
			startTime = System.nanoTime()*1E-9f;
		currentTime = System.nanoTime()*1E-9f - startTime;

		if (repeat > 0f) {
			if (forthAndBack) {
				currentTime = currentTime % (2*repeat);
				if (currentTime > repeat)
					currentTime = repeat - (currentTime-repeat);
			}
			else {
				currentTime = currentTime % repeat;
			}
		}

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
			Graphics dbg = dbImage.getGraphics();
			if (dbg instanceof Graphics2D)
				((Graphics2D)dbg).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.dbg = dbg;
		}
	}

	@Override
	public void paint(final Graphics g) {
		if (!(g instanceof Graphics2D))
			throw new RuntimeException("needs a Graphics2D object: " + g.getClass().getCanonicalName()); //$NON-NLS-1$
		final Graphics2D g2d = (Graphics2D) g;
		drawPath(g2d);
	}

	private void drawPath(final Graphics2D g2d) {
		g2d.setColor(Color.red);
		g2d.setPaint(paint);
		final Path2D.Float path = new Path2D.Float();
		path.setWindingRule(windingRule.getInt());
		path.moveTo(0f, 0f);
		for (final IPathCommand pc : list) {
			pc.apply(path, currentTime);
		}
		if (closePath)
			path.closePath();
		if (fillPath)
			g2d.fill(path);
		else
			g2d.draw(path);
	}

	public void stopAnim() {
		timer.stop();
	}
	public void startAnim() {
		timer.start();
	}

	public void setRepeat(final float repeat) {
		this.repeat  = repeat;
	}
	public void setForthAndBack(final boolean forthAndBack) {
		this.forthAndBack = forthAndBack;
	}
}