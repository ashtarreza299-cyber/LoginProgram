import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame{


    private Canvas canvas;

    public void setFrame(){

        setTitle("Login Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setComponents(){

        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);
    }


    public LoginView(){

        setFrame();

        setLayout(new BorderLayout());
        setComponents();
        setVisible(true);

    }


}
