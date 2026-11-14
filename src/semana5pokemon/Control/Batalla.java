package semana5pokemon.Control;

import Excepciones.AccionInvalidaException;
import java.util.ArrayList;
import java.util.Random;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.Pokemon;

public class Batalla {

    public static final String NOMBRE_RIVAL = "Rival CPU";
    public static final int LIMITE_RONDAS = 50;

    private final Entrenador entrenador;
    private final Equipo equipoJugador;
    private final Equipo equipoRival;
    private final Random random;

    private int activoJugador;
    private int activoRival;
    private int ronda;
    private EstadoBatalla estado;

    public Batalla(Entrenador entrenador, Equipo equipoJugador, Equipo equipoRival) {
        this.entrenador = entrenador;
        this.equipoJugador = equipoJugador;
        this.equipoRival = equipoRival;
        this.random = new Random();
        this.activoJugador = 0;          
        this.activoRival = 0;
        this.ronda = 1;
        this.estado = EstadoBatalla.PREPARACION;
    }

    
    public String iniciar() {
        estado = EstadoBatalla.ESPERANDO_ACCION;
        return "¡Comienza la batalla! " + entrenador.getNombre() + " vs " + NOMBRE_RIVAL + ".\n"
                + entrenador.getNombre() + " envía a " + getPokemonJugador().getNombre() + ". "
                + NOMBRE_RIVAL + " envía a " + getPokemonRival().getNombre() + ".\n"
                + "--- Ronda " + ronda + ": elige tu acción ---\n";
    }

  

    public boolean esAccionValida(AccionBatalla accion) {
        if (estado == EstadoBatalla.ESPERANDO_ACCION) {
            return esValidaPara(accion, equipoJugador, activoJugador);
        }
        return false;
    }

    private boolean esValidaPara(AccionBatalla accion, Equipo equipo, int activo) {
        Pokemon p = equipo.get(activo);
        if (p.estaDebilitado()) {
            return false;
        }
        switch (accion) {
            case ATACAR:
                return true;
            case ESPECIAL:
                return p.puedeUsarEspecial();
            case RECUPERAR:
                return p.puedeRecuperar();
            case CAMBIAR:
                return hayOtroDisponible(equipo, activo);
            default:
                return false;
        }
    }

    private boolean hayOtroDisponible(Equipo equipo, int activo) {
        return opcionesDeCambio(equipo, activo).isEmpty() == false;
    }

    
    public ArrayList<Integer> getOpcionesCambio() {
        return opcionesDeCambio(equipoJugador, activoJugador);
    }

    private ArrayList<Integer> opcionesDeCambio(Equipo equipo, int activo) {
        ArrayList<Integer> opciones = new ArrayList<>();
        for (int i = 0; i < equipo.getCantidad(); i++) {
            if (i == activo || equipo.get(i).estaDebilitado()) {
                continue;
            }
            opciones.add(i);
        }
        return opciones;
    }

    private void validarCambio(Equipo equipo, int activo, int indice) throws AccionInvalidaException {
        if (indice < 0 || indice >= equipo.getCantidad()) {
            throw new AccionInvalidaException("Elige el Pokémon al que quieres cambiar.");
        }
        if (indice == activo) {
            throw new AccionInvalidaException(equipo.get(indice).getNombre() + " ya está en combate.");
        }
        if (equipo.get(indice).estaDebilitado()) {
            throw new AccionInvalidaException(equipo.get(indice).getNombre()
                    + " está debilitado y no puede combatir.");
        }
    }

    
    public String jugarRonda(AccionBatalla accionJugador, int indiceCambio) throws AccionInvalidaException {
        if (estado == EstadoBatalla.ESPERANDO_ACCION) {
            
        } else {
            throw new AccionInvalidaException("Ahora no puedes elegir una acción.");
        }
        if (esValidaPara(accionJugador, equipoJugador, activoJugador) == false) {
            throw new AccionInvalidaException(motivoInvalida(accionJugador));
        }
        if (accionJugador == AccionBatalla.CAMBIAR) {
            validarCambio(equipoJugador, activoJugador, indiceCambio);
        }

     
        AccionBatalla accionRival = elegirAccionRival();
        int cambioRival = -1;
        if (accionRival == AccionBatalla.CAMBIAR) {
            ArrayList<Integer> opciones = opcionesDeCambio(equipoRival, activoRival);
            cambioRival = opciones.get(random.nextInt(opciones.size()));
        }

        estado = EstadoBatalla.RESOLVIENDO_RONDA;
        String texto = "";

      
        boolean jugadorPrimero;
        if (accionJugador.tienePrioridad()) {
            jugadorPrimero = true;
        } else if (accionRival.tienePrioridad()) {
            jugadorPrimero = false;
        } else {
            jugadorPrimero = getPokemonJugador().getVelocidad() >= getPokemonRival().getVelocidad();
        }

        if (jugadorPrimero) {
            texto += turno(true, accionJugador, indiceCambio);
            texto += turno(false, accionRival, cambioRival);
        } else {
            texto += turno(false, accionRival, cambioRival);
            texto += turno(true, accionJugador, indiceCambio);
        }

    
        texto += cerrarRonda();
        return texto;
    }

   
    private String turno(boolean esJugador, AccionBatalla accion, int indiceCambio) {
        String dueno = NOMBRE_RIVAL;
        Pokemon usuario = getPokemonRival();
        Pokemon objetivo = getPokemonJugador();
        if (esJugador) {
            dueno = entrenador.getNombre();
            usuario = getPokemonJugador();
            objetivo = getPokemonRival();
        }

        if (usuario.estaDebilitado()) {
            return usuario.getNombre() + " está debilitado y pierde su acción.\n";
        }

        try {
            switch (accion) {
                case ATACAR:
                    return dueno + ": " + usuario.atacar(objetivo) + "\n";
                case ESPECIAL:
                    return dueno + ": " + usuario.usarEspecial(objetivo) + "\n";
                case RECUPERAR:
                    return dueno + ": " + usuario.recuperar() + "\n";
                case CAMBIAR:
                    return cambiar(esJugador, indiceCambio);
                default:
                    return "";
            }
        } catch (AccionInvalidaException e) {
            return dueno + ": " + e.getMessage() + "\n";
        }
    }

