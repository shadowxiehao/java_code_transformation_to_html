package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.Iterator;

class Pond extends JPanel {
    private BufferedImage bimg;
    private Vector turtles;     // must be atomic.

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private Rectangle clipRegion;

    public Pond() {
        bimg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics pen = bimg.createGraphics();
        pen.setColor(Color.white);
        pen.fillRect(0, 0, WIDTH, HEIGHT);
        clipRegion = null;
        turtles = new Vector();

        JFrame app = new JFrame("Turtle");
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JMenu speed = new JMenu("Speed");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (cmd.equals("extreme"))
                    Turtle.interval = 0;
                else if (cmd.equals("very fast"))
                    Turtle.interval = Turtle.SLOW_INTERVAL / 10;
                else if (cmd.equals("fast"))
                    Turtle.interval = Turtle.SLOW_INTERVAL / 2;
                else
                    Turtle.interval = Turtle.SLOW_INTERVAL;
            }
        };

        speed.add("slow").addActionListener(listener);
        speed.add("fast").addActionListener(listener);
        speed.add("very fast").addActionListener(listener);
        speed.add("extreme").addActionListener(listener);

        JMenuBar bar = new JMenuBar();
        bar.add(speed);
        app.setJMenuBar(bar);
        app.setSize(WIDTH, HEIGHT);
        app.setResizable(false);
        app.getContentPane().add(this);
        app.setVisible(true);
    }

    public void addTurtle(Turtle t) {
        turtles.add(t);
    }

    public Graphics makePen() {
        return bimg.createGraphics();
    }

    public synchronized void setClip(int x0, int y0, int margin) {
        Rectangle r = new Rectangle(x0 - margin, y0 - margin,
                margin * 2, margin * 2);
        if (clipRegion == null)
            clipRegion = r;
        else
            clipRegion.add(r);
    }

    public void partlyRepaint() {
        Rectangle r = clipRegion;
        clipRegion = null;
        if (r != null)
            repaint(r);
        else
            repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bimg, 0, 0, this);
        Iterator i = turtles.iterator();
        while (i.hasNext()) {
            Turtle t = (Turtle)i.next();
            t.paint(g);
        }
    }
}

public class Turtle {
    static final int TURTLE_SIZE = 5;
    static final int SLOW_INTERVAL = 100;
    static int interval = SLOW_INTERVAL;

    private static final double STRIDE = 5.0;
    private static Pond pond = null;

    private Graphics pen;
    private double xpos, ypos;
    private int direction;
    private boolean penIsDown;
    private Color penColor;

    private static synchronized void makePond() {
        if (pond == null)
            pond = new Pond();
    }

    public Turtle() {
        makePond();
        pond.addTurtle(this);
        pen = pond.makePen();
        xpos = ypos = 0.0;
        direction = 0;
        penIsDown = false;
        penColor = Color.blue;
    }

    private void repaint() {
        pond.partlyRepaint();
    }

    void paint(Graphics g) {
        g.setColor(Color.black);
        g.drawOval((int)xpos - TURTLE_SIZE, (int)ypos - TURTLE_SIZE,
                TURTLE_SIZE * 2, TURTLE_SIZE * 2);
    }

    public void penDown() { penIsDown = true; }

    public void penUp() { penIsDown = false; }

    public boolean isDown() { return penIsDown; }

    public void setColor(Color c) { penColor = c; }

    public void setDirection(double d) { direction = (int)d; }

    public void setDirection(int d) { direction = d; }

    public synchronized void rotate(int r) {
        direction = (direction + r) % 360;
    }

    public void rotate(double r) { rotate((int)r); }

    public void go(int distance) {
        go((double)distance);
    }

    public void go(double distance) {
        double x = xpos;
        double y = ypos;
        if (direction == 0)
            x += distance;
        else if (direction == 90)
            y -= distance;
        else if (direction == 180)
            x -= distance;
        else if (direction == 270)
            y += distance;
        else {
            double r = direction * Math.PI * 2.0 / 360.0;
            x = xpos + distance * Math.cos(r);
            y = ypos - distance * Math.sin(r);
        }

        move(x, y);
    }

    public void move(int x, int y) {
        move((double)x, (double)y);
    }

    public synchronized void move(double x, double y) {
        if (penIsDown) {
            pen.setColor(penColor);
            double x2 = xpos - x;
            double y2 = ypos - y;
            int step;
            if (interval > 0) {
                step = (int)(Math.sqrt(x2 * x2 + y2 * y2) / STRIDE);
                if (step < 2)
                    step = 2;
            }
            else
                step = 1;

            for (int i = step - 1; i >= 0; --i) {
                double xx = x + x2 * i / step;
                double yy = y + y2 * i / step;
                pond.setClip((int)xpos, (int)ypos, TURTLE_SIZE + 2);
                pond.setClip((int)xx, (int)yy, TURTLE_SIZE + 2);
                pen.drawLine((int)xpos, (int)ypos, (int)xx, (int)yy);
                xpos = xx;
                ypos = yy;
                repaint();
                if (i > 0 && interval > 0)
                    try {
                        Thread.sleep(interval);
                    }
                    catch (InterruptedException e) {}
            }
        }
        else
        if (interval > 0)
            try {
                Thread.sleep(interval / 2);
            }
            catch (InterruptedException e) {}

        pond.setClip((int)xpos, (int)ypos, TURTLE_SIZE + 2);
        xpos = x;
        ypos = y;
        pond.setClip((int)x, (int)y, TURTLE_SIZE + 2);
        repaint();
    }

    public synchronized void print(String text) {
        pen.setColor(penColor);
        pen.drawString(text, (int)xpos, (int)ypos);
        repaint();
    }
}
