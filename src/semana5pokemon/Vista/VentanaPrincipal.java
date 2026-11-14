package semana5pokemon.Vista;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.CardLayout;
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final String PANTALLA_INICIO = "INICIO";

    private final PanelInicio panelInicio;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);


    public VentanaPrincipal(){
        super("Batallas Pokémon");

        panelInicio = new PanelInicio(this);
        contenedor.add(panelInicio, PANTALLA_INICIO);
        this.add(contenedor);
        this.configurarVentana();
        mostrar(PANTALLA_INICIO);
    }
    
    private void configurarVentana() {
        setMinimumSize(new Dimension(900, 650));
        setSize(950, 700);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
       
    }
    

    private void mostrar(String pantalla) {
        cardLayout.show(contenedor, pantalla);
    }
}