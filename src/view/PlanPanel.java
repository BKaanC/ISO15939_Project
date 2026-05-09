package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.Navigator;
import model.Metric;
import model.Scenario;
import model.Session;

// Plan Measurement
// seçilen senaryonun dimension + metric bilgilerini gösteriyoruz
// read-only (kullanıcı değiştiremez)
public class PlanPanel extends JPanel implements WizardStep {

    private final Session session;
    private final Navigator navigator;

    // içeriği her senaryo değiştiğinde yenilemek lazım, bu yüzden referansı tutucaz
    private JPanel content;

    public PlanPanel(Session session, Navigator navigator) {
        this.session = session;
        this.navigator = navigator;

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        buildUi();
    }

    private void buildUi() {
        // üst başlık
        JLabel title = new JLabel("Step 3: Plan Measurement");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // orta içerik  her dimension için ayrı başlık + tablo
        content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // alt tarafta Back / Next
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.setOpaque(false);

        JButton back = new JButton("\u2190 Back");
        back.setPreferredSize(new Dimension(110, 32));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigator.goBack();
            }
        });

        JButton next = new JButton("Next \u2192");
        next.setPreferredSize(new Dimension(110, 32));
        next.addActionListener(e -> navigator.goNext());

        bar.add(back);
        bar.add(next);
        add(bar, BorderLayout.SOUTH);
    }

    // panel ekrana gelince çağrılıyor, senaryo verisini basıyoruz
    @Override
    public void onShow() {
        content.removeAll();

        Scenario scenario = session.getScenario();
        if (scenario == null) {
            //önlem
            content.add(new JLabel("No scenario selected."));
        } else {
            // her dimension için bir başlık ve tablo ekle
            for (model.Dimension dim : scenario.getDimensions()) {
                content.add(buildDimensionSection(dim));
                content.add(Box.createVerticalStrut(12));
            }
        }

        content.revalidate();
        content.repaint();
    }

    // tek bir dimension için başlık + metric tablosu döndürüyor
    // not: java.awt.Dimension ile çakışmasın diye model.Dimension yazdım
    private JPanel buildDimensionSection(model.Dimension dim) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // dimension başlığı "Usability (Coefficient: 25)"
        JLabel header = new JLabel(dim.getHeaderLabel());
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        panel.add(header, BorderLayout.NORTH);

        // metric tablosu
        String[] columns = {"Metric", "Coefficient", "Direction", "Range", "Unit"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            // hücreler read-only olsun
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Metric m : dim.getMetrics()) {
            Object[] row = {
                    m.getName(),
                    m.getCoefficient(),
                    m.getDirectionLabel(),
                    m.getRangeLabel(),
                    m.getUnit()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        // tabloyu scroll içine koyuyoruz
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(700, 120));

        panel.add(tableScroll, BorderLayout.CENTER);
        return panel;
    }
}
