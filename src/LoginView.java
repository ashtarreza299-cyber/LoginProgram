
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class LoginView extends JPanel {

    private int panelWidth;
    private int panelHeight;
    private int currentPhotoIndex = 0;
    private float alpha = 0.0f;
    private int roundRectWidth = 400;
    private int roundRectHeight = 600;
    private float alphaLink = 1.0f;
    private boolean isGoogle = false;

    private JTextField userNameField;
    private JPasswordField passwordField;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel orLabel;
    private JButton googleButton;
    private JButton faceBookButton;

    private MyButton submit;
    private MyButton cancel;
    private BufferedImage[] backgroundImags;
    private BufferedImage avatar;
    private BufferedImage googleLogo = ImageIO.read(new File("google.png"));
    private BufferedImage faceBookLogo = ImageIO.read(new File("facebook.png"));
    private Timer timer;

    private String[] images = {

            "1 (1).jpg",
            "1 (3).jpg",
            "1 (4).jpg",
    };

    public LoginView() throws IOException {


        setLayout(null);
        setUpUI();
        loadImage();
        changeImage();
        actionHandle();

    }

    private void setUIComponents() {

        userNameLabel = new JLabel("User Name: ");// Use absolute positioning
        userNameField = new JTextField();
        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        submit = new MyButton("Submit");
        cancel = new MyButton("Cancel");
        orLabel = new JLabel("Oe Login by");
        googleButton = new JButton(makeTransparent(googleLogo));
        faceBookButton = new JButton(makeTransparent(faceBookLogo));

        googleButton.setBorder(null);
        googleButton.setOpaque(false);
        faceBookButton.setBorder(null);
        faceBookButton.setOpaque(false);

        orLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

    }

    private void setUIComponenetsPosition(){

        positionObjects(userNameLabel, 50, 220, 300, 30);
        positionObjects(userNameField, 50, 250, 300, 30);
        positionObjects(passwordLabel, 50, 290, 300, 30);
        positionObjects(passwordField, 50, 320, 300, 30);

        positionObjects(submit, 50, 380, 300, 30);
        positionObjects(cancel, 50, 430, 300, 30);

        positionObjects(orLabel, 50, 470, 300, 30);
        positionObjects(googleButton, 100, 510, googleLogo.getWidth(), googleLogo.getHeight());
        positionObjects(faceBookButton, 250, 510, faceBookLogo.getWidth(), faceBookLogo.getHeight());

    }

    private void addUIComponentsToCanvas() {

        add(userNameLabel);
        add(userNameField);
        add(passwordLabel);
        add(passwordField);
        add(submit);
        add(cancel);
        add(orLabel);
        add(googleButton);
        add(faceBookButton);
    }

    private void setUpUI() {

        setUIComponents();
        setUIComponenetsPosition();
        addUIComponentsToCanvas();
    }

    private void actionHandle(){

        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e){

                panelWidth = getWidth();
                panelHeight = getHeight();

                positionObjects(userNameLabel, 50, 220, 300, 30);
                positionObjects(userNameField, 50, 250, 300, 30);
                positionObjects(passwordLabel, 50, 290, 300, 30);
                positionObjects(passwordField, 50, 320, 300, 30);

                positionObjects(submit, 50, 380, 300, 30);
                positionObjects(cancel, 50, 430, 300, 30);

                positionObjects(orLabel, 50, 470, 300, 30);
                positionObjects(googleButton, 100, 510, googleLogo.getWidth(), googleLogo.getHeight());
                positionObjects(faceBookButton, 250, 510, faceBookLogo.getWidth(), faceBookLogo.getHeight());

                repaint();
            }
        });

        submit.addActionListener(e -> {

            String userName = userNameField.getText().trim();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars).trim();

            if(userName.isEmpty() || password.isEmpty()){
                System.out.println("Please fill all the fields");
            }else{

                File file = new File("userinfo.txt");

                if(!file.exists() || file.length() == 0){

                    try {
                        PrintWriter writer = new PrintWriter(file);
                        writer.println(userName);
                        writer.println(password);
                        JOptionPane.showMessageDialog(
                                null, "You are configured as a new user! Please login again with the same credentials.",
                                "Success", JOptionPane.INFORMATION_MESSAGE
                        );
                        writer.close();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }else{

                    try {
                        Scanner input = new Scanner(file);
                        String storeduserName = input.next();
                        String storedpassword = input.next();

                        if(storeduserName.equals(userName) && storedpassword.equals(password)){
                            JOptionPane.showMessageDialog(null, "You have successfully logged in!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }


        });

        cancel.addActionListener(e -> {
            int cancelConfirm = JOptionPane.showConfirmDialog(LoginView.this, "Are you sure to cancel?");
            if(cancelConfirm == JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });

        googleButton.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {

                setCursor(new Cursor(Cursor.HAND_CURSOR));
                isGoogle = true;
                startAnimation(0.5f, isGoogle);
            }

            public void mouseExited(MouseEvent e) {

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isGoogle = true;
                startAnimation(1.0f, isGoogle);
            }
        });

        faceBookButton.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {

                setCursor(new Cursor(Cursor.HAND_CURSOR));
                isGoogle = false;
                startAnimation(0.5f, isGoogle);
            }

            public void mouseExited(MouseEvent e) {

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isGoogle = false;
                startAnimation(1.0f, isGoogle);
            }
        });

    }

    public void startAnimation(float target, boolean isGoogle){

        if(timer != null && timer.isRunning()){
            timer.stop();
        }

        timer = new Timer(20,e -> {

            float speed = 0.05f;

            // fade out
            if(alphaLink > target) {

                alphaLink -= speed;
                if(alphaLink < target)
                    alphaLink = target;
            }

            // fade in
            else if(alphaLink < target) {

                alphaLink += speed;
                if(alphaLink > target)
                    alphaLink = target;
            }

            if(isGoogle)
                googleButton.setIcon(makeTransparent(googleLogo));
            else
                faceBookButton.setIcon(makeTransparent(faceBookLogo));

            if(alphaLink == target) {
                timer.stop();
            }
        });

        timer.start();
    }

    public ImageIcon makeTransparent(BufferedImage logo) {

        BufferedImage image = new BufferedImage(
                googleLogo.getWidth(),
                googleLogo.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaLink));
        g2.drawImage(logo, 0, 0, null); // draw logo at (0,0)
        g2.dispose();

        return new ImageIcon(image);
    }

    private void changeImage() {

        Timer timer = new Timer(30, e ->{

            alpha += 0.02f;
            if(alpha >= 1.0f) {
                alpha = 0.0f;
                currentPhotoIndex = (currentPhotoIndex + 1) % images.length;
            }

            repaint();
        });

        timer.start();
    }

    private void loadImage() throws IOException {

        backgroundImags = new BufferedImage[images.length];

        for(int i = 0; i < images.length; i++){
            backgroundImags[i] = ImageIO.read(new File(images[i]));

        }
        avatar = ImageIO.read(new File("avatar.png"));
    }

    private void ImageInterpolation(Graphics2D g2) {

        int nextPhotoIndex = (currentPhotoIndex + 1) % images.length;

        BufferedImage currentPhoto = backgroundImags[currentPhotoIndex];
        BufferedImage nextPhoto = backgroundImags[nextPhotoIndex];

        if(currentPhoto != null){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - alpha));
            g2.drawImage(currentPhoto, 0, 0, panelWidth, panelHeight, this);
        }

        if(nextPhoto != null){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(nextPhoto, 0, 0, panelWidth, panelHeight, this);
        }


    }

    private void blackOverlay(Graphics2D g2) {

        g2.setColor(Color.black);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2.fillRect(0, 0, panelWidth, panelHeight);
    }

    private void drawRoundRect(Graphics2D g2) {

        g2.setColor(new Color(243, 240, 240));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY =  (panelHeight - roundRectHeight) / 2;
        g2.fillRoundRect(roundRectX, roundRectY, roundRectWidth, roundRectHeight, 50, 50);
    }

    private void drawAvatarImage(Graphics2D g2) {

        int x = (panelWidth - roundRectWidth) / 2;
        int y = (panelHeight - roundRectHeight) / 2;
        g2.drawImage(avatar, x + roundRectWidth/4, y, 200, 200, null);
    }

    private void positionObjects(JComponent comp, int offexX, int offexY, int width, int height) {

        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY = (panelHeight - roundRectHeight) / 2;

        comp.setBounds(roundRectX + offexX, roundRectY + offexY, width, height);
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ImageInterpolation(g2);
        blackOverlay(g2);
        drawRoundRect(g2);
        drawAvatarImage(g2);

    }

}