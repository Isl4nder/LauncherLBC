package me.kioo.ui.component;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import me.kioo.Launcher;

public class TexturedPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image img;
	private Image bgImage;
	private static int SIZE = 169; 

	public TexturedPanel() {
		setOpaque(true);
		try {
			this.bgImage = ImageIO.read(Launcher.class.getResource("/me/kioo/res/texture.jpg")).getScaledInstance(TexturedPanel.SIZE, TexturedPanel.SIZE, 16);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paintComponent(Graphics g2) {
		int w = getWidth() / 2 + 1;
		int h = getHeight() / 2 + 1;
		if ((this.img == null)
		|| (this.img.getWidth(null) != w)
		|| (this.img.getHeight(null) != h)) {
			this.img = createImage(w, h);

			Graphics g = this.img.getGraphics();
			for (int x = 0; x <= w / TexturedPanel.SIZE; x++) {
				for (int y = 0; y <= h / TexturedPanel.SIZE; y++)
					g.drawImage(this.bgImage, x * TexturedPanel.SIZE, y * TexturedPanel.SIZE, null);
			}

			if ((g instanceof Graphics2D)) {
				Graphics2D gg = (Graphics2D) g;
				int gh = 1;
				gg.setPaint(
					new GradientPaint(
						new Point2D.Float(0.0F, 0.0F),
						new Color(553648127, true),
						new Point2D.Float(0.0F, gh),
						new Color(0, true)
					)
				);
				gg.fillRect(0, 0, w, gh);

				gh = h;
				gg.setPaint(
					new GradientPaint(
						new Point2D.Float(0.0F, 0.0F),
						new Color(0, true),
						new Point2D.Float(0.0F, gh),
						new Color(1610612736, true)
					)
				);
				gg.fillRect(0, 0, w, gh);
			}
			g.dispose();
		}
		g2.drawImage(this.img, 0, 0, w * 2, h * 2, null);
	}
}