package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ScenarioRepository = tüm senaryoları tutan merkezi sınıf
// pdf'te "scenario data may be defined hard-coded" diyor, biz de öyle yaptık
// HashMap kullandık çünkü Mode'a göre senaryo listesini çabuk bulmak lazım
// (collections kriteri de bu şekilde kapsanmış oluyor — ArrayList + HashMap)
public class ScenarioRepository {

    // key = Mode, value = o moda ait senaryolar
    private Map<Mode, List<Scenario>> scenariosByMode;

    public ScenarioRepository() {
        scenariosByMode = new HashMap<>();
        // her mod için senaryo listesi hazırlıyoruz
        loadEducationScenarios();
        loadHealthScenarios();
        loadCustomScenarios();
    }

    // dışarıdan bir mod verilince o modun senaryolarını dönüyor
    public List<Scenario> getScenarios(Mode mode) {
        List<Scenario> list = scenariosByMode.get(mode);
        // null dönmesin diye boş liste veriyoruz
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    // ======================= EDUCATION =======================
    // pdf'teki örnek dataset'ten üretildi (Scenario C)
    // Scenario D'yi de aynı yapıda farklı değerlerle kendim oluşturdum
    private void loadEducationScenarios() {
        List<Scenario> list = new ArrayList<>();

        // ---------- Scenario C — Team Alpha (pdf'teki örnek) ----------
        Scenario c = new Scenario(
                "Scenario C \u2014 Team Alpha",
                "LMS system, overall strong performance",
                Mode.EDUCATION);

        Dimension usabilityC = new Dimension("Usability", 25);
        usabilityC.addMetric(new HigherIsBetterMetric("SUS score", 50, 0, 100, "points", 89));
        usabilityC.addMetric(new LowerIsBetterMetric("Onboarding time", 50, 0, 60, "min", 5));
        c.addDimension(usabilityC);

        Dimension perfC = new Dimension("Perf. Efficiency", 20);
        perfC.addMetric(new LowerIsBetterMetric("Video start time", 50, 0, 15, "sec", 3));
        perfC.addMetric(new HigherIsBetterMetric("Concurrent exams", 50, 0, 600, "users", 450));
        c.addDimension(perfC);

        Dimension accC = new Dimension("Accessibility", 20);
        accC.addMetric(new HigherIsBetterMetric("WCAG compliance", 50, 0, 100, "%", 85));
        accC.addMetric(new HigherIsBetterMetric("Screen reader score", 50, 0, 100, "%", 78));
        c.addDimension(accC);

        Dimension relC = new Dimension("Reliability", 20);
        relC.addMetric(new HigherIsBetterMetric("Uptime", 50, 95, 100, "%", 99));
        relC.addMetric(new LowerIsBetterMetric("MTTR", 50, 0, 120, "min", 25));
        c.addDimension(relC);

        Dimension funcC = new Dimension("Func. Suitability", 15);
        funcC.addMetric(new HigherIsBetterMetric("Feature completion", 50, 0, 100, "%", 92));
        funcC.addMetric(new HigherIsBetterMetric("Assignment submit rate", 50, 0, 100, "%", 87));
        c.addDimension(funcC);

        list.add(c);

        // ---------- Scenario D — Team Beta (daha zayıf performans) ----------
        Scenario d = new Scenario(
                "Scenario D \u2014 Team Beta",
                "LMS system, average performance",
                Mode.EDUCATION);

        Dimension usabilityD = new Dimension("Usability", 25);
        usabilityD.addMetric(new HigherIsBetterMetric("SUS score", 50, 0, 100, "points", 65));
        usabilityD.addMetric(new LowerIsBetterMetric("Onboarding time", 50, 0, 60, "min", 25));
        d.addDimension(usabilityD);

        Dimension perfD = new Dimension("Perf. Efficiency", 20);
        perfD.addMetric(new LowerIsBetterMetric("Video start time", 50, 0, 15, "sec", 8));
        perfD.addMetric(new HigherIsBetterMetric("Concurrent exams", 50, 0, 600, "users", 200));
        d.addDimension(perfD);

        Dimension accD = new Dimension("Accessibility", 20);
        accD.addMetric(new HigherIsBetterMetric("WCAG compliance", 50, 0, 100, "%", 50));
        accD.addMetric(new HigherIsBetterMetric("Screen reader score", 50, 0, 100, "%", 45));
        d.addDimension(accD);

        Dimension relD = new Dimension("Reliability", 20);
        relD.addMetric(new HigherIsBetterMetric("Uptime", 50, 95, 100, "%", 97));
        relD.addMetric(new LowerIsBetterMetric("MTTR", 50, 0, 120, "min", 70));
        d.addDimension(relD);

        Dimension funcD = new Dimension("Func. Suitability", 15);
        funcD.addMetric(new HigherIsBetterMetric("Feature completion", 50, 0, 100, "%", 70));
        funcD.addMetric(new HigherIsBetterMetric("Assignment submit rate", 50, 0, 100, "%", 60));
        d.addDimension(funcD);

        list.add(d);

        scenariosByMode.put(Mode.EDUCATION, list);
    }

    // ======================= HEALTH =======================
    // sağlık yönetim sistemleri için kendi tasarladığım senaryolar
    private void loadHealthScenarios() {
        List<Scenario> list = new ArrayList<>();

        // ---------- Scenario A — Hospital Management System ----------
        Scenario a = new Scenario(
                "Scenario A \u2014 Hospital Management",
                "Hospital-wide management system, high quality",
                Mode.HEALTH);

        Dimension security = new Dimension("Security", 30);
        security.addMetric(new HigherIsBetterMetric("Auth success rate", 60, 0, 100, "%", 98));
        security.addMetric(new HigherIsBetterMetric("Data encryption", 40, 0, 100, "%", 95));
        a.addDimension(security);

        Dimension reliability = new Dimension("Reliability", 25);
        reliability.addMetric(new HigherIsBetterMetric("Uptime", 50, 95, 100, "%", 99.5));
        reliability.addMetric(new HigherIsBetterMetric("MTBF", 50, 0, 1000, "hours", 800));
        a.addDimension(reliability);

        Dimension performance = new Dimension("Performance", 25);
        performance.addMetric(new LowerIsBetterMetric("Response time", 50, 0, 5, "sec", 1.2));
        performance.addMetric(new HigherIsBetterMetric("Throughput", 50, 0, 1000, "tps", 650));
        a.addDimension(performance);

        Dimension usabilityH = new Dimension("Usability", 20);
        usabilityH.addMetric(new HigherIsBetterMetric("Task completion rate", 50, 0, 100, "%", 85));
        usabilityH.addMetric(new LowerIsBetterMetric("Error rate", 50, 0, 20, "%", 3));
        a.addDimension(usabilityH);

        list.add(a);

        // ---------- Scenario B — Clinic Management System ----------
        Scenario b = new Scenario(
                "Scenario B \u2014 Clinic Management",
                "Small clinic system, moderate performance",
                Mode.HEALTH);

        Dimension securityB = new Dimension("Security", 30);
        securityB.addMetric(new HigherIsBetterMetric("Auth success rate", 60, 0, 100, "%", 88));
        securityB.addMetric(new HigherIsBetterMetric("Data encryption", 40, 0, 100, "%", 80));
        b.addDimension(securityB);

        Dimension reliabilityB = new Dimension("Reliability", 25);
        reliabilityB.addMetric(new HigherIsBetterMetric("Uptime", 50, 95, 100, "%", 97));
        reliabilityB.addMetric(new HigherIsBetterMetric("MTBF", 50, 0, 1000, "hours", 500));
        b.addDimension(reliabilityB);

        Dimension performanceB = new Dimension("Performance", 25);
        performanceB.addMetric(new LowerIsBetterMetric("Response time", 50, 0, 5, "sec", 2.5));
        performanceB.addMetric(new HigherIsBetterMetric("Throughput", 50, 0, 1000, "tps", 400));
        b.addDimension(performanceB);

        Dimension usabilityB = new Dimension("Usability", 20);
        usabilityB.addMetric(new HigherIsBetterMetric("Task completion rate", 50, 0, 100, "%", 70));
        usabilityB.addMetric(new LowerIsBetterMetric("Error rate", 50, 0, 20, "%", 8));
        b.addDimension(usabilityB);

        list.add(b);

        scenariosByMode.put(Mode.HEALTH, list);
    }

    // ======================= CUSTOM =======================
    // Custom mod bonus, şimdilik boş bir placeholder senaryo koyuyoruz
    // ileride kullanıcı buraya kendi dimension/metric'lerini ekleyebilir
    private void loadCustomScenarios() {
        List<Scenario> list = new ArrayList<>();
        Scenario empty = new Scenario(
                "Empty Scenario",
                "Define your own dimensions and metrics",
                Mode.CUSTOM);
        list.add(empty);
        scenariosByMode.put(Mode.CUSTOM, list);
    }
}
