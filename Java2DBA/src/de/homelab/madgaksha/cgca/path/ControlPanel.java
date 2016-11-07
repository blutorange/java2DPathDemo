package de.homelab.madgaksha.cgca.path;

import java.awt.FlowLayout;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.homelab.madgaksha.cgca.path.paths.CurveToBuilder;
import de.homelab.madgaksha.cgca.path.paths.LineToBuilder;
import de.homelab.madgaksha.cgca.path.paths.MoveToBuilder;
import de.homelab.madgaksha.cgca.path.paths.QuadToBuilder;

class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final DrawingCanvas canvas;
	private final MainFrame frame;
	public ControlPanel(DrawingCanvas canvas, MainFrame frame) {
		this.canvas = canvas;
		this.frame = frame;
		
		final JButton btnClear = new JButton("clear"); //$NON-NLS-1$
		final JButton btnMoveTo = new JButton("moveTo"); //$NON-NLS-1$
		final JButton btnLineTo = new JButton("lineTo"); //$NON-NLS-1$
		final JButton btnQuadTo = new JButton("quadTo"); //$NON-NLS-1$
		final JButton btnCurveTo = new JButton("curveTo"); //$NON-NLS-1$
		final JButton btnChange = new JButton("change"); //$NON-NLS-1$
		final JButton btnPlayAnim = new JButton("play..."); //$NON-NLS-1$
		final JButton btnText = new JButton("Load texture..."); //$NON-NLS-1$
		final JCheckBox cbClose = new JCheckBox("close"); //$NON-NLS-1$
		final JCheckBox cbFill = new JCheckBox("fill"); //$NON-NLS-1$
		final JFileChooser fcText = new JFileChooser("Load texture..."); //$NON-NLS-1$
		final JComboBox<EWindingRule> selWRule = new JComboBox<>(EWindingRule.values());

		setLayout(new FlowLayout());
		add(btnClear);
		add(btnMoveTo);
		add(btnLineTo);
		add(btnQuadTo);
		add(btnCurveTo);
		add(btnText);
		add(btnChange);
		add(cbClose);
		add(cbFill);
		add(selWRule);
		add(btnPlayAnim);
		
		btnClear.addActionListener((final ActionEvent actionEvent) -> {
			canvas.clearPathList();
		});
		btnMoveTo.addActionListener((final ActionEvent actionEvent) -> {
			canvas.beginPath(new MoveToBuilder());
		});
		btnLineTo.addActionListener((final ActionEvent actionEvent) -> {
			canvas.beginPath(new LineToBuilder());
		});
		btnQuadTo.addActionListener((final ActionEvent actionEvent) -> {
			canvas.beginPath(new QuadToBuilder());
		});
		btnCurveTo.addActionListener((final ActionEvent actionEvent) -> {
			canvas.beginPath(new CurveToBuilder());
		});
		btnText.addActionListener((final ActionEvent actionEvent) -> {
			fcText.showOpenDialog(this);
		});
		btnChange.addActionListener((final ActionEvent actionEvent) -> {
			canvas.setMode(EMode.POINT_EDIT);
		});
		btnPlayAnim.addActionListener((final ActionEvent actionEvent) -> {
			playAnimation();
		});
		fcText.addActionListener((final ActionEvent actionEvent) -> {
			final Object source = actionEvent.getSource();
			if (source instanceof JFileChooser) {
				final File file = ((JFileChooser)source).getSelectedFile();
				try {
					final BufferedImage img = ImageIO.read(file);
					canvas.setFillPaint(new TexturePaint(img, new Rectangle2D.Float(0,0,img.getWidth(), img.getHeight())));
				}
				catch (final Exception e) {
					JOptionPane.showMessageDialog(this, "Failed to load image."); //$NON-NLS-1$
				}
			}
			else
				MainFrame.LOG.log(Level.SEVERE, "not a checkbox instance: " + source.getClass().getCanonicalName()); //$NON-NLS-1$
		});
		cbClose.addActionListener((final ActionEvent actionEvent) -> {
			final Object source = actionEvent.getSource();
			if (source instanceof JCheckBox)
				canvas.setClosePath(((JCheckBox)source).isSelected());
			else
				MainFrame.LOG.log(Level.SEVERE, "not a checkbox instance: " + source.getClass().getCanonicalName()); //$NON-NLS-1$
		});
		cbFill.addActionListener((final ActionEvent actionEvent) -> {
			final Object source = actionEvent.getSource();
			if (source instanceof JCheckBox)
				canvas.setFillPath(((JCheckBox)source).isSelected());
			else
				MainFrame.LOG.log(Level.SEVERE, "not a checkbox instance: " + source.getClass().getCanonicalName()); //$NON-NLS-1$
		});
		selWRule.addActionListener((final ActionEvent actionEvent) -> {
			final Object source = actionEvent.getSource();
			if (source instanceof JComboBox<?>) {
				final JComboBox<?> genBox = (JComboBox<?>)source;
				final Object genSel = genBox.getSelectedItem();
				if (genSel instanceof EWindingRule)
					canvas.setWindingRule((EWindingRule)genSel);
				else
					MainFrame.LOG.log(Level.SEVERE, "not a winding rule: " + genSel.getClass().getCanonicalName()); //$NON-NLS-1$
			}
			else
				MainFrame.LOG.log(Level.SEVERE, "not a checkbox instance: " + source.getClass().getCanonicalName()); //$NON-NLS-1$
		});
		selWRule.setSelectedItem(canvas.getWindingRule());
	}
	private void playAnimation() {
		PlayDialog dialog = new PlayDialog(frame, canvas);
		dialog.setVisible(true);
	}
}