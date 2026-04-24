package model;

// "Lower is better" yani değer ne kadar düşükse o kadar iyi
// örn: Onboarding time, MTTR, Response time gibi metrikler
// formül (pdf'ten): score = 5 - (value - min) / (max - min) * 4
public class LowerIsBetterMetric extends Metric {

    public LowerIsBetterMetric(String name, int coefficient,
                               double minValue, double maxValue,
                               String unit, double value) {
        super(name, coefficient, minValue, maxValue, unit, value);
    }

    @Override
    public double calculateScore() {
        // sıfıra bölmeyi engelle
        if (maxValue == minValue) {
            return 5.0;
        }
        // formülü uyguladık, düşük değer -> yüksek skor
        double raw = 5.0 - ((value - minValue) / (maxValue - minValue)) * 4.0;
        // 0.5'e yuvarla
        return roundToHalf(raw);
    }

    @Override
    public String getDirectionLabel() {
        return "Lower \u2193";
    }
}
