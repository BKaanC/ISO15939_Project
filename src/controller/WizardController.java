package controller;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.ScenarioRepository;
import model.Session;
import view.StepIndicator;
import view.WizardStep;


// - hangi adımdayız, yönetiyor

// - goNext / goBack / goTo çağrılarında hem CardLayout'u hem StepIndicator'ı günceller
// - panelin onShow()'unu çağırır
// - Session ve ScenarioRepository'yi tutar (view'a sadece referans verir)
public class WizardController implements Navigator {

    // toplam adım sayısı sabitt
    private static final int STEP_COUNT = 5;

    // model tarafı
    private final Session session;
    
    private final ScenarioRepository repository;

    // view tarafı (attachUi çağrıldıktan sonra dolduruluyor)
    private StepIndicator indicator;
    private CardLayout cardLayout;
    private JPanel cardHost;

    // her adımın panel referansı ve kart adı
    private final List<WizardStep> steps = new ArrayList<>();
    private final String[] cardNames = {"profile", "define", "plan", "collect", "analyse"};

    // şu anda gösterdiğimiz adımın Indexi
    private int currentIndex = 0;

    public WizardController(Session session, ScenarioRepository repository) {
        this.session = session;
        this.repository = repository;
    }

    // model'e erişmek isteyenler için
    public Session getSession() {
        return session;
    }

    public ScenarioRepository getRepository() {
        return repository;
    }

    // view oluşturulduktan sonra ui bileşenlerini controller'a bağlıyoruz
    // paneller WizardStep'i implemente ettiği için sıralı liste olarak alıyoruz
    // TODO: ileride paneli runtime'da eklemek için bu metodu refactor et
    public void attachUi(StepIndicator indicator,CardLayout cardLayout,JPanel cardHost,WizardStep profile, WizardStep define, WizardStep plan, WizardStep collect,WizardStep analyse) {
        this.indicator = indicator;
        this.cardLayout = cardLayout;
        this.cardHost = cardHost;

        steps.clear();
        steps.add(profile);
        steps.add(define);
        steps.add(plan);
        steps.add(collect);
        steps.add(analyse);
    }

    // uygulamanın ilk ekranını göster
    public void start() {
        goTo(0);
    }

    // ---- Navigator implementation ----

    @Override
    public void goNext() {
        if (currentIndex < STEP_COUNT - 1) goTo(currentIndex + 1);
    }

    @Override
    public void goBack() {
        if (currentIndex > 0) goTo(currentIndex - 1);
    }

    @Override
    public void goTo(int stepIndex) {
        // System.out.println("debug: stepIndex = " + stepIndex);

        // sınırlara sıkıştır kontrol et
        if (stepIndex < 0) stepIndex = 0;
        if (stepIndex > STEP_COUNT - 1) stepIndex = STEP_COUNT - 1;
        currentIndex = stepIndex;

        // step indicator'ı güncelle (sayaç)
        if (indicator != null) {
            indicator.setCurrentStep(stepIndex);
        }

        // kartı değiştir
        if (cardLayout != null && cardHost != null) {
            cardLayout.show(cardHost, cardNames[stepIndex]);
        }

        // panelin onShow'unu çağır (polymorphik)
        if (stepIndex < steps.size()) {
            steps.get(stepIndex).onShow();
        }
    }

   
}