    private String cambiar(boolean esJugador, int indice) {
        if (esJugador) {
            String sale = getPokemonJugador().getNombre();
            activoJugador = indice;
            return entrenador.getNombre() + ": ¡" + sale + ", regresa! ¡Adelante, "
                    + getPokemonJugador().getNombre() + "!\n";
        }
        String sale = getPokemonRival().getNombre();
        activoRival = indice;
        return NOMBRE_RIVAL + " retira a " + sale + " y envía a " + getPokemonRival().getNombre() + ".\n";
    }

    private AccionBatalla elegirAccionRival() {
        ArrayList<AccionBatalla> validas = new ArrayList<>();
        for (AccionBatalla a : AccionBatalla.values()) {
            if (esValidaPara(a, equipoRival, activoRival)) {
                validas.add(a);
            }
        }
        return validas.get(random.nextInt(validas.size()));
    }

   
    private String cerrarRonda() {
        String texto = "";

       
        if (equipoJugador.todosDebilitados()) {
            estado = EstadoBatalla.DERROTA;
            return texto + "Todos los Pokémon de " + entrenador.getNombre() + " están debilitados.\n";
        }
        if (equipoRival.todosDebilitados()) {
            estado = EstadoBatalla.VICTORIA;
            return texto + "Todos los Pokémon de " + NOMBRE_RIVAL + " están debilitados.\n";
        }

       
        if (getPokemonRival().estaDebilitado()) {
            activoRival = equipoRival.buscarSiguienteDisponible(0);
            texto += NOMBRE_RIVAL + " envía a " + getPokemonRival().getNombre() + ".\n";
        }

     
        if (ronda >= LIMITE_RONDAS) {
            int psJugador = equipoJugador.sumarPsRestantes();
            int psRival = equipoRival.sumarPsRestantes();
            texto += "Se alcanzó el límite de " + LIMITE_RONDAS + " rondas. PS restantes: "
                    + psJugador + " vs " + psRival + ".\n";
            if (psJugador > psRival) {
                estado = EstadoBatalla.VICTORIA;
            } else if (psRival > psJugador) {
                estado = EstadoBatalla.DERROTA;
            } else {
                estado = EstadoBatalla.EMPATE;
            }
            return texto;
        }

        ronda++;

        
        if (getPokemonJugador().estaDebilitado()) {
            estado = EstadoBatalla.ELEGIR_REEMPLAZO;
            return texto + "Elige el Pokémon que entrará en lugar de "
                    + getPokemonJugador().getNombre() + ".\n";
        }

        estado = EstadoBatalla.ESPERANDO_ACCION;
        return texto + "--- Ronda " + ronda + ": elige tu acción ---\n";
    }

    
    public String elegirReemplazo(int indice) throws AccionInvalidaException {
        if (estado == EstadoBatalla.ELEGIR_REEMPLAZO) {
           
        } else {
            throw new AccionInvalidaException("Ahora no hay que elegir reemplazo.");
        }
        validarCambio(equipoJugador, activoJugador, indice);
        activoJugador = indice;
        estado = EstadoBatalla.ESPERANDO_ACCION;
        return entrenador.getNombre() + " envía a " + getPokemonJugador().getNombre() + ".\n"
                + "--- Ronda " + ronda + ": elige tu acción ---\n";
    }

    private String motivoInvalida(AccionBatalla accion) {
        Pokemon p = getPokemonJugador();
        if (p.estaDebilitado()) {
            return p.getNombre() + " está debilitado y no puede actuar.";
        }
        if (accion == AccionBatalla.ESPECIAL) {
            return p.getNombre() + " ya no tiene usos de " + p.getNombreEspecial() + ".";
        }
        if (accion == AccionBatalla.RECUPERAR) {
            if (p.getPsActual() == p.getPsMax()) {
                return p.getNombre() + " ya tiene los PS completos.";
            }
            return p.getNombre() + " ya no tiene usos de recuperación.";
        }
        if (accion == AccionBatalla.CAMBIAR) {
            return "No tienes otro Pokémon disponible para cambiar.";
        }
        return "Acción no válida.";
    }

    

    public boolean estaTerminada() {
        return estado.esFinal();
    }

    public ResultadoBatalla getResultado() {
        return new ResultadoBatalla(estado, entrenador.getNombre(), ronda, equipoJugador, equipoRival);
    }

    public Pokemon getPokemonJugador() { return equipoJugador.get(activoJugador); }
    public Pokemon getPokemonRival() { return equipoRival.get(activoRival); }
    public int getIndiceActivoJugador() { return activoJugador; }
    public Equipo getEquipoJugador() { return equipoJugador; }
    public Equipo getEquipoRival() { return equipoRival; }
    public String getNombreEntrenador() { return entrenador.getNombre(); }
    public int getRonda() { return ronda; }
    public EstadoBatalla getEstado() { return estado; }
}