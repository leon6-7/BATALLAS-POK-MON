package modelo;

import Excepciones.AccionInvalidaException;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Pokemon implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public static final int NIVEL_INICIAL = 5;

    private String nombre;
    private TipoPokemon tipo;
    private int nivel;
    private int psMax;
    private int psActual;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int usosEspecial;
    private int usosRecuperacion;
    private String nombreEspecial;
    private int potenciaEspecial;
    private ArrayList<Habilidad> habilidades;
    public Pokemon(String nombre, TipoPokemon tipo, int psMax, int ataque, int defensa,
                   int velocidad, String nombreEspecial, int potenciaEspecial) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = NIVEL_INICIAL;
        this.psMax = psMax;
        this.psActual = psMax;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.usosEspecial = HabilidadEspecial.USOS_MAXIMOS;
        this.usosRecuperacion = Recuperacion.USOS_MAXIMOS;
        this.nombreEspecial = nombreEspecial;
        this.potenciaEspecial = potenciaEspecial;
        habilidades = new ArrayList<>();
        habilidades.add(new AtaqueBasico());
        habilidades.add(new HabilidadEspecial());
        habilidades.add(new Recuperacion());
    }

    protected abstract String efectoEspecial(Pokemon objetivo);

    public int calcularDanio(Pokemon objetivo, int potencia, double extra) {
        double multiplicador = tipo.getMultiplicador(objetivo.tipo);
        double base = ataque + nivel + potencia - objetivo.defensa / 2.0;
        int danio = (int) Math.round(base * multiplicador * extra);
        return Math.max(1, danio);
    }

    protected int golpear(Pokemon objetivo, int potencia, double extra) {
        int danio = calcularDanio(objetivo, potencia, extra);
        return objetivo.recibirDanio(danio);
    }

    protected String textoGolpe(Pokemon objetivo, int danio) {
    String efectividad = TipoPokemon.mensajeEfectividad(tipo.getMultiplicador(objetivo.tipo));
    String texto = "";
    if (efectividad.length() > 0) {
        texto = efectividad + " ";
    }
    texto += objetivo.nombre + " recibió " + danio + " de daño";
    if (objetivo.estaDebilitado()) {
        texto += " ¡" + objetivo.nombre + " fue debilitado!";
    }
    return texto;
   }

    public int recibirDanio(int danio) {
        int antes = psActual;
        psActual = Math.max(0, psActual - danio);
        return antes - psActual;
    }

    public int curar(int cantidad) {
        int antes = psActual;
        psActual = Math.min(psMax, psActual + cantidad);
        return psActual - antes;
    }

    public boolean estaDebilitado() {
        return psActual == 0;
    }

    public String atacar(Pokemon objetivo) throws AccionInvalidaException {
        return habilidades.get(0).ejecutar(this, objetivo);
    }

    public String usarEspecial(Pokemon objetivo) throws AccionInvalidaException {
        return habilidades.get(1).ejecutar(this, objetivo);
    }

    public String recuperar() throws AccionInvalidaException {
        return habilidades.get(2).ejecutar(this, null);
    }

         public boolean puedeUsarEspecial() {
            if (estaDebilitado()) {
                 return false;
            }
         return usosEspecial > 0;
        }

       public boolean puedeRecuperar() {
         if (estaDebilitado()) {
             return false;
          }
         return usosRecuperacion > 0 && psActual < psMax;
        }
    void consumirUsoEspecial() {
        usosEspecial--;
    }

    void consumirUsoRecuperacion() {
        usosRecuperacion--;
    }

    @Override
    public Pokemon clone() {
        try {
            Pokemon copia = (Pokemon) super.clone();
            copia.habilidades = new ArrayList<>(this.habilidades);
            return copia;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Pokemon implementa Cloneable", e);
        }
    }

    public String getFicha() {
        return "Nombre: " + nombre
                + "\nTipo: " + tipo
                + "\nNivel: " + nivel
                + "\nPS: " + psActual + "/" + psMax
                + "\nAtaque: " + ataque
                + "\nDefensa: " + defensa
                + "\nVelocidad: " + velocidad
                + "\nHabilidad especial: " + nombreEspecial + " (potencia " + potenciaEspecial + ")";
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }

    public HabilidadEspecial getHabilidadEspecial(){
        return (HabilidadEspecial) habilidades.get(1);
    }
    public String getNombre() {
        return nombre; }

    public TipoPokemon getTipo() {
        return tipo; }

    public int getNivel() {
        return nivel; }

    public int getPsMax() {
        return psMax; }

    public int getPsActual() {
        return psActual; }

    public int getAtaque() {
        return ataque; }

    public int getDefensa() {
        return defensa; }

    public int getVelocidad() {
        return velocidad; }

    public int getUsosEspecial() {
        return usosEspecial; }

    public int getUsosRecuperacion() {
        return usosRecuperacion; }

    public String getNombreEspecial() {
        return nombreEspecial; }

    public int getPotenciaEspecial() {
        return potenciaEspecial; }

    public ArrayList<Habilidad> getHabilidades() {
        return new ArrayList<>(habilidades); }
}
