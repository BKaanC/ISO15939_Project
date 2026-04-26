package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import model.Scenario;

// Step 5 içinde gösterilen radar (örümcek ağı) grafiği — BONUS
// Java 2D (Graphics2D) ile çizdim, dış kütüphane yok
// her dimension için bir eksen, skorlar 1-5 arasında
public class RadarChartPanel extends JPanel {

    private Scenario scenario;  // hangi senaryoyu çiziyoruz

    // kaç halka (1, 2, 3, 4, 5)
    private static final int RINGS = 5;

    public RadarChartPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(420, 420));
    }

    // senaryo set edilince yeniden çiziyoruz
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scenario == null) return;

        List<model.Dimension> dims = scenario.getDimensions();
        int n = dims.size();
        if (n < 3) {
            // radar için en az 3 eksen olsun (üçgen), yoksa anlamsız çıkar
            g.setColor(Color.GRAY);
            g.drawString("Not enough dimensions for radar chart.", 20, 40);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        // yarıçapı biraz küçük tut, kenarda etiket sığsın
        int radius = Math.min(w, h) / 2 - 50;

        // her dimension için açı (tepe noktasından başla ve saat yönünde dön)
        // -PI/2 = yukarı
        double[] angles = new double[n];
        for (int i = 0; i < n; i++) {
            angles[i] = -Math.PI / 2 + (2 * Math.PI * i / n);
        }

        // 1) arka plan halkaları (1..5 için)
        g2.setStroke(new BasicStroke(1f));
        for (int r = 1; r <= RINGS; r++) {
            double ratio = r / (double) RINGS;
            int[] xs = new int[n];
            int[] ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = (int) (cx + radius * ratio * Math.cos(angles[i]));
                ys[i] = (int) (cy + radius * ratio * Math.sin(angles[i]));
            }
            g2.setColor(new Color(229, 231, 235)); // çok açık gri
            g2.drawPolygon(xs, ys, n);
        }

        // 2) her eksen çizgisi (merkez -> uçtaki köşe)
        g2.setColor(new Color(209, 213, 219));
        for (int i = 0; i < n; i++) {
            int x = (int) (cx + radius * Math.cos(angles[i]));
            int y = (int) (cy + radius * Math.sin(angles[i]));
            g2.drawLine(cx, cy, x, y);
        }

        // 3) ekseni etiketle (dimension adı)
        Font nameFont = new Font("SansSerif", Font.BOLD, 12);
        g2.setFont(nameFont);
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(new Color(55, 65, 81));
        for (int i = 0; i < n; i++) {
            String name = dims.get(i).getName();
            // etiketi ekseninin bir tık dışına koyuyoruz
            int lx = (int) (cx + (radius + 18) * Math.cos(angles[i]));
            int ly = (int) (cy + (radius + 18) * Math.sin(angles[i]));
            int tw = fm.stringWidth(name);
            // yazıyı eksenin yönüne göre ortala
            g2.drawString(name, lx - tw / 2, ly + fm.getAscent() / 2);
        }

        // 4) asıl veriyi çiz — her dimension'ın skoruna göre nokta
        Polygon dataPoly = new Polygon();
        int[] pxs = new int[n];
        int[] pys = new int[n];
        for (int i = 0; i < n; i++) {
            double score = dims.get(i).calculateDimensionScore(); // 1..5
            // merkezde 0 demek çok kötü, ama skor 1 bile biraz dışarıda olsun
            // 0-5 aralığını 0..radius'a eşleyelim
            double ratio = score / 5.0;
            pxs[i] = (int) (cx + radius * ratio * Math.cos(angles[i]));
            pys[i] = (int) (cy + radius * ratio * Math.sin(angles[i]));
            dataPoly.addPoint(pxs[i], pys[i]);
        }

        // dolgu — yarı saydam mavi
        g2.setColor(new Color(59, 130, 246, 80));
        g2.fillPolygon(dataPoly);
        // çerçeve
        g2.setColor(new Color(59, 130, 246));
        g2.setStroke(new BasicStroke(2f));
        g2.drawPolygon(dataPoly);

        // köşe noktaları
        for (int i = 0; i < n; i++) {
            g2.fillOval(pxs[i] - 4, pys[i] - 4, 8, 8);
        }

        // 5) merkezi işaretle
        g2.setColor(new Color(156, 163, 175));
        g2.fillOval(cx - 2, cy - 2, 4, 4);

        g2.dispose();
    }
}
