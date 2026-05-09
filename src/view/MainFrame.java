package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.WizardController;
import model.ScenarioRepository;
import model.Session;

// ana pencere (sadece ui katmanı)
// tüm adım değişimi , iş mantığı WizardController'da
// bu class yalnızca component'leri bir araya getiriyor
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("ISO 15939 Measurement Process Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null); // ekranı ortalasın
        getContentPane().setBackground(Color.WHITE);

        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        //model'i ve controller'ı kuruyoruz
        Session session = new Session();
        ScenarioRepository repository = new ScenarioRepository();
        WizardController controller = new WizardController(session, repository);

        //üstteki step indicator
        StepIndicator indicator = new StepIndicator();
        indicator.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        add(indicator, BorderLayout.NORTH);

        // kart alanı 
        CardLayout cardLayout = new CardLayout();
        JPanel cardHost = new JPanel(cardLayout);
        cardHost.setBackground(Color.WHITE);
        add(cardHost, BorderLayout.CENTER);

        //panelleri oluştur (Navigator olarak controller'ı geçiyoruz)
        ProfilePanel profilePanel = new ProfilePanel(session, controller);
        DefinePanel  definePanel  = new DefinePanel(session, controller, repository);
        PlanPanel    planPanel    = new PlanPanel(session, controller);
        CollectPanel collectPanel = new CollectPanel(session, controller);
        AnalysePanel analysePanel = new AnalysePanel(session, controller);

        // kartları sırayla ekle
        cardHost.add(profilePanel, "profile");
        cardHost.add(definePanel,  "define");
        cardHost.add(planPanel,    "plan");
        cardHost.add(collectPanel, "collect");
        cardHost.add(analysePanel, "analyse");

        //controller'a UI referanslarını bağlicaz
        controller.attachUi(indicator, cardLayout, cardHost,
                profilePanel, definePanel, planPanel, collectPanel, analysePanel);

        // ilk adımı göster
        controller.start();
    }
}
