import javax.swing.SwingUtilities;

import view.MainFrame;

// uygulamanın giriş noktası
// swing pencereleri özel bir threadde açılmalı
// bu yüzden MainFrame'i invokelater ile başlatıyoruz
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
