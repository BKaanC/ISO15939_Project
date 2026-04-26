package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import controller.Navigator;
import model.Mode;
import model.QualityType;
import model.Scenario;
import model.ScenarioRepository;
import model.Session;

// Step 2 — Define
// 3 ayrı seçim var:
//   a) Quality Type (Product / Process)
//   b) Mode          (Custom / Health / Education)
//   c) Scenario      (modun senaryolarına göre değişir)
// hepsi aynı anda tek seçimi zorunlu kıldığı için JRadioButton + ButtonGroup
public class DefinePanel extends JPanel implements WizardStep {

    private final Session session;
    private final Navigator navigator;
    private final ScenarioRepository repository;

    // quality type için radio'lar
    private JRadioButton productRadio;
    private JRadioButton processRadio;
    private ButtonGroup qualityGroup;

    // mode için radio'lar
    private JRadioButton customRadio;
    private JRadioButton healthRadio;
    private JRadioButton educationRadio;
    private ButtonGroup modeGroup;

    // scenario listesi mode değiştikçe güncelleniyor, dinamik
    private JPanel scenarioPanel;
    private ButtonGroup scenarioGroup;
    // her radio'ya karşılık gelen Scenario nesnesini hatırlamak için
    private List<JRadioButton> scenarioRadios = new ArrayList<>();
    private List<Scenario> scenarioList = new ArrayList<>();

    public DefinePanel(Session session, Navigator navigator, ScenarioRepository repository) {
        this.session = session;
        this.navigator = navigator;
        this.repository = repository;

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        buildUi();
    }

    private void buildUi() {
        // üstte başlık
        JLabel title = new JLabel("Step 2: Define Quality Dimensions");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ortadaki içerik (3 grup alt alta)
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(buildQualitySection());
        content.add(Box.createVerticalStrut(15));
        content.add(buildModeSection());
        content.add(Box.createVerticalStrut(15));
        content.add(buildScenarioSection());

        // uzun listede kaydırma olsun
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // alt tarafta Back / Next butonları
        add(buildButtonBar(), BorderLayout.SOUTH);
    }

