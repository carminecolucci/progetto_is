package farmacia;

import farmacia.boundary.LoginPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");  // Windows Look and feel
            // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");  // Windows Look and feel
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // Prova prova = new Prova();
        LoginPage loginPage = new LoginPage();
    }
}