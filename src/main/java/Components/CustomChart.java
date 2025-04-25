package Components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

public class CustomChart extends JPanel {
    private int[] incomeData; // Array to hold the income data
    private int[] outcomeData; // Array to hold the outcome data
    private String[] xLabels; // Labels for the x-axis
    private String[] yLabels; // Labels for the y-axis
    private int animationStep = 0; // Tracks the current step of the animation
    private Timer animationTimer;

    public CustomChart(int[] incomeData, int[] outcomeData, String[] xLabels, String[] yLabels) {
        this.incomeData = incomeData; // Initialize the income data
        this.outcomeData = outcomeData; // Initialize the outcome data
        this.xLabels = xLabels; // Initialize x-axis labels
        this.yLabels = yLabels; // Initialize y-axis labels

        // Initialize the animation timer
        animationTimer = new Timer(50, e -> {
            animationStep++; // Increment the animation step
            repaint(); // Repaint the chart to reflect the animation
            if (animationStep > incomeData.length - 1) {
                animationTimer.stop(); // Stop the animation when all points are drawn
            }
        });
        animationTimer.start(); // Start the animation
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int padding = 30;
    int width = getWidth();
    int height = getHeight();

    int chartWidth = width - 2 * padding;
    int chartHeight = height - 2 * padding;

    // Background putih
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);

    // Sumbu X dan Y
    g.setColor(Color.BLACK);
    g.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
    g.drawLine(padding, height - padding, padding, padding); // Y-axis

    if (incomeData != null && outcomeData != null) {
        int maxData = 100;
        double xScale = (double) chartWidth / (incomeData.length - 1);
        double yScale = (double) chartHeight / maxData;

        // Garis Income (biru)
        g.setColor(Color.BLUE);
        for (int i = 0; i < animationStep && i < incomeData.length - 1; i++) {
            int x1 = padding + (int) (i * xScale);
            int y1 = height - padding - (int) (incomeData[i] * yScale);
            int x2 = padding + (int) ((i + 1) * xScale);
            int y2 = height - padding - (int) (incomeData[i + 1] * yScale);
            g.drawLine(x1, y1, x2, y2);
        }

        // Garis Outcome (merah)
        g.setColor(Color.RED);
        for (int i = 0; i < animationStep && i < outcomeData.length - 1; i++) {
            int x1 = padding + (int) (i * xScale);
            int y1 = height - padding - (int) (outcomeData[i] * yScale);
            int x2 = padding + (int) ((i + 1) * xScale);
            int y2 = height - padding - (int) (outcomeData[i + 1] * yScale);
            g.drawLine(x1, y1, x2, y2);
        }

        // Titik Income
        g.setColor(Color.BLUE);
        for (int i = 0; i <= animationStep && i < incomeData.length; i++) {
            int x = padding + (int) (i * xScale);
            int y = height - padding - (int) (incomeData[i] * yScale);
            g.fillOval(x - 2, y - 2, 4, 4);
        }

        // Titik Outcome
        g.setColor(Color.RED);
        for (int i = 0; i <= animationStep && i < outcomeData.length; i++) {
            int x = padding + (int) (i * xScale);
            int y = height - padding - (int) (outcomeData[i] * yScale);
            g.fillOval(x - 2, y - 2, 4, 4);
        }

        // Label X (misalnya bulan)
        g.setColor(Color.BLACK);
        if (xLabels != null && xLabels.length == incomeData.length) {
            for (int i = 0; i < xLabels.length; i++) {
                int x = padding + (int) (i * xScale);
                g.drawString(xLabels[i], x - 10, height - padding + 15);
            }
        }

        // Label Y (angka 0 - 100)
        if (yLabels != null && yLabels.length > 1) {
            int yStep = chartHeight / (yLabels.length - 1);
            for (int i = 0; i < yLabels.length; i++) {
                int y = height - padding - i * yStep;
                g.drawString(yLabels[i], padding - 25, y + 5);
            }
        }
    }

        // Example static content
        g.setColor(Color.BLACK);
        g.drawString("GRAFIK PEMASUKAN DAN PENGELUARAN", getWidth() / 2 - 130, 20);
    }
}