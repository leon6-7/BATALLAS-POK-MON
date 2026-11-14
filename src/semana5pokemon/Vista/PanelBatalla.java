/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana5pokemon.Vista;
import semana5pokemon.Control.AccionBatalla;
import semana5pokemon.Control.Batalla;
import semana5pokemon.Control.EstadoBatalla;
import Excepciones.AccionInvalidaException;
import modelo.Catalogo;
import modelo.Entrenador;
import modelo.Pokemon;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Pantalla 3: la batalla.
 *
 * Es una vista "tonta": cada clic es un evento que llama a un método de Batalla,
 * y después el panel consulta el estado y se redibuja. Las reglas del combate
 * (orden, daño, reemplazos, fin de la batalla) viven en el paquete control.
 *
 * Los botones de acción se habilitan según Batalla.getAccionesValidas(), así que
 * quedan deshabilitados antes de comenzar, al elegir reemplazo y al terminar.
 */
public class PanelBatalla extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_AVISO = new Color(180, 30, 30);
    private static final Color AZUL_OSCURO = new Color(20, 30, 100);

    private final VentanaPrincipal ventana;
    private final Catalogo catalogo;

    private Batalla batalla;

    private final JLabel lblTurno = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
    private final PanelPokemonActivo panelJugador = new PanelPokemonActivo();
    private final PanelPokemonActivo panelRival = new PanelPokemonActivo();
    private final JTextArea areaMensajes = new JTextArea(8, 40);
    private final JLabel lblAviso = new JLabel(" ", SwingConstants.CENTER);

    private final JButton btnComenzar = new JButton("Comenzar batalla");
    private final JButton btnVerResultado = new JButton("Ver resultado  >>");
    private final Map<AccionBatalla, JButton> botonesAccion = new EnumMap<>(AccionBatalla.class);

    public PanelBatalla(VentanaPrincipal ventana, Catalogo catalogo) {
        this.ventana = ventana;
        this.catalogo = catalogo;

        construirInterfaz();
        registrarEventos();
    }

    // ------------------------------------------------------------------
    // Construcción de la interfaz
    // ------------------------------------------------------------------

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(construirEncabezado(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.add(construirCampo(), BorderLayout.NORTH);
        centro.add(construirMensajes(), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        add(construirControles(), BorderLayout.SOUTH);
    }

    private JPanel construirEncabezado() {
        lblTurno.setFont(lblTurno.getFont().deriveFont(Font.BOLD, 20f));
        lblTurno.setForeground(AZUL_OSCURO);
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.ITALIC, 13f));

        JPanel norte = new JPanel(new GridLayout(2, 1));
        norte.add(lblTurno);
        norte.add(lblEstado);
        return norte;
    }

    /** Los dos Pokémon activos enfrentados: jugador a la izquierda, rival a la derecha. */
    private JPanel construirCampo() {
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(vs.getFont().deriveFont(Font.BOLD, 28f));
        vs.setForeground(AZUL_OSCURO);

        JPanel campo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 0, 8);

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        campo.add(panelJugador, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        campo.add(vs, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1.0;
        campo.add(panelRival, gbc);

        return campo;
    }

    private JScrollPane construirMensajes() {
        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        areaMensajes.setMargin(new Insets(6, 8, 6, 8));

        JScrollPane scroll = new JScrollPane(areaMensajes);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro de la batalla"));
        return scroll;
    }

    private JPanel construirControles() {
        lblAviso.setFont(lblAviso.getFont().deriveFont(Font.BOLD, 13f));
        lblAviso.setForeground(COLOR_AVISO);
        lblAviso.setPreferredSize(new Dimension(0, 24));

        btnComenzar.setFont(btnComenzar.getFont().deriveFont(Font.BOLD, 14f));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        acciones.add(btnComenzar);
        for (AccionBatalla accion : AccionBatalla.values()) {
            JButton boton = new JButton(accion.getTexto());
            botonesAccion.put(accion, boton);
            acciones.add(boton);
        }
        acciones.add(btnVerResultado);

        JPanel sur = new JPanel(new GridLayout(2, 1, 0, 6));
        sur.add(lblAviso);
        sur.add(acciones);
        return sur;
    }

    private void registrarEventos() {
        btnComenzar.addActionListener(e -> comenzar());
        btnVerResultado.addActionListener(e -> ventana.irAResultado(batalla.getResultado()));
        for (Map.Entry<AccionBatalla, JButton> par : botonesAccion.entrySet()) {
            AccionBatalla accion = par.getKey();
            par.getValue().addActionListener(e -> ejecutarAccion(accion));
        }
    }

    public void iniciar(Entrenador entrenador) {
        try {
            batalla = new Batalla(entrenador, catalogo);
        } catch (AccionInvalidaException ex) {
            batalla = null;
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se puede iniciar la batalla", JOptionPane.WARNING_MESSAGE);
            SwingUtilities.invokeLater(() -> ventana.irAEquipo(entrenador));
            return;
        }

        panelJugador.setEntrenador("Entrenador: " + batalla.getNombreEntrenador());
        panelRival.setEntrenador("Rival: " + batalla.getNombreRival());
        areaMensajes.setText("Pulsa «Comenzar batalla» para iniciar.\n");
        limpiarAviso();
        actualizarVista();
    }

    private void comenzar() {
        try {
            batalla.comenzar();
            limpiarAviso();
        } catch (AccionInvalidaException ex) {
            mostrarAviso(ex.getMessage());
        }
        volcarMensajes();
        actualizarVista();
    }

    private void ejecutarAccion(AccionBatalla accion) {
        int indiceCambio = -1;
        if (accion == AccionBatalla.CAMBIAR) {
            Integer elegido = elegirPokemon("Cambiar de Pokémon",
                    "¿A qué Pokémon quieres cambiar?",
                    batalla.getIndicesCambioDisponibles(), false);
            if (elegido == null) {
                return;   
            }
            indiceCambio = elegido;
        }

        try {
            batalla.ejecutarRonda(accion, indiceCambio);
            limpiarAviso();
        } catch (AccionInvalidaException ex) {
            mostrarAviso(ex.getMessage());
            actualizarVista();
            return;
        }

        volcarMensajes();
        actualizarVista();

        if (batalla.getEstado() == EstadoBatalla.ELEGIR_REEMPLAZO) {
            SwingUtilities.invokeLater(this::pedirReemplazo);
        }
    }

    private void pedirReemplazo() {
        Integer elegido = elegirPokemon("Elegir reemplazo",
                "Tu Pokémon fue debilitado. ¿Quién entra al combate?",
                batalla.getIndicesCambioDisponibles(), true);
        try {
            batalla.elegirReemplazo(elegido);
            limpiarAviso();
        } catch (AccionInvalidaException ex) {
            mostrarAviso(ex.getMessage());
            SwingUtilities.invokeLater(this::pedirReemplazo);   // se vuelve a pedir
        }
        volcarMensajes();
        actualizarVista();
    }

    private void actualizarVista() {
        lblTurno.setText(batalla.getDescripcionTurno());
        lblEstado.setText("Estado: " + batalla.getEstado().getTexto());

        panelJugador.mostrar(batalla.getPokemonJugador());
        panelJugador.setRestantes(contarEnPie(batalla.getEquipoJugador()), batalla.getEquipoJugador().size());
        panelRival.mostrar(batalla.getPokemonRival());
        panelRival.setRestantes(contarEnPie(batalla.getEquipoRival()), batalla.getEquipoRival().size());

        List<AccionBatalla> validas = batalla.getAccionesValidas();
        for (Map.Entry<AccionBatalla, JButton> par : botonesAccion.entrySet()) {
            par.getValue().setEnabled(validas.contains(par.getKey()));
        }
        btnComenzar.setEnabled(batalla.getEstado() == EstadoBatalla.PREPARACION);
        btnVerResultado.setEnabled(batalla.haTerminado());
    }

    private int contarEnPie(List<Pokemon> equipo) {
        int enPie = 0;
        for (Pokemon pokemon : equipo) {
            if (!pokemon.estaDebilitado()) {
                enPie++;
            }
        }
        return enPie;
    }

    private void volcarMensajes() {
        for (String mensaje : batalla.consumirMensajesNuevos()) {
            areaMensajes.append(mensaje + "\n");
        }
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }

    private Integer elegirPokemon(String titulo, String mensaje, List<Integer> indices, boolean obligatorio) {
        if (indices.isEmpty()) {
            mostrarAviso("No hay otro Pokémon disponible.");
            return null;
        }

        List<Pokemon> equipo = batalla.getEquipoJugador();
        List<String> etiquetas = new ArrayList<>();
        for (int indice : indices) {
            Pokemon p = equipo.get(indice);
            etiquetas.add(p.getNombre() + " (" + p.getTipo().getNombre() + ")  "
                    + p.getPsActual() + "/" + p.getPsMax() + " PS");
        }
        if (!obligatorio) {
            etiquetas.add("Cancelar");
        }
        Object[] opciones = etiquetas.toArray();

        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(this, mensaje, titulo,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opciones, opciones[0]);
        } while (obligatorio && opcion == JOptionPane.CLOSED_OPTION);

        if (opcion < 0 || opcion >= indices.size()) {
            return null;
        }
        return indices.get(opcion);
    }

    private void mostrarAviso(String texto) {
        lblAviso.setText(texto);
    }

    private void limpiarAviso() {
        lblAviso.setText(" ");
    }
}