package model;

// Step 2'nin ikinci kısmında kullanıcı mod seçiyor
// Custom = kendi metriklerini tanımlar (bonus)
// Health = hazır sağlık senaryoları
// Education = hazır eğitim senaryoları
public enum Mode {

    CUSTOM("Custom", "User defines their own dimensions and metrics from scratch"),
    HEALTH("Health", "Health management system scenarios"),
    EDUCATION("Education", "Education LMS system scenarios");

    private final String label;
    private final String description;

    Mode(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
