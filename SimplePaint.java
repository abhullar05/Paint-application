import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *  *  Homework 12 - Challenge
 *
 * Create a GUI paint application.
 *
 *
 * @author advitbhullar, L-24
 *
 * @version November 17 ,2021
 */

public class SimplePaint extends JComponent implements Runnable {
    Image image; // the canvas
    Graphics2D graphics2D;  // this will enable drawing
    int curX; // current mouse x coordinate
    int curY; // current mouse y coordinate
    int oldX; // previous mouse x coordinate
    int oldY; // previous mouse y coordinate

    JButton rgbButton;
    JButton hexButton;
    JButton randomButton;
    JButton eraseButton;
    JButton fillButton;
    JButton clrButton;
    JTextField hexText;
    JTextField rText;
    JTextField gText;
    JTextField bText;

    SimplePaint paint; // variable of the type SimplePaint

    public SimplePaint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // set oldX and oldY coordinates to beginning mouse press
                oldX = e.getX();
                oldY = e.getY();
            }
        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // set current coordinates to where mouse is being dragged
                curX = e.getX();
                curY = e.getY();

                // draw the line between old coordinates and new ones
                graphics2D.setStroke(new BasicStroke(5));
                graphics2D.drawLine(oldX, oldY, curX, curY);

                // refresh frame and reset old coordinates
                repaint();
                oldX = curX;
                oldY = curY;

            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SimplePaint());
    }

    @Override
    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Simple Paint Challenge");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = new SimplePaint();
        content.add(paint, BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        clrButton = new JButton("Clear");
        fillButton = new JButton("Fill");
        eraseButton = new JButton("Erase");
        randomButton = new JButton("Random");
        hexButton = new JButton("Hex");
        rgbButton = new JButton("RGB");
        hexText = new JTextField(10);
        hexText.setText("#");
        rText = new JTextField(100);
        gText = new JTextField(5);
        bText = new JTextField(5);



        JPanel northPanel = new JPanel(new GridLayout(1, 4));
        northPanel.add(clrButton);
        northPanel.add(fillButton);
        northPanel.add(eraseButton);
        northPanel.add(randomButton);

        content.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel(new GridLayout(1, 6));
        southPanel.add(hexText);
        southPanel.add(hexButton);
        southPanel.add(rText);
        southPanel.add(gText);
        southPanel.add(bText);
        southPanel.add(rgbButton);

        content.add(southPanel, BorderLayout.SOUTH);

        clrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint.clrButtonPaint();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
        });
        fillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint.fillButtonPaint();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
        });
        eraseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = content.getBackground();
                paint.eraseButtonPaint(color);
            }
        });
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                Color randomColor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
                paint.randomButtonPaint(randomColor);
                hexText.setText(String.format("#%02x%02x%02x",
                        randomColor.getRed(),
                        randomColor.getGreen(),
                        randomColor.getBlue()));
                rText.setText(String.valueOf(randomColor.getRed()));
                gText.setText(String.valueOf(randomColor.getGreen()));
                bText.setText(String.valueOf(randomColor.getBlue()));

            }
        });
        hexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = null;
                try {
                    color = Color.decode(hexText.getText());
                    paint.hexButtonPaint(color);
                    rText.setText(String.valueOf(color.getRed()));
                    gText.setText(String.valueOf(color.getGreen()));
                    bText.setText(String.valueOf(color.getBlue()));
                } catch (NumberFormatException numberFormatException) {
                    JOptionPane.showMessageDialog(null, "Not a valid Hex Value", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        rgbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rText.getText().equals(""))
                    rText.setText("0");
                if (gText.getText().equals(""))
                    gText.setText("0");
                if (bText.getText().equals(""))
                    bText.setText("0");
                try {
                    Color color = new Color(Integer.parseInt(rText.getText()),
                            Integer.parseInt(gText.getText()),
                            Integer.parseInt(bText.getText()));
                    paint.rgbButtonPaint(color);
                    hexText.setText(String.format("#%02x%02x%02x", color.getRed() , color.getGreen(), color.getBlue()));
                    rText.setText(String.valueOf(color.getRed()));
                    gText.setText(String.valueOf(color.getGreen()));
                    bText.setText(String.valueOf(color.getBlue()));



                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        });




    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            // this lets us draw on the image (ie. the canvas)
            graphics2D = (Graphics2D) image.getGraphics();

            // gives us better rendering quality for the drawing lines
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // set canvas to white with default paint color
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clrButtonPaint() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void fillButtonPaint() {
        graphics2D.setPaint(graphics2D.getColor());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void eraseButtonPaint(Color color) {
        graphics2D.setPaint(color);
        repaint();
    }

    public void randomButtonPaint(Color randomColor) {
        graphics2D.setPaint(randomColor);
        repaint();
    }

    public void hexButtonPaint(Color color) {
        graphics2D.setPaint(color);
        repaint();
    }

    public void rgbButtonPaint(Color color) {
        graphics2D.setPaint(color);
        repaint();
    }


}
