package de.homelab.madgaksha.cgca.path;

import java.awt.FlowLayout;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

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

class DrawControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final EditCanvas canvas;
	private final MainFrame frame;
	public DrawControlPanel(final EditCanvas canvas, final MainFrame frame) {
		super();
		this.canvas = canvas;
		this.frame = frame;

		final JButton btnClear = new JButton("clear"); //$NON-NLS-1$
		final JButton btnMoveTo = new JButton("moveTo"); //$NON-NLS-1$
		final JButton btnLineTo = new JButton("lineTo"); //$NON-NLS-1$
		final JButton btnQuadTo = new JButton("quadTo"); //$NON-NLS-1$
		final JButton btnCurveTo = new JButton("curveTo"); //$NON-NLS-1$
		final JButton btnChange = new JButton("select..."); //$NON-NLS-1$
		final JButton btnPlayAnim = new JButton("play"); //$NON-NLS-1$
		final JButton btnText = new JButton("load texture..."); //$NON-NLS-1$
		final JCheckBox cbClose = new JCheckBox("close"); //$NON-NLS-1$
		final JCheckBox cbFill = new JCheckBox("fill"); //$NON-NLS-1$
		final JFileChooser fcText = new JFileChooser("load texture"); //$NON-NLS-1$
		final JComboBox<EWindingRule> selWRule = new JComboBox<>(EWindingRule.values());

		setLayout(new FlowLayout());
		add(btnClear);
		add(btnPlayAnim);
		add(btnMoveTo);
		add(btnLineTo);
		add(btnQuadTo);
		add(btnCurveTo);
		add(btnText);
		add(btnChange);
		add(cbClose);
		add(cbFill);
		add(selWRule);

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
			canvas.setMode(EMode.PATH_BUILD);
			canvas.setMode(EMode.POINT_EDIT);		});
		btnPlayAnim.addActionListener((final ActionEvent actionEvent) -> {
			playAnimation();
		});
		fcText.addActionListener((final ActionEvent event) -> {
			SWUtil.srcAs(event, JFileChooser.class).ifPresent((final JFileChooser fileChooser) -> {
				final File file = fileChooser.getSelectedFile();
				try {
					final BufferedImage img = ImageIO.read(file);
					canvas.setFillPaint(new TexturePaint(img, new Rectangle2D.Float(0,0,img.getWidth(), img.getHeight())));
				}
				catch (final Exception e) {
					JOptionPane.showMessageDialog(this, "Failed to load image.\n" + e.getMessage()); //$NON-NLS-1$
				}
			});
		});
		cbClose.addActionListener((final ActionEvent event) -> {
			SWUtil.srcAs(event, JCheckBox.class).ifPresent(cb -> canvas.setClosePath(cb.isSelected()));
		});
		cbFill.addActionListener((final ActionEvent event) -> {
			SWUtil.srcAs(event, JCheckBox.class).ifPresent(cb -> canvas.setFillPath(cb.isSelected()));
		});
		selWRule.addItemListener((final ItemEvent event) -> {
			SWUtil.cboxSelAs(event, EWindingRule.class).ifPresent(windingRule -> {
				canvas.setWindingRule(windingRule);
			});
		});
		selWRule.setSelectedItem(canvas.getWindingRule());
	}
	private void playAnimation() {
		final PlayDialog dialog = new PlayDialog(frame, canvas);
		dialog.setVisible(true);
	}
}