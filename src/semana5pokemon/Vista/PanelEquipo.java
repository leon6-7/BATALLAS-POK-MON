/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana5pokemon.Vista;

import Excepciones.EquipoLlenoException;
import Excepciones.EquipoNoGuardadoException;
import Excepciones.LimiteCopiasException;
import excepciones.SeleccionVaciaException;
import modelo.Catalogo;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.GestorEquipo;
 

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;

/**
 * Pantalla 2: selección y armado del equipo.
 *
 * Muestra dos listas (catálogo disponible y Mi equipo), la ficha del Pokémon
 * seleccionado y los botones Agregar, Quitar, Guardar equipo, Restaurar equipo
 * y Comenzar batalla.
 *
 * Toda la lógica de negocio (límites de 6 integrantes y 2 copias, clonación,
 * serialización en memoria) vive en el modelo. Este panel solo llama al modelo,
 * captura las excepciones y muestra el mensaje sin interrumpir la aplicación.
 */
public class PanelEquipo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_ERROR = new Color(180, 30, 30);
    private static final Color COLOR_OK = new Color(30, 130, 50);
    private static final Color AZUL_OSCURO = new Color(20, 30, 100);

    private final VentanaPrincipal ventana;

    /** Guarda en memoria (byte[]) la copia del equipo; vive mientras viva el panel. */
    private final GestorEquipo gestorEquipo = new GestorEquipo();

    private Entrenador entrenador;

    private final DefaultListModel<Pokemon> modeloCatalogo = new DefaultListModel<>();
    private final DefaultListModel<Pokemon> modeloEquipo = new DefaultListModel<>();
    private final JList<Pokemon> listaCatalogo = new JList<>(modeloCatalogo);
    private final JList<Pokemon> listaEquipo = new JList<>(modeloEquipo);

    private final JLabel lblEntrenador = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblCantidad = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblMensaje = new JLabel(" ", SwingConstants.CENTER);
    private final JTextArea txtFicha = new JTextArea();

    private final JButton btnAgregar = new JButton("Agregar  >>");
    private final JButton btnQuitar = new JButton("<<  Quitar");
    private final JButton btnGuardar = new JButton("Guardar equipo");
    private final JButton btnRestaurar = new JButton("Restaurar equipo");
    private final JButton btnComenzar = new JButton("Comenzar batalla");

    public PanelEquipo(VentanaPrincipal ventana, Catalogo catalogo) {
        this.ventana = ventana;

        for (Pokemon pokemon : catalogo.getPokemones()) {
            modeloCatalogo.addElement(pokemon);
        }

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
        add(construirListas(), BorderLayout.CENTER);
        add(construirFicha(), BorderLayout.EAST);
        add(construirPie(), BorderLayout.SOUTH);
    }

    private JPanel construirEncabezado() {
        JLabel titulo = new JLabel("Arma tu equipo", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 26f));
        titulo.setForeground(AZUL_OSCURO);

        lblEntrenador.setFont(lblEntrenador.getFont().deriveFont(Font.BOLD, 15f));

        JPanel norte = new JPanel(new GridLayout(2, 1));
        norte.add(titulo);
        norte.add(lblEntrenador);
        return norte;
    }

    private JPanel construirListas() {
        listaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCatalogo.setCellRenderer(new PokemonRenderer(false));
        listaEquipo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEquipo.setCellRenderer(new PokemonRenderer(true));

        JScrollPane scrollCatalogo = new JScrollPane(listaCatalogo);
        scrollCatalogo.setBorder(BorderFactory.createTitledBorder("Catálogo disponible"));

        lblCantidad.setFont(lblCantidad.getFont().deriveFont(Font.BOLD, 14f));
        JScrollPane scrollEquipo = new JScrollPane(listaEquipo);
        JPanel panelEquipo = new JPanel(new BorderLayout(0, 4));
        panelEquipo.setBorder(BorderFactory.createTitledBorder("Mi equipo"));
        panelEquipo.add(lblCantidad, BorderLayout.NORTH);
        panelEquipo.add(scrollEquipo, BorderLayout.CENTER);

        JPanel botonesCentro = new JPanel(new GridLayout(2, 1, 0, 10));
        botonesCentro.add(btnAgregar);
        botonesCentro.add(btnQuitar);

        JPanel listas = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 4, 0, 4);

        gbc.gridx = 0;
        gbc.weightx = 0.5;
        listas.add(scrollCatalogo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        listas.add(botonesCentro, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        listas.add(panelEquipo, gbc);

        return listas;
    }

    private JScrollPane construirFicha() {
        txtFicha.setEditable(false);
        txtFicha.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtFicha.setMargin(new Insets(8, 10, 8, 10));
        txtFicha.setLineWrap(true);
        txtFicha.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtFicha);
        scroll.setBorder(BorderFactory.createTitledBorder("Ficha del Pokémon"));
        scroll.setPreferredSize(new Dimension(250, 0));
        return scroll;
    }

    private JPanel construirPie() {
        lblMensaje.setFont(lblMensaje.getFont().deriveFont(Font.BOLD, 13f));
        lblMensaje.setPreferredSize(new Dimension(0, 24));

        btnComenzar.setFont(btnComenzar.getFont().deriveFont(Font.BOLD, 14f));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        botones.add(btnGuardar);
        botones.add(btnRestaurar);
        botones.add(btnComenzar);

        JPanel sur = new JPanel(new GridLayout(2, 1, 0, 6));
        sur.add(lblMensaje);
        sur.add(botones);
        return sur;
    }

    private void registrarEventos() {
        btnAgregar.addActionListener(e -> agregarSeleccionado());
        btnQuitar.addActionListener(e -> quitarSeleccionado());
        btnGuardar.addActionListener(e -> guardarEquipo());
        btnRestaurar.addActionListener(e -> restaurarEquipo());
        btnComenzar.addActionListener(e -> ventana.irABatalla());

        listaCatalogo.addListSelectionListener(e -> mostrarFichaSeleccion(e, listaCatalogo));
        listaEquipo.addListSelectionListener(e -> mostrarFichaSeleccion(e, listaEquipo));
    }

    // ------------------------------------------------------------------
    // API usada por VentanaPrincipal
    // ------------------------------------------------------------------

    /**
     * Prepara la pantalla para armar un equipo nuevo. Se llama tanto al llegar desde
     * Inicio como al elegir "Nueva partida". La copia guardada con "Guardar equipo"
     * se conserva entre partidas.
     */
    public void iniciar(Entrenador entrenador) {
        this.entrenador = entrenador;
        entrenador.getEquipo().vaciar();

        lblEntrenador.setText("Entrenador: " + entrenador.getNombre());
        listaCatalogo.clearSelection();
        txtFicha.setText("Selecciona un Pokémon para ver su ficha.");
        limpiarMensaje();
        actualizarEquipo();
    }

    // ------------------------------------------------------------------
    // Acciones: el modelo valida y lanza, la GUI captura y muestra el mensaje
    // ------------------------------------------------------------------

    private void agregarSeleccionado() {
        Pokemon seleccion = listaCatalogo.getSelectedValue();
        Equipo equipo = entrenador.getEquipo();
        try {
            equipo.agregarCopia(seleccion);   // el modelo clona al agregar
            actualizarEquipo();
            listaEquipo.setSelectedIndex(modeloEquipo.size() - 1);
            mostrarOk(seleccion.getNombre() + " se agregó a tu equipo.");
        } catch (SeleccionVaciaException | EquipoLlenoException | LimiteCopiasException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void quitarSeleccionado() {
        int indice = listaEquipo.getSelectedIndex();
        Equipo equipo = entrenador.getEquipo();
        try {
            Pokemon quitado = equipo.quitar(indice);
            actualizarEquipo();
            mostrarOk(quitado.getNombre() + " salió de tu equipo.");
        } catch (SeleccionVaciaException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void guardarEquipo() {
        try {
            gestorEquipo.guardar(entrenador);
            mostrarOk("Equipo guardado en memoria (" + entrenador.getEquipo().cantidad() + " Pokémon).");
        } catch (IOException ex) {
            mostrarError("No se pudo guardar el equipo.");
        }
    }

    private void restaurarEquipo() {
        try {
            Entrenador restaurado = gestorEquipo.restaurar();
            entrenador.setEquipo(restaurado.getEquipo());
            actualizarEquipo();
            mostrarOk("Equipo restaurado (" + entrenador.getEquipo().cantidad() + " Pokémon).");
        } catch (EquipoNoGuardadoException ex) {
            mostrarError(ex.getMessage());
        } catch (IOException | ClassNotFoundException ex) {
            mostrarError("No se pudo restaurar el equipo.");
        }
    }

    // ------------------------------------------------------------------
    // Actualización de la vista
    // ------------------------------------------------------------------

    /** Redibuja la lista del equipo, el contador y el estado del botón Comenzar batalla. */
    private void actualizarEquipo() {
        Equipo equipo = entrenador.getEquipo();

        modeloEquipo.clear();
        for (Pokemon pokemon : equipo.getPokemones()) {
            modeloEquipo.addElement(pokemon);
        }

        lblCantidad.setText("Equipo: " + equipo.cantidad() + "/" + Equipo.MAX_INTEGRANTES);
        btnComenzar.setEnabled(equipo.cantidad() >= 1);
    }

    private void mostrarFichaSeleccion(ListSelectionEvent e, JList<Pokemon> lista) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        Pokemon seleccionado = lista.getSelectedValue();
        if (seleccionado != null) {
            txtFicha.setText(construirFicha(seleccionado));
            txtFicha.setCaretPosition(0);
        }
    }

    private String construirFicha(Pokemon p) {
        return String.format(
                "Nombre:     %s%n"
              + "Tipo:       %s%n"
              + "Nivel:      %d%n"
              + "PS:         %d%n"
              + "Ataque:     %d%n"
              + "Defensa:    %d%n"
              + "Velocidad:  %d%n"
              + "Habilidad:  %s",
                p.getNombre(),
                p.getTipo().getNombre(),
                p.getNivel(),
                p.getPsMaximos(),
                p.getAtaque(),
                p.getDefensa(),
                p.getVelocidad(),
                p.getHabilidadEspecial().obtenerNombre());
    }

    private void mostrarError(String texto) {
        lblMensaje.setForeground(COLOR_ERROR);
        lblMensaje.setText(texto);
    }

    private void mostrarOk(String texto) {
        lblMensaje.setForeground(COLOR_OK);
        lblMensaje.setText(texto);
    }

    private void limpiarMensaje() {
        lblMensaje.setText(" ");
    }

    // ------------------------------------------------------------------
    // Renderer de las listas
    // ------------------------------------------------------------------

    /** Muestra "Nombre - Tipo - Nv. N". En Mi equipo numera y marca al primero en salir. */
    private static class PokemonRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        private final boolean numerar;

        PokemonRenderer(boolean numerar) {
            this.numerar = numerar;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Pokemon) {
                Pokemon p = (Pokemon) value;
                String prefijo = numerar ? (index + 1) + ".  " : "";
                String sufijo = (numerar && index == 0) ? "   (sale primero)" : "";
                setText(prefijo + p.getNombre() + "  -  " + p.getTipo().getNombre()
                        + "  -  Nv. " + p.getNivel() + sufijo);
            }
            return this;
        }
    }
}