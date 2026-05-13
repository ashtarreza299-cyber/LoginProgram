import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class Canvas extends JPanel {

    private int currentImageIndex = 0;
    private float alpha = 0.0f;
    private int panelWidth;
    private int panelHeight;
    private final int roundRectWidth = 400;
    private final int roundRectHeight = 600;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private MyButton submit;
    private MyButton cancel;

    private final String[] images = {

            "1 (1).jpg",
            "1 (3).jpg",
            "1 (4).jpg"
    };

    public Canvas() {

        panelWidth = getWidth();
        panelHeight = getHeight();

        setLayout(null);
        userNameLabel = new JLabel("User Name: ");// Use absolute positioning
        userNameField = new JTextField();
        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        submit = new MyButton("Submit");
        cancel = new MyButton("Cancel");

        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        updateUserNameFieldPositions();
        updateUserNameLabelPositions();
        updatePasswordLabelPositions();
        updatePasswordFieldPositions();
        updateSubmitButtonPosition();
        updateCancelButtonPosition();


        add(userNameLabel);
        add(userNameField);
        add(passwordLabel);
        add(passwordField);
        add(submit);
        add(cancel);

        changeImage();
        actionHandle();
    }

    public void actionHandle(){

        addComponentListener(new ComponentAdapter(){

            public void componentResized(ComponentEvent e) {

                panelWidth = getWidth();
                panelHeight = getHeight();

                updateUserNameLabelPositions();
                updateUserNameFieldPositions();
                updatePasswordLabelPositions();
                updatePasswordFieldPositions();
                updateSubmitButtonPosition();
                updateCancelButtonPosition();

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
            int cancelConfirm = JOptionPane.showConfirmDialog(Canvas.this, "Are you sure to cancel?");
            if(cancelConfirm == JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
    }

    private void changeImage(){

        Timer timer = new Timer(50, e -> {

            alpha += 0.02f;

            if (alpha >= 1f) {
                alpha = 0f;
                currentImageIndex = (currentImageIndex + 1) % images.length;
            }

            repaint();
        });

        timer.start();
        repaint();
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        //Image interpolation
        ImageInterpolation(g2);
        // Draw Overlay
        drawBlackOverlay(g2);
        //Draw Rectangle at the Center
        drawRoundedRectangleAndAvater(g2);


    }

    public void ImageInterpolation(Graphics2D g2){

        try{
            int nextImageIndex = (currentImageIndex + 1) % images.length;
            BufferedImage currentPhoto = ImageIO.read(new File(images[currentImageIndex]));
            BufferedImage nextPhoto = ImageIO.read(new File(images[nextImageIndex]));

            // Draw Current Photo
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - alpha));
            g2.drawImage(currentPhoto, 0, 0, panelWidth, panelHeight, null);

            // Draw Next Photo
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(nextPhoto, 0, 0, panelWidth, panelHeight, null);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void drawBlackOverlay(Graphics2D g2){

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, panelWidth, panelHeight);

    }

    public void drawRoundedRectangleAndAvater(Graphics2D g2){

        g2.setPaint(Color.WHITE);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        int rectWidth = 400;
        int rectHeight = 600;
        int roundRectX = (panelWidth - rectWidth) / 2;
        int roundRectY = (panelHeight - rectHeight) / 2;
        g2.setColor(new Color(217, 217, 217));
        g2.fillRoundRect(roundRectX, roundRectY, rectWidth, rectHeight, 50, 50);

        try {

            BufferedImage image = ImageIO.read(new File("avatar.png"));
            g2.drawImage(image, roundRectX + rectWidth/4, roundRectY, 200, 200, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateUserNameLabelPositions() {

       int roundRectX = (panelWidth - roundRectWidth) / 2;
       int roundRectY = (panelHeight - roundRectHeight) / 2;
       userNameLabel.setBounds(roundRectX + 50, roundRectY + 220, 300, 30);
    }

    private void updateUserNameFieldPositions() {

        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY = (panelHeight - roundRectHeight) / 2;
        userNameField.setBounds(roundRectX + 50, roundRectY + 250, 300, 30);
    }

    private void updatePasswordLabelPositions() {

        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY = (panelHeight - roundRectHeight) / 2;
        passwordLabel.setBounds(roundRectX + 50, roundRectY + 290, 300, 30);
    }

    private void updatePasswordFieldPositions() {

        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY = (panelHeight - roundRectHeight) / 2;
        passwordField.setBounds(roundRectX + 50, roundRectY + 320, 300, 30);
    }

    private void updateSubmitButtonPosition(){

        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY = (panelHeight - roundRectHeight) / 2;

        submit.setBounds(roundRectX + 50, roundRectY + 380, 300, 30);
    }

    private void updateCancelButtonPosition(){

        int roundRectX = (panelWidth - roundRectWidth) / 2;
        int roundRectY = (panelHeight - roundRectHeight) / 2;

        cancel.setBounds(roundRectX + 50, roundRectY + 430, 300, 30);
    }

}