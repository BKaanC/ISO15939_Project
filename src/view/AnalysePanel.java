package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import controller.Navigator;
import model.Scenario;
import model.Session;

// Step 5 — Analyse
// 3 bölümden oluşuyor:
//   5a) her dimension'ın ağırlıklı ortalaması (JProgressBar ile)
//   5b) radar chart (RadarChartPanel — bonus)
//   5c) gap analysis — en düşük skorlu dimension bilgisi
public class AnalysePanel extends JPanel implements WizardStep {

    private final Session session;
    private final Navigator navigator;

    // alt bileşenler
    private JPanel dimensionBarsPanel; // 5a
    private RadarChartPanel radarPanel; // 5b
    private JPanel gapPanel;            // 5c

    public AnalysePanel(Session session, Navigator navigator) {
        this.session = session;
        this.navigator = navigator;

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        buildUi();
    }

    private void buildUi() {
        // başlık
        JLabel title = new JLabel("Step 5: Analyse");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ortadaki içerik, dikey yerleşim
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // --- 5a: dimension bar'ları ---
        JLabel header1 = new JLabel("Dimension Scores (weighted average of metrics)");
        header1.setFont(new Font("SansSerif", Font.BOLD, 14));
        header1.setAlignmentX(LEFT_ALIGNMENT);
        center.add(header1);
        center.add(Box.createVerticalStrut(6));

        dimensionBarsPanel = new JPanel();
        dimensionBarsPanel.setOpaque(false);
        dimensionBarsPanel.setLayout(new BoxLayout(dimensionBarsPanel, BoxLayout.Y_AXIS));
        dimensionBarsPanel.setAlignmentX(LEFT_ALIGNMENT);
        center.add(dimensionBarsPanel);

        center.add(Box.createVerticalStrut(16));

        // --- 5b ve 5c: radar + gap analizi yan yana ---
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 16, 0));
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(LEFT_ALIGNMENT);

        radarPanel = new RadarChartPanel();
        radarPanel.setBorder(BorderFactory.createTitledBorder("Radar Chart"));
        bottomRow.add(radarPanel);

        gapPanel = new JPanel();
        gapPanel.setOpaque(false);
        gapPanel.setLayout(new BoxLayout(gapPanel, BoxLayout.Y_AXIS));
        gapPanel.setBorder(BorderFactory.createTitledBorder("Gap Analysis"));
        bottomRow.add(gapPanel);

        center.add(bottomRow);

        // scroll içine alıyoruz, dar ekranda taşmasın
        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // alt buton çubuğu
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.setOpaque(false);

        JButton back = new JButton("\u2190 Back");
        back.setPreferredSize(new Dimension(110, 32));
        back.addActionListener(e -> navigator.goBack());

        JButton restart = new JButton("Finish / Restart");
        restart.setPreferredSize(new Dimension(150, 32));
        restart.addActionListener(e -> onRestart());

        bar.add(back);
        bar.add(restart);
        add(bar, BorderLayout.SOUTH);
    }

    @Override
    public void onShow() {
        Scenario scenario = session.getScenario();
        if (scenario == null) {
            return;
        }

        // --- 5a ---
        dimensionBarsPanel.removeAll();
        for (model.Dimension d : scenario.getDimensions()) {
            dimensionBarsPanel.add(buildDimensionRow(d));
            dimensionBarsPanel.add(Box.createVerticalStrut(6));
        }

        // --- 5b ---
        radarPanel.setScenario(scenario);

        // --- 5c ---
        fillGapAnalysis(scenario);

        revalidate();
        repaint();
    }

    // tek bir dimension için isim + skor + progress bar
    private JPanel buildDimensionRow(model.Dimension d) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);

        double score = d.calculateDimensionScore();

        JLabel name = new JLabel(d.getName());
        name.setFont(new Font("SansSerif", Font.PLAIN, 13));
        name.setPreferredSize(new Dimension(170, 20));

        // progress bar 0-100 yerine 100 tabanında gösteriyoruz (skor * 20)
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue((int) Math.round(score * 20));
        bar.setStringPainted(true);
        bar.setString(String.format("%.2f / 5.00", score));
        bar.setForeground(colorFor(score));

        row.add(name, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    // gap analizi bölümünü dolduruyor
    private void fillGapAnalysis(Scenario scenario) {
        gapPanel.removeAll();

        model.Dimension lowest = scenario.findLowestScoringDimension();
        if (lowest == null) {
            gapPanel.add(new JLabel("No dimensions available."));
            return;
        }

        double score = lowest.calculateDimensionScore();
        double gap = 5.0 - score;
        String level = qualityLevel(score);

        gapPanel.add(padLabel("Lowest dimension: " + lowest.getName(), Font.BOLD, 14));
        gapPanel.add(Box.createVerticalStrut(4));
        gapPanel.add(padLabel(String.format("Score: %.2f / 5.00", score), Font.PLAIN, 13));
        gapPanel.add(padLabel(String.format("Gap:   %.2f", gap), Font.PLAIN, 13));
        gapPanel.add(padLabel("Quality level: " + level, Font.PLAIN, 13));
        gapPanel.add(Box.createVerticalStrut(6));

        JLabel note = new JLabel(
                "<html><i>This dimension has the lowest score and requires the most improvement.</i></html>");
        note.setForeground(new Color(107, 114, 128));
        gapPanel.add(note);
    }

    // gap panelinde kullanmak için küçük etiket yardımcı metodu
    private JLabel padLabel(String text, int style, int size) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", style, size));
        l.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        return l;
    }

    // skora göre kalite seviyesi etiketi
    private String qualityLevel(double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 3.5) return "Good";
        if (score >= 2.5) return "Needs Improvement";
        return "Poor";
    }

    // progress bar rengi skora göre değişsin
    private Color colorFor(double score) {
        if (score >= 4.0) return new Color(34, 197, 94);   // yeşil
        if (score >= 3.0) return new Color(234, 179, 8);   // sarı
        if (score >= 2.0) return new Color(249, 115, 22);  // turuncu
        return new Color(239, 68, 68);                     // kırmızı
    }

    // "Finish / Restart" — oturumu sıfırla ve başa dön
    private void onRestart() {
        int answer = JOptionPane.showConfirmDialog(this,
                "Start a new session? Current data will be cleared.",
                "Restart",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            // session'ı temizle
            session.setUsername(null);
            session.setSchool(null);
            session.setSessionName(null);
            session.setQualityType(null);
            session.setMode(null);
            session.setScenario(null);
            // ilk ekrana dön
            navigator.goTo(0);
        }
    }
}
