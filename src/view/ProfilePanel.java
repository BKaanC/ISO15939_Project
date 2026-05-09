package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Navigator;
import model.Session;

// Step 1 Profil
// kullanıcıdan 3 bilgi alıyoruz: username school session name
// next butonuna basıldığında boş alan varsa uyarı göstercez
public class ProfilePanel extends JPanel implements WizardStep {

    // session ve navigator dışardan geliyor
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
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // başlık
        JLabel title = new JLabel("Step 1: Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(25);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // School
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("School:"), gbc);
        schoolField = new JTextField(25);
        gbc.gridx = 1;
        add(schoolField, gbc);

        // Session Name
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Session Name:"), gbc);
        sessionNameField = new JTextField(25);
        gbc.gridx = 1;
        add(sessionNameField, gbc);

        // alt tarafta Next butonu
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setOpaque(false);

        JButton nextButton = new JButton("Next →");
        nextButton.setPreferredSize(new Dimension(110, 32));
        // TODO: ileride enter tuşuna basınca da next'e tıklamış sayılsın
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNext();
            }
        });
        buttonRow.add(nextButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonRow, gbc);
    }

    // next butonuna basıldığında çalışır
    private void onNext() {
        String username = usernameField.getText().trim();
        String school = schoolField.getText().trim();

        // ilk denemede tek mesaj gösteriyordum, sonra her alan için ayrı yaptım
        // if (username.isEmpty() || school.isEmpty() || sessionName.isEmpty()) {
        //     JOptionPane.showMessageDialog(this, "Please fill all fields");
        //     return;
        // }

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username to continue.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (school.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your school to continue.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sessionNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a session name to continue.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // alanlar dolu sessiona kaydet ve ilerle
        session.setUsername(username);
        session.setSchool(school);
        session.setSessionName(sessionNameField.getText().trim());

        navigator.goNext();
    }

    // panel ekrana geldiğinde session'da önceden yazılanları geri yükle
    @Override
    public void onShow() {
        if (session.getUsername() != null) usernameField.setText(session.getUsername());
        if (session.getSchool() != null) schoolField.setText(session.getSchool());
        if (session.getSessionName() != null) sessionNameField.setText(session.getSessionName());
    }
}
