package model;

import java.util.ArrayList;
import java.util.List;

// Dimension = kalite boyutu (ör: Usability, Reliability, Accessibility)
// her dimension'ın kendi adı, senaryo içindeki ağırlığı (coefficient)
// ve birden fazla metriği var
public class Dimension {

    private String name;           // ör: "Usability"
    private int coefficient;       // senaryo içindeki ağırlığı, ör: 25
    private List<Metric> metrics;  // bu dimension'a ait metrikler

    public Dimension(String name, int coefficient) {
        this.name = name;
        this.coefficient = coefficient;
        // ArrayList kullandık çünkü sıra önemli olabilir
        // (tabloda aynı sırada göstericez)
        this.metrics = new ArrayList<>();
    }

    // dimension'a yeni metrik eklemek için
    public void addMetric(Metric metric) {
        metrics.add(metric);
    }

    // pdf'teki formül:
    // dimensionScore = Σ(metricScore × metricCoefficient) / Σ(metricCoefficient)
    // yani ağırlıklı ortalama
    public double calculateDimensionScore() {
        double weightedSum = 0.0; // pay
        double totalWeight = 0.0; // payda

        for (Metric m : metrics) {
            double score = m.calculateScore();
            int weight = m.getCoefficient();
            weightedSum += score * weight;
            totalWeight += weight;
        }

        // hiç metrik yoksa sıfıra bölmeyi engelle
        if (totalWeight == 0.0) {
            return 0.0;
        }
        return weightedSum / totalWeight;
    }

    // ---- getter'lar ----

    public String getName() {
        return name;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    // başlıkta "Usability (Coefficient: 25)" gibi göstermek için
    public String getHeaderLabel() {
        return name + " (Coefficient: " + coefficient + ")";
    }
}
