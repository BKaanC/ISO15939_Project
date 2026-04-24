package model;

// Session = kullanıcının bu oturumda girdiği tüm bilgiler
// Step 1'de profile, Step 2'de seçimler buraya yazılıyor
// böylece bir sonraki ekranda verilere ulaşabiliyoruz
// (hepsini ana frame'de tek bir Session nesnesi olarak dolaştıracağız)
public class Session {

    // Step 1 — Profile bilgileri
    private String username;
    private String school;
    private String sessionName;

    // Step 2 — Define seçimleri
    private QualityType qualityType;
    private Mode mode;
    private Scenario scenario;

    public Session() {
        // başlangıçta her şey boş, kullanıcı wizard'da doldurduğunda güncellenecek
    }

    // Step 1 validation için yardımcı
    // true dönerse 3 alan da dolu demektir
    public boolean isProfileComplete() {
        return username != null && !username.trim().isEmpty()
                && school != null && !school.trim().isEmpty()
                && sessionName != null && !sessionName.trim().isEmpty();
    }

    // Step 2 validation için yardımcı
    // hem quality hem mode hem scenario seçilmişse true
    public boolean isDefineComplete() {
        return qualityType != null && mode != null && scenario != null;
    }

    // ---- getter / setter'lar ----

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public QualityType getQualityType() {
        return qualityType;
    }

    public void setQualityType(QualityType qualityType) {
        this.qualityType = qualityType;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
}
