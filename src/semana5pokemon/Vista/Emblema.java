package semana5pokemon.Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

    class Emblema extends JPanel {
    private static final Color AZUL_OSCURO = new Color(20, 30, 100);

        private static final long serialVersionUID = 1L;

        Emblema() {
            setPreferredSize(new Dimension(120, 120));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int d = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - d) / 2;
                int y = (getHeight() - d) / 2;

                g2.setColor(new Color(215, 40, 40));
                g2.fill(new Arc2D.Double(x, y, d, d, 0, 180, Arc2D.PIE));
                g2.setColor(Color.WHITE);
                g2.fill(new Arc2D.Double(x, y, d, d, 180, 180, Arc2D.PIE));

                g2.setColor(AZUL_OSCURO);
                g2.setStroke(new java.awt.BasicStroke(5f));
                g2.draw(new Ellipse2D.Double(x, y, d, d));
                g2.drawLine(x, y + d / 2, x + d, y + d / 2);

                int r = d / 4;
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Double(x + d / 2.0 - r / 2.0, y + d / 2.0 - r / 2.0, r, r));
                g2.setColor(AZUL_OSCURO);
                g2.draw(new Ellipse2D.Double(x + d / 2.0 - r / 2.0, y + d / 2.0 - r / 2.0, r, r));
            } finally {
                g2.dispose();
            }
        }
    }