    // --- quality type grubu ---
    private JPanel buildQualitySection() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 4));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Quality Type"));

        productRadio = new JRadioButton(QualityType.PRODUCT.getLabel());
        processRadio = new JRadioButton(QualityType.PROCESS.getLabel());
        productRadio.setOpaque(false);
        processRadio.setOpaque(false);

        // aynı anda sadece biri seçilebilsin
        qualityGroup = new ButtonGroup();
        qualityGroup.add(productRadio);
        qualityGroup.add(processRadio);

        panel.add(productRadio);
        panel.add(new JLabel("   " + QualityType.PRODUCT.getDescription()));
        panel.add(processRadio);
        panel.add(new JLabel("   " + QualityType.PROCESS.getDescription()));

        return panel;
    }

    // --- mode grubu ---
    private JPanel buildModeSection() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 4));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Mode"));

        customRadio    = new JRadioButton(Mode.CUSTOM.getLabel()    + " (bonus, empty scenario)");
        healthRadio    = new JRadioButton(Mode.HEALTH.getLabel());
        educationRadio = new JRadioButton(Mode.EDUCATION.getLabel());
        customRadio.setOpaque(false);
        healthRadio.setOpaque(false);
        educationRadio.setOpaque(false);

        modeGroup = new ButtonGroup();
        modeGroup.add(customRadio);
        modeGroup.add(healthRadio);
        modeGroup.add(educationRadio);

        // mod değişince senaryo listesini yenile
        customRadio.addActionListener(e -> refreshScenariosFor(Mode.CUSTOM));
        healthRadio.addActionListener(e -> refreshScenariosFor(Mode.HEALTH));
        educationRadio.addActionListener(e -> refreshScenariosFor(Mode.EDUCATION));

        panel.add(healthRadio);
        panel.add(educationRadio);
        panel.add(customRadio);

        return panel;
    }

    // --- scenario grubu (mode değişince içeriği değişir) ---
    private JPanel buildScenarioSection() {
        scenarioPanel = new JPanel();
        scenarioPanel.setLayout(new BoxLayout(scenarioPanel, BoxLayout.Y_AXIS));
        scenarioPanel.setOpaque(false);
        scenarioPanel.setBorder(BorderFactory.createTitledBorder("Scenario"));

        // başlangıçta bilgilendirme metni
        scenarioPanel.add(new JLabel("  Please select a mode first to see scenarios."));
        return scenarioPanel;
    }

    // seçilen mode'a göre senaryo radio'larını yeniden oluşturuyor
    private void refreshScenariosFor(Mode mode) {
        scenarioPanel.removeAll();
        scenarioRadios.clear();
        scenarioList.clear();
        scenarioGroup = new ButtonGroup();

        List<Scenario> list = repository.getScenarios(mode);
        if (list.isEmpty()) {
            scenarioPanel.add(new JLabel("  No scenarios available for this mode."));
        } else {
            for (Scenario s : list) {
                JRadioButton rb = new JRadioButton(s.getName() + "  —  " + s.getDescription());
                rb.setOpaque(false);
                scenarioGroup.add(rb);
                scenarioPanel.add(rb);
                scenarioRadios.add(rb);
                scenarioList.add(s);
            }
        }
        // değişiklikler yansısın diye
        scenarioPanel.revalidate();
        scenarioPanel.repaint();
    }

    // --- alt buton çubuğu ---
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.setOpaque(false);

        JButton back = new JButton("\u2190 Back");
        back.setPreferredSize(new Dimension(110, 32));
        back.addActionListener(e -> navigator.goBack());

        JButton next = new JButton("Next \u2192");
        next.setPreferredSize(new Dimension(110, 32));
        next.addActionListener(e -> onNext());

        bar.add(back);
        bar.add(next);
        return bar;
    }

    // Next'e basıldığında tüm seçimler yapılmış olmalı
    private void onNext() {
        // 1) quality type seçildi mi
        QualityType qt = null;
        if (productRadio.isSelected()) qt = QualityType.PRODUCT;
        else if (processRadio.isSelected()) qt = QualityType.PROCESS;

        if (qt == null) {
            warn("Please select a quality type (Product or Process).");
            return;
        }

        // 2) mode seçildi mi
        Mode mode = null;
        if (customRadio.isSelected()) mode = Mode.CUSTOM;
        else if (healthRadio.isSelected()) mode = Mode.HEALTH;
        else if (educationRadio.isSelected()) mode = Mode.EDUCATION;

        if (mode == null) {
            warn("Please select a mode (Health, Education or Custom).");
            return;
        }

        // 3) scenario seçildi mi
        Scenario chosen = null;
        for (int i = 0; i < scenarioRadios.size(); i++) {
            if (scenarioRadios.get(i).isSelected()) {
                chosen = scenarioList.get(i);
                break;
            }
        }
        if (chosen == null) {
            warn("Please select a scenario to continue.");
            return;
        }

        // hepsi tamam — session'a yaz
        session.setQualityType(qt);
        session.setMode(mode);
        session.setScenario(chosen);

        navigator.goNext();
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Missing information", JOptionPane.WARNING_MESSAGE);
    }

    // panel tekrar ekrana geldiğinde önceki seçimleri geri yükle
    @Override
    public void onShow() {
        // quality
        if (session.getQualityType() == QualityType.PRODUCT) productRadio.setSelected(true);
        else if (session.getQualityType() == QualityType.PROCESS) processRadio.setSelected(true);

        // mode + senaryolar
        if (session.getMode() != null) {
            if (session.getMode() == Mode.CUSTOM) customRadio.setSelected(true);
            else if (session.getMode() == Mode.HEALTH) healthRadio.setSelected(true);
            else if (session.getMode() == Mode.EDUCATION) educationRadio.setSelected(true);
            refreshScenariosFor(session.getMode());

            // eski seçili senaryoyu işaretle
            if (session.getScenario() != null) {
                for (int i = 0; i < scenarioList.size(); i++) {
                    if (scenarioList.get(i) == session.getScenario()) {
                        scenarioRadios.get(i).setSelected(true);
                        break;
                    }
                }
            }
        }
    }
}
