package Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class RoundedPanelDashboard extends JPanel {
    private int cornerRadius;
    private Color borderColor;
    private int borderWidth;

    public RoundedPanelDashboard(int cornerRadius, Color borderColor, int borderWidth) {
        this.cornerRadius = cornerRadius;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Drawing background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(
                borderWidth / 2.0f, borderWidth / 2.0f,
                getWidth() - borderWidth, getHeight() - borderWidth,
                cornerRadius, cornerRadius
        ));

        // Drawing border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderWidth));
        g2.draw(new RoundRectangle2D.Float(
                borderWidth / 2.0f, borderWidth / 2.0f,
                getWidth() - borderWidth, getHeight() - borderWidth,
                cornerRadius, cornerRadius
        ));
    }
}
