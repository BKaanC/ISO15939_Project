package controller;

// view paneller bu arayüz üzerinden ileri/geri geçiş yapıyor
// WizardController bu arayüzü implemente ediyor
// böylece paneller somut controller sınıfına bağlı kalmıyor (loose coupling)
public interface Navigator {

    // bir sonraki adıma geç
    void goNext();

    // bir önceki adıma dön
    void goBack();

    // belirli bir adıma atla (0..4)
    void goTo(int stepIndex);
}
