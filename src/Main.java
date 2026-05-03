import javax.swing.SwingUtilities;

import view.MainFrame;

// uygulamanın giriş noktası
// Swing bileşenleri EDT (Event Dispatch Thread) üzerinde oluşturulmalı
// bu yüzden MainFrame'i invokeLater ile başlatıyoruz
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
