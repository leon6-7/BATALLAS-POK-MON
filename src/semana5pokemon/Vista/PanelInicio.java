/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana5pokemon.Vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;

public class PanelInicio extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color AZUL_OSCURO = new Color(20, 30, 100);
    private static final Color COLOR_ERROR = new Color(180, 30, 30);

    private static final String INSTRUCCIONES =
            "COMO JUGAR\n"
          + "Crea un equipo Pokemon y pelea contra un Cpu .\n\n"
          + "ANTES DE PELEAR\n"
          + " Debes elegir entre 1 a 6 pokemon del catalogo \n"
          + " Puedes repetir pokemon, pero solo 2 veces \n"
          + " (Guardar Equipo) guarda una copia en memoria\n\n"
          + "EN LA PELEA (cada ronda eliges una accion)\n"
          + "  Atacar: daño basico (potencia 10).\n"
          + "  Habilidad especial: potencia propia de cada tipo. 3 usos por Pokémon.\n"
          + "  Recuperar: restaura el 25 % de los PS máximos. 2 usos por Pokémon.\n"
          + "  Cambiar de Pokémon: siempre actúa primero en la ronda.\n"
          + "  Las demás acciones las ejecuta primero el Pokémon más veloz.\n\n"
          + "EFECTIVIDAD\n"
          + "  Fuego > Planta,  Agua > Fuego,  Planta > Agua,  Eléctrico > Agua.\n"
          + "  Se muestran: ×2: «¡Es muy eficaz!»   ×0,5: «No es muy eficaz…».\n\n"
          + "FIN DE LA BATALLA\n"
          + "  Gana quien conserve al menos un Pokemon con vida.\n"
          + "  Si se llega a 50 rondas, gana el equipo con mas vida restantes (o hay empate).";

    private final VentanaPrincipal ventana;

    private final JTextField txtNombre = new JTextField(14);
    private final JLabel lblMensaje = new JLabel(" ", SwingConstants.CENTER);
    private final JButton btnContinuar = new JButton("Continuar");
    private final JButton btnInstrucciones = new JButton("Instrucciones");
    private final JButton btnSalir = new JButton("Salir");

    public PanelInicio(VentanaPrincipal ventana) {
        this.ventana = ventana;
        construirInterfaz();
        registrarEventos();
    }

    private void construirInterfaz() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(6, 6, 6, 6);

        add(new Emblema(), gbc);

        JLabel titulo = new JLabel("BATALLAS POKÉMON", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 38f));
        titulo.setForeground(AZUL_OSCURO);
        gbc.gridy++;
        add(titulo, gbc);

        JLabel subtitulo = new JLabel("Por: Leonardo Romero y Josue Ponce ", SwingConstants.CENTER);
        subtitulo.setFont(subtitulo.getFont().deriveFont(Font.ITALIC, 15f));
        subtitulo.setForeground(Color.DARK_GRAY);
        gbc.gridy++;
        gbc.insets = new Insets(0, 6, 25, 6);
        add(subtitulo, gbc);

        JLabel lblNombre = new JLabel("Nombre del entrenador:", SwingConstants.CENTER);
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 15f));
        gbc.gridy++;
        gbc.insets = new Insets(6, 6, 2, 6);
        add(lblNombre, gbc);

        txtNombre.setFont(txtNombre.getFont().deriveFont(18f));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy++;
        gbc.insets = new Insets(2, 6, 2, 6);
        add(txtNombre, gbc);

        JLabel ayuda = new JLabel("De 3 a 10 caracteres", SwingConstants.CENTER);
        ayuda.setForeground(Color.GRAY);
        gbc.gridy++;
        add(ayuda, gbc);

        lblMensaje.setFont(lblMensaje.getFont().deriveFont(Font.BOLD, 13f));
        lblMensaje.setForeground(COLOR_ERROR);
        lblMensaje.setPreferredSize(new Dimension(520, 24));
        gbc.gridy++;
        gbc.insets = new Insets(8, 6, 8, 6);
        add(lblMensaje, gbc);

        btnContinuar.setFont(btnContinuar.getFont().deriveFont(Font.BOLD, 16f));
        btnContinuar.setPreferredSize(new Dimension(200, 40));
        gbc.gridy++;
        add(btnContinuar, gbc);

        JPanel secundarios = new JPanel();
        secundarios.setOpaque(false);
        secundarios.add(btnInstrucciones);
        secundarios.add(btnSalir);
        gbc.gridy++;
        gbc.insets = new Insets(4, 6, 6, 6);
        add(secundarios, gbc);
    }

    private void registrarEventos() {
        btnContinuar.addActionListener(e -> continuar());
        txtNombre.addActionListener(e -> continuar());         
        btnInstrucciones.addActionListener(e -> mostrarInstrucciones());
        

        txtNombre.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                limpiarMensaje();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                limpiarMensaje();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                limpiarMensaje();
            }
        });
    }

    private void continuar() {
        System.out.println("Continuo");
    }

    private void limpiarMensaje() {
        lblMensaje.setText(" ");
    }

    private void mostrarInstrucciones() {
        JTextArea area = new JTextArea(INSTRUCCIONES, 18, 52);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(8, 10, 8, 10));
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class Emblema extends JPanel {

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
}
