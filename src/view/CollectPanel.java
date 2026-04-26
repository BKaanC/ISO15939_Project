package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.Navigator;
import model.Metric;
import model.Scenario;
import model.Session;

// Step 4 — Collect Data
// her metrik için ham değeri (value) ve hesaplanmış skoru gösteriyoruz
// skor sütununu biraz renklendirdim ki görselliği düzgün olsun (gui quality kriteri)
public class CollectPanel extends JPanel implements WizardStep {

    private final Session session;
    private final Navigator navigator;

    private JPanel content;

    public CollectPanel(Session session, Navigator navigator) {
        this.session = session;
        this.navigator = navigator;

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        buildUi();
    }

    private void buildUi() {
        // başlık
        JLabel title = new JLabel("Step 4: Collect Data");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // içerik — her dimension için ayrı tablo
        content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // alt buton çubuğu
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.setOpaque(false);

        JButton back = new JButton("\u2190 Back");
        back.setPreferredSize(new Dimension(110, 32));
        back.addActionListener(e -> navigator.goBack());

        JButton next = new JButton("Analyse \u2192");
        next.setPreferredSize(new Dimension(130, 32));
        next.addActionListener(e -> navigator.goNext());

        bar.add(back);
        bar.add(next);
        add(bar, BorderLayout.SOUTH);
    }

    @Override
    public void onShow() {
        content.removeAll();

        Scenario scenario = session.getScenario();
        if (scenario == null) {
            content.add(new JLabel("No scenario selected."));
        } else {
            for (model.Dimension dim : scenario.getDimensions()) {
                content.add(buildDimensionSection(dim));
                content.add(Box.createVerticalStrut(12));
            }
        }

        content.revalidate();
        content.repaint();
    }

    // bir dimension için tablo oluşturuyor, skor sütununu renkli gösteriyor
    private JPanel buildDimensionSection(model.Dimension dim) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // dimension başlığı
        JLabel header = new JLabel(dim.getHeaderLabel());
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panel.add(header, BorderLayout.NORTH);

        // tablo kolonları — pdf'teki görünümle aynı
        String[] columns = {"Metric", "Direction", "Range", "Value", "Score (1-5)", "Coeff / Unit"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Metric m : dim.getMetrics()) {
            double score = m.calculateScore();
            Object[] row = {
                    m.getName(),
                    m.getDirectionLabel(),
                    m.getRangeLabel(),
                    formatValue(m.getValue()),
                    formatScore(score),
                    m.getCoefficient() + " / " + m.getUnit()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);

        // skor sütununu skoruna göre renklendiren özel renderer
        table.getColumnModel().getColumn(4).setCellRenderer(new ScoreCellRenderer());

        JScrollPane tableScroll = new JScrollPane(table);
        int rows = dim.getMetrics().size();
        tableScroll.setPreferredSize(new Dimension(760, (rows + 1) * 24 + 4));

        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }

    // tam sayıysa ".0" göstermemek için küçük helper
    private String formatValue(double v) {
        if (v == (int) v) return String.valueOf((int) v);
        return String.valueOf(v);
    }

    // skoru "5.0" veya "3.5" gibi basitçe yazdırıyoruz
    private String formatScore(double s) {
        return String.valueOf(s);
    }

    // skor hücresi için renklendirme renderer'ı
    // yüksek skor yeşil, orta sarımsı, düşük kırmızımsı
    private static class ScoreCellRenderer extends DefaultTableCellRenderer {
        public ScoreCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            // seçiliyken zaten highlight olsun, ama biz de renkten anlaşılsın diye
            if (!isSelected) {
                try {
                    double score = Double.parseDouble(String.valueOf(value));
                    if (score >= 4.0) {
                        c.setBackground(new Color(187, 247, 208)); // açık yeşil
                    } else if (score >= 3.0) {
                        c.setBackground(new Color(254, 240, 138)); // açık sarı
                    } else {
                        c.setBackground(new Color(254, 202, 202)); // açık kırmızı
                    }
                } catch (NumberFormatException ex) {
                    c.setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }
}
