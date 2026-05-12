
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MyButton extends AbstractButton {

    private boolean isHovered = false;
    private boolean isPressed = false;
    private final ArrayList<ActionListener> listeners = new ArrayList<>();
    private final String text;

    private Timer hoverTimer;
    private float animationProgress = 0f;

    public MyButton(String text){

        this.text = text;
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                isPressed = true;
                repaint();
            }

            public void mouseReleased(MouseEvent e){


                if(isPressed && contains(e.getPoint())){
                    fireActionEvent(null);
                }
                isPressed = false;
                repaint();
            }

            public void mouseEntered(MouseEvent e){

                isHovered = true;
                hoverTimer.start();
                repaint();
            }
            public void mouseExited(MouseEvent e){
                isHovered = false;
                hoverTimer.start();
                repaint();
            }
        });


        hoverTimer = new Timer(15, e -> {

            if(isHovered && animationProgress < 1f){
                animationProgress += 0.05f;
            }else if(!isHovered && animationProgress > 0f){
                animationProgress -= 0.05f;
            }
            animationProgress = Math.max(0f, Math.min(1f, animationProgress));
            repaint();
        });


    }

    public void addActionListener(ActionListener l){
        listeners.add(l);
    }

    private void fireActionEvent(ActionEvent e){

        ActionEvent event = new ActionEvent(
                this, ActionEvent.ACTION_PERFORMED, "Action Event"
        );

        for(ActionListener l : listeners){
            l.actionPerformed(event);
        }

    }

    public Dimension getPreferredSize(){
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Enable smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Base colors
        Color start = new Color(0, 200, 255);   // cyan-ish
        Color end = new Color(255, 200, 0);     // yellow-ish

        // Interpolate colors
        float t = 1 - (1 - animationProgress) * (1 - animationProgress);  // ease out

        int r = (int) (start.getRed() + t * (end.getRed() - start.getRed()));
        int gr = (int) (start.getGreen() + t * (end.getGreen() - start.getGreen()));
        int b = (int) (start.getBlue() + t * (end.getBlue() - start.getBlue()));

        Color blended = new Color(r, gr, b);

        // Press effect (slight darkening)
        if (isPressed) {
            blended = blended.darker();
        }

        g2.setColor(blended);
        g2.fillRoundRect(0, 0, width, height, 20, 20);

        // Text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(text)) / 2;
        int textY = (height + fm.getAscent()) / 2 - 4;

        g2.drawString(text, textX, textY);

        g2.dispose();
    }

    public String getText(){
        return  text;
    }
}
