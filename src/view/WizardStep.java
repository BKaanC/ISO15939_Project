package view;

// her wizard paneli bu arayüzü implemente ediyor
// controller panele geçiş yapılırken onShow()'u çağırıyor
// böylece panel ekrana gelmeden önce session'daki güncel veriye göre kendini güncelleyebiliyor
public interface WizardStep {

    // panel ekrana geldiğinde çağrılır — senaryoya göre içeriği yenilemek için
    void onShow();
}
