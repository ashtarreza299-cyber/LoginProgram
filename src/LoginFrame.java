import javax.swing.*;
import java.io.IOException;

public class LoginFrame extends JFrame {

    public LoginFrame () throws IOException {

        setUpFrame();
        LoginView view = new LoginView();
        add(view);
        setVisible(true);
    }

    private void setUpFrame() {

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

}

