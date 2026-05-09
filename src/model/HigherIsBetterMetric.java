package model;

// Higher is better, yani değer ne kadar yüksekse o kadar iyi
// SUS score, Uptime, WCAG compliance gibi metrikler
// formül pdfdeki: score = 1 + (value - min) / (max - min) * 4
public class HigherIsBetterMetric extends Metric {

    public HigherIsBetterMetric(String name, int coefficient,
                                double minValue, double maxValue,
                                String unit, double value) {
        // üst sınıfın constructor'ı zaten alanları set ediyor
        super(name, coefficient, minValue, maxValue, unit, value);
    }

    @Override
    public double calculateScore() {
        // max ile min aynıysa sıfıra bölme olur, ona karşı önlem
        if (maxValue == minValue) {
            return 1.0;
        }
        // formülü uyguladık
        double raw = 1 + ((value - minValue) / (maxValue - minValue)) * 4;
        // 0.5'e yuvarla ve 1-5 arasında tut
        return roundToHalf(raw);
    }

    @Override
    public String getDirectionLabel() {
        // tabloda gösterilen ok ile birlikte etiket
        return "Higher ↑";
    }
}
