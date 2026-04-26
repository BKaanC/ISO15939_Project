package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

// ekranın üstünde duran adım göstergesi
// 5 tane daire var, altlarında adım isimleri
// aktif adım mavi, tamamlananlar yeşil tick'li, sonrakiler gri
// custom paint ile çizdim çünkü JLabel'larla aynı görünümü yakalamak zor
public class StepIndicator extends JPanel {

    // adım isimleri — pdf'te aynı sıra
    private final String[] stepNames = {"Profile", "Define", "Plan", "Collect", "Analyse"};

    // şu an aktif olan adım (0..4)
    private int currentStep = 0;

    // renkler (kod içinde sabit tuttum)
    private static final Color COLOR_ACTIVE    = new Color(59, 130, 246);  // mavi
    private static final Color COLOR_COMPLETED = new Color(34, 197, 94);   // yeşil
    private static final Color COLOR_PENDING   = new Color(209, 213, 219); // gri
    private static final Color COLOR_TEXT_DARK = new Color(31, 41, 55);
    private static final Color COLOR_TEXT_MUTED= new Color(107, 114, 128);

    // dairenin çapı
    private static final int CIRCLE_SIZE = 36;

    public StepIndicator() {
        // arka planı beyaz yapıyoruz ki frame'den ayrılsın
        setBackground(Color.WHITE);
        // yüksekliği sabit tuttum, üstte çok yer kaplamasın
        setPreferredSize(new Dimension(800, 80));
    }

    // dışarıdan hangi adımdayız diye bildirince repaint ediyoruz
    public void setCurrentStep(int step) {
        if (step < 0) step = 0;
        if (step > 4) step = 4;
        this.currentStep = step;
        repaint();
    }

    public int getCurrentStep() {
        return currentStep;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        // yumuşak kenar görünümü için antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int count = stepNames.length;

        // her adım için yatay merkez konumunu önceden hesaplıyoruz
        int[] centerX = new int[count];
        // kenarlardan biraz boşluk bırakıp eşit aralıkla dağıttık
        int padding = 40;
        int usable = width - (padding * 2);
        for (int i = 0; i < count; i++) {
            centerX[i] = padding + (int) ((usable / (double) (count - 1)) * i);
        }
        int centerY = height / 2 - 6; // daireyi biraz yukarıda tutup altına isim sığsın

        // önce adımları birleştiren çizgileri çiziyoruz
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < count - 1; i++) {
            // iki adım arası tamamlanmışsa yeşil, değilse gri
            if (i < currentStep) {
                g2.setColor(COLOR_COMPLETED);
            } else {
                g2.setColor(COLOR_PENDING);
            }
            g2.drawLine(centerX[i] + CIRCLE_SIZE / 2,
                        centerY,
                        centerX[i + 1] - CIRCLE_SIZE / 2,
                        centerY);
        }

        // şimdi her adım için daireyi ve yazıyı çizelim
        Font numberFont = new Font("SansSerif", Font.BOLD, 14);
        Font labelFont  = new Font("SansSerif", Font.PLAIN, 12);

        for (int i = 0; i < count; i++) {
            int cx = centerX[i];
            int cy = centerY;
            int x = cx - CIRCLE_SIZE / 2;
            int y = cy - CIRCLE_SIZE / 2;

            // daireyi renklendir (tamamlanan/aktif/sonraki)
            if (i < currentStep) {
                g2.setColor(COLOR_COMPLETED);
            } else if (i == currentStep) {
                g2.setColor(COLOR_ACTIVE);
            } else {
                g2.setColor(COLOR_PENDING);
            }
            g2.fillOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);

            // dairenin ortasında ya tick ya sayı
            g2.setColor(Color.WHITE);
            g2.setFont(numberFont);
            FontMetrics fmNum = g2.getFontMetrics();
            String text = (i < currentStep) ? "\u2713" : String.valueOf(i + 1);
            int tw = fmNum.stringWidth(text);
            int th = fmNum.getAscent();
            g2.drawString(text, cx - tw / 2, cy + th / 2 - 2);

            // altına adım ismi
            g2.setFont(labelFont);
            FontMetrics fmLbl = g2.getFontMetrics();
            // aktif adımı biraz daha belirgin yapalım
            if (i == currentStep) {
                g2.setColor(COLOR_TEXT_DARK);
                g2.setFont(labelFont.deriveFont(Font.BOLD));
            } else {
                g2.setColor(COLOR_TEXT_MUTED);
            }
            String name = stepNames[i];
            int lw = g2.getFontMetrics().stringWidth(name);
            g2.drawString(name, cx - lw / 2, cy + CIRCLE_SIZE / 2 + 18);
        }

        g2.dispose();
    }
}
