package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Navigator;
import model.Session;

// Step 1 — Profile
// kullanıcıdan 3 bilgi alıyoruz: username, school, session name
// "Next"e basıldığında boş alan varsa uyarı gösteriyoruz
public class ProfilePanel extends JPanel implements WizardStep {

    // MVC gereği session ve navigator'ı dışarıdan alıyoruz
    private final Session session;
    private final Navigator navigator;

    // form alanları
    private JTextField usernameField;
    private JTextField schoolField;
    private JTextField sessionNameField;

    public ProfilePanel(Session session, Navigator navigator) {
        this.session = session;
        this.navigator = navigator;

        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        // etrafta biraz boşluk olsun
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        buildUi();
    }

    // tüm bileşenleri yerleştiriyoruz
    private void buildUi() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // ---- başlık ----
        JLabel title = new JLabel("Step 1: Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // küçük açıklama
        JLabel subtitle = new JLabel("Please enter your user and session information.");
        subtitle.setForeground(new Color(107, 114, 128));
        gbc.gridy = 1;
        add(subtitle, gbc);

        gbc.gridwidth = 1;

        // ---- Username ----
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(25);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // ---- School ----
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("School:"), gbc);

        schoolField = new JTextField(25);
        gbc.gridx = 1;
        add(schoolField, gbc);

        // ---- Session Name ----
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Session Name:"), gbc);

        sessionNameField = new JTextField(25);
        gbc.gridx = 1;
        add(sessionNameField, gbc);

        // ---- Next butonu (alt tarafta) ----
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setOpaque(false);

        JButton nextButton = new JButton("Next \u2192");
        nextButton.setPreferredSize(new Dimension(110, 32));
        nextButton.addActionListener(e -> onNext());
        buttonRow.add(nextButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonRow, gbc);
    }

    // Next'e tıklandığında çağrılıyor
    private void onNext() {
        // her alanı teker teker kontrol et, kullanıcıya hangi alan eksik dürüstçe söyle
        String username = usernameField.getText().trim();
        String school = schoolField.getText().trim();
        String sessionName = sessionNameField.getText().trim();

        if (username.isEmpty()) {
            showWarning("Please enter your username to continue.");
            usernameField.requestFocus();
            return;
        }
        if (school.isEmpty()) {
            showWarning("Please enter your school to continue.");
            schoolField.requestFocus();
            return;
        }
        if (sessionName.isEmpty()) {
            showWarning("Please enter a session name to continue.");
            sessionNameField.requestFocus();
            return;
        }

        // alanlar tamam, session'a yaz ve ilerle
        session.setUsername(username);
        session.setSchool(school);
        session.setSessionName(sessionName);

        navigator.goNext();
    }

    // uyarı mesajı göstermek için ortak metod
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Missing information", JOptionPane.WARNING_MESSAGE);
    }

    // panel ekrana geldiğinde çağıracağız (session'da daha önce yazılan varsa alanlara koy)
    @Override
    public void onShow() {
        if (session.getUsername() != null) usernameField.setText(session.getUsername());
        if (session.getSchool() != null) schoolField.setText(session.getSchool());
        if (session.getSessionName() != null) sessionNameField.setText(session.getSessionName());
    }
}
