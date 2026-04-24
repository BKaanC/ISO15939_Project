package model;

// Metric = ölçülen tek bir şey (ör: SUS score, Onboarding time)
// bu sınıf abstract çünkü skorun nasıl hesaplanacağı
// metriğin yönüne göre değişiyor (Higher/Lower is better)
// bu yüzden alt sınıflarda calculateScore metodu farklı olacak
public abstract class Metric {

    // temel alanlar
    protected String name;        // metriğin adı (ör: "SUS score")
    protected int coefficient;    // dimension içindeki ağırlığı (ör: 50)
    protected double minValue;    // geçerli aralığın alt sınırı
    protected double maxValue;    // geçerli aralığın üst sınırı
    protected String unit;        // birim (points, min, sec, %, users vb.)
    protected double value;       // toplanmış ham veri

    // constructor
    public Metric(String name, int coefficient, double minValue, double maxValue, String unit, double value) {
        this.name = name;
        this.coefficient = coefficient;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unit = unit;
        this.value = value;
    }

    // skor hesaplama alt sınıfta yapılacak
    // Higher is better ve Lower is better formülleri farklı
    public abstract double calculateScore();

    // tablo ekranında "Higher ↑" veya "Lower ↓" göstermek için
    public abstract String getDirectionLabel();

    // skoru 0.5'e yuvarlayan yardımcı metod
    // pdf'te "rounded to the nearest 0.5" yazıyor
    // ayrıca sonuç 1.0 - 5.0 aralığında olmalı (clamp)
    protected double roundToHalf(double score) {
        // önce 0.5'e yuvarla
        double rounded = Math.round(score * 2.0) / 2.0;
        // sonra sınırlarda tut
        if (rounded < 1.0) {
            rounded = 1.0;
        }
        if (rounded > 5.0) {
            rounded = 5.0;
        }
        return rounded;
    }

    // ---- getter / setter'lar ----

    public String getName() {
        return name;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // range'i ekranda "0–100" şeklinde göstermek için hazır bir metin
    public String getRangeLabel() {
        // tam sayı gibi görünüyorsa noktasız göster
        String minStr = (minValue == (int) minValue) ? String.valueOf((int) minValue) : String.valueOf(minValue);
        String maxStr = (maxValue == (int) maxValue) ? String.valueOf((int) maxValue) : String.valueOf(maxValue);
        return minStr + "–" + maxStr;
    }
}
