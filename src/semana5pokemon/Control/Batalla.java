
package semana5pokemon.Control;

import Excepciones.AccionInvalidaException;
import Excepciones.BatallaException;
import modelo.Catalogo;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Batalla {

    public static final int LIMITE_RONDAS = 50;
    public static final String NOMBRE_RIVAL = "Rival CPU";

    private final Entrenador entrenador;
    private final Equipo equipoJugador;
    private final Equipo equipoRival;
    private final Random random = new Random();
    private final List<String> mensajes = new ArrayList<>();

    private int indiceActivoJugador = 0;   // el primero de la lista sale primero
    private int indiceActivoRival = 0;
    private int ronda = 1;
    private int mensajesLeidos = 0;
    private EstadoBatalla estado = EstadoBatalla.PREPARACION;
    private ResultadoBatalla resultado;

    private static class AccionElegida {
        private final boolean esJugador;
        private final AccionBatalla accion;
        private final int indiceCambio;

        AccionElegida(boolean esJugador, AccionBatalla accion, int indiceCambio) {
            this.esJugador = esJugador;
            this.accion = accion;
            this.indiceCambio = indiceCambio;
        }
    }

    public Batalla(Entrenador entrenador, Catalogo catalogo) throws AccionInvalidaException {
        this.entrenador = entrenador;
        this.equipoJugador = entrenador.getEquipo();
        if (equipoJugador.getCantidad() < 1) {
            throw new AccionInvalidaException("Necesitas al menos un Pokémon para combatir.");
        }
        this.equipoRival = generarRival(catalogo, equipoJugador.getCantidad());
    }


    private Equipo generarRival(Catalogo catalogo, int cantidad) {
        List<Pokemon> disponibles = catalogo.obtenerTodos();
        Equipo rival = new Equipo();
        try {
            while (rival.getCantidad() < cantidad) {
                List<Pokemon> candidatos = new ArrayList<>();
                for (Pokemon pokemon : disponibles) {
                    if (copiasEn(rival, pokemon) < Equipo.MAX_COPIAS) {
                        candidatos.add(pokemon);
                    }
                }
                if (candidatos.isEmpty()) {
                    throw new IllegalStateException("El catálogo es muy pequeño para generar el rival.");
                }
                rival.agregar(candidatos.get(random.nextInt(candidatos.size())));
            }
        } catch (BatallaException ex) {
            throw new IllegalStateException("No se pudo generar el equipo rival: " + ex.getMessage(), ex);
        }
        return rival;
    }

    private int copiasEn(Equipo equipo, Pokemon modelo) {
        int copias = 0;
        for (Pokemon pokemon : equipo.getIntegrantes()) {
            if (pokemon.getNombre().equals(modelo.getNombre())) {
                copias++;
            }
        }
        return copias;
    }

    public void comenzar() throws AccionInvalidaException {
        if (estado != EstadoBatalla.PREPARACION) {
            throw new AccionInvalidaException("La batalla ya comenzó.");
        }
        estado = EstadoBatalla.ESPERANDO_ACCION;
        agregarMensaje("¡Comienza la batalla entre " + entrenador.getNombre() + " y " + NOMBRE_RIVAL + "!");
        agregarMensaje(entrenador.getNombre() + " envía a " + activo(true).getNombre() + ".");
        agregarMensaje(NOMBRE_RIVAL + " envía a " + activo(false).getNombre() + ".");
    }


    public void ejecutarRonda(AccionBatalla accion, int indiceCambio) throws AccionInvalidaException {
        if (estado != EstadoBatalla.ESPERANDO_ACCION) {
            throw new AccionInvalidaException("Ahora no es momento de elegir una acción.");
        }
        validarAccion(true, accion, indiceCambio);

        estado = EstadoBatalla.RESOLVIENDO_RONDA;
        agregarMensaje("--- Ronda " + ronda + " de " + LIMITE_RONDAS + " ---");

        AccionElegida delJugador = new AccionElegida(true, accion, indiceCambio);
        AccionElegida delRival = elegirAccionRival();

        resolver(delJugador, delRival);
        cerrarRonda();
    }

    public void elegirReemplazo(int indice) throws AccionInvalidaException {
        if (estado != EstadoBatalla.ELEGIR_REEMPLAZO) {
            throw new AccionInvalidaException("Ahora no corresponde elegir un reemplazo.");
        }
        validarCambio(true, indice);
        indiceActivoJugador = indice;
        agregarMensaje(entrenador.getNombre() + " envía a " + activo(true).getNombre() + ".");
        estado = EstadoBatalla.ESPERANDO_ACCION;
    }

    private void resolver(AccionElegida delJugador, AccionElegida delRival) {
        List<AccionElegida> restantes = new ArrayList<>();
        for (AccionElegida elegida : new AccionElegida[] {delJugador, delRival}) {
            if (elegida.accion.esPrioritaria()) {
                ejecutar(elegida);
            } else {
                restantes.add(elegida);
            }
        }

        if (restantes.size() == 2 && activo(false).getVelocidad() > activo(true).getVelocidad()) {
            Collections.swap(restantes, 0, 1);
        }
        for (AccionElegida elegida : restantes) {
            ejecutar(elegida);
        }
    }

    private void ejecutar(AccionElegida elegida) {
        boolean esJugador = elegida.esJugador;
        Pokemon actor = activo(esJugador);
        Pokemon objetivo = activo(!esJugador);
        String dueno = nombreDe(esJugador);

        if (actor.estaDebilitado()) {
            agregarMensaje(dueno + ": " + actor.getNombre()
                    + " está debilitado y pierde su acción en esta ronda.");
            return;
        }

        try {
            if (elegida.accion == AccionBatalla.CAMBIAR) {
                cambiarActivo(esJugador, elegida.indiceCambio);
                return;
            }

            boolean yaDebilitado = objetivo.estaDebilitado();
            String texto = switch (elegida.accion) {
                case ATACAR -> actor.atacar(objetivo);
                case HABILIDAD_ESPECIAL -> actor.usarEspecial(objetivo);
                case RECUPERAR -> actor.recuperar();
                case CAMBIAR -> throw new IllegalStateException("CAMBIAR se resuelve antes.");
            };
            agregarMensaje(dueno + ": " + texto);

            if (!yaDebilitado && objetivo.estaDebilitado()) {
                agregarMensaje("¡" + objetivo.getNombre() + " de " + nombreDe(!esJugador) + " se debilitó!");
            }
        } catch (AccionInvalidaException ex) {
            agregarMensaje(dueno + ": la acción no pudo realizarse (" + ex.getMessage() + ").");
        }
    }

    private void cerrarRonda() {
        if (!haySobrevivientes(false)) {
            terminar(EstadoBatalla.VICTORIA);
            return;
        }
        if (!haySobrevivientes(true)) {
            terminar(EstadoBatalla.DERROTA);
            return;
        }
        if (ronda >= LIMITE_RONDAS) {
            definirPorLimiteDeRondas();
            return;
        }
        ronda++;

        if (activo(false).estaDebilitado()) {
            indiceActivoRival = primerDisponible(false);
            agregarMensaje(NOMBRE_RIVAL + " envía a " + activo(false).getNombre() + ".");
        }

        if (activo(true).estaDebilitado()) {
            estado = EstadoBatalla.ELEGIR_REEMPLAZO;
            agregarMensaje("Elige el Pokémon que reemplazará a " + activo(true).getNombre() + ".");
        } else {
            estado = EstadoBatalla.ESPERANDO_ACCION;
        }
    }

    private void definirPorLimiteDeRondas() {
        int psJugador = equipoJugador.sumarPsRestantes();
        int psRival = equipoRival.sumarPsRestantes();
        agregarMensaje("Se alcanzaron " + LIMITE_RONDAS + " rondas sin un ganador.");
        agregarMensaje("PS restantes: " + entrenador.getNombre() + " " + psJugador
                + " - " + NOMBRE_RIVAL + " " + psRival + ".");

        if (psJugador > psRival) {
            terminar(EstadoBatalla.VICTORIA);
        } else if (psJugador < psRival) {
            terminar(EstadoBatalla.DERROTA);
        } else {
            terminar(EstadoBatalla.EMPATE);
        }
    }

    private void terminar(EstadoBatalla estadoFinal) {
        estado = estadoFinal;

        List<String> enPieJugador = pokemonEnPie(true);
        List<String> enPieRival = pokemonEnPie(false);
        resultado = new ResultadoBatalla(
                entrenador.getNombre(), NOMBRE_RIVAL, estadoFinal, ronda,
                equipoJugador.getCantidad() - enPieJugador.size(),
                equipoRival.getCantidad() - enPieRival.size(),
                enPieJugador, enPieRival);

        agregarMensaje(resultado.getMensajeFinal());
    }


    private void validarAccion(boolean esJugador, AccionBatalla accion, int indiceCambio)
            throws AccionInvalidaException {
        Pokemon activo = activo(esJugador);
        if (activo.estaDebilitado()) {
            throw new AccionInvalidaException(activo.getNombre() + " está debilitado y no puede actuar.");
        }
        switch (accion) {
            case ATACAR -> { }   // siempre es válida si el Pokémon puede actuar
            case HABILIDAD_ESPECIAL -> {
                if (!activo.puedeUsarEspecial()) {
                    throw new AccionInvalidaException(
                            activo.getNombre() + " no tiene usos restantes de su habilidad especial.");
                }
            }
            case RECUPERAR -> {
                if (!activo.puedeRecuperar()) {
                    throw new AccionInvalidaException(activo.getNombre()
                            + " no puede recuperar: tiene los PS completos o no le quedan usos.");
                }
            }
            case CAMBIAR -> validarCambio(esJugador, indiceCambio);
        }
    }

    private void validarCambio(boolean esJugador, int indice) throws AccionInvalidaException {
        List<Pokemon> equipo = pokemones(esJugador);
        if (indice < 0 || indice >= equipo.size()) {
            throw new AccionInvalidaException("Elige el Pokémon al que quieres cambiar.");
        }
        Pokemon candidato = equipo.get(indice);
        if (candidato.estaDebilitado()) {
            throw new AccionInvalidaException(candidato.getNombre() + " está debilitado y no puede combatir.");
        }
        if (indice == indiceActivo(esJugador)) {
            throw new AccionInvalidaException(candidato.getNombre() + " ya está en combate.");
        }
    }

    /** Versión sin excepciones de validarAccion, para saber qué acciones habilitar. */
    private boolean esAccionPosible(boolean esJugador, AccionBatalla accion) {
        Pokemon activo = activo(esJugador);
        if (activo.estaDebilitado()) {
            return false;
        }
        return switch (accion) {
            case ATACAR -> true;
            case HABILIDAD_ESPECIAL -> activo.puedeUsarEspecial();
            case RECUPERAR -> activo.puedeRecuperar();
            case CAMBIAR -> !indicesCambioPosibles(esJugador).isEmpty();
        };
    }

    private AccionElegida elegirAccionRival() {
        List<AccionBatalla> posibles = new ArrayList<>();
        for (AccionBatalla accion : AccionBatalla.values()) {
            if (esAccionPosible(false, accion)) {
                posibles.add(accion);
            }
        }
        AccionBatalla elegida = posibles.get(random.nextInt(posibles.size()));

        int indice = -1;
        if (elegida == AccionBatalla.CAMBIAR) {
            List<Integer> candidatos = indicesCambioPosibles(false);
            indice = candidatos.get(random.nextInt(candidatos.size()));
        }
        return new AccionElegida(false, elegida, indice);
    }

    public EstadoBatalla getEstado() {
        return estado;
    }

    public int getRonda() {
        return ronda;
    }

    public String getNombreEntrenador() {
        return entrenador.getNombre();
    }

    public String getNombreRival() {
        return NOMBRE_RIVAL;
    }

    public Pokemon getPokemonJugador() {
        return activo(true);
    }

    public Pokemon getPokemonRival() {
        return activo(false);
    }

    public int getIndiceActivoJugador() {
        return indiceActivoJugador;
    }

    public int getIndiceActivoRival() {
        return indiceActivoRival;
    }

    public List<Pokemon> getEquipoJugador() {
        return pokemones(true);
    }

    public List<Pokemon> getEquipoRival() {
        return pokemones(false);
    }

    public List<AccionBatalla> getAccionesValidas() {
        List<AccionBatalla> validas = new ArrayList<>();
        if (estado != EstadoBatalla.ESPERANDO_ACCION) {
            return validas;
        }
        for (AccionBatalla accion : AccionBatalla.values()) {
            if (esAccionPosible(true, accion)) {
                validas.add(accion);
            }
        }
        return validas;
    }


    public List<Integer> getIndicesCambioDisponibles() {
        if (estado != EstadoBatalla.ESPERANDO_ACCION && estado != EstadoBatalla.ELEGIR_REEMPLAZO) {
            return new ArrayList<>();
        }
        return indicesCambioPosibles(true);
    }

    public String getDescripcionTurno() {
        String ronda = "Ronda " + this.ronda + " de " + LIMITE_RONDAS;
        return switch (estado) {
            case PREPARACION -> "Preparación: pulsa «Comenzar batalla»";
            case ESPERANDO_ACCION -> ronda + " - Turno de " + entrenador.getNombre();
            case RESOLVIENDO_RONDA -> ronda + " - Resolviendo ronda...";
            case ELEGIR_REEMPLAZO -> ronda + " - " + entrenador.getNombre() + " debe elegir reemplazo";
            case VICTORIA, DERROTA, EMPATE -> "Batalla terminada: " + estado.getTexto();
        };
    }

    public boolean haTerminado() {
        return estado.estaTerminado();
    }

    public ResultadoBatalla getResultado() {
        return resultado;
    }

    public List<String> getMensajes() {
        return Collections.unmodifiableList(new ArrayList<>(mensajes));
    }

    public List<String> consumirMensajesNuevos() {
        List<String> nuevos = new ArrayList<>(mensajes.subList(mensajesLeidos, mensajes.size()));
        mensajesLeidos = mensajes.size();
        return nuevos;
    }


    private List<Pokemon> pokemones(boolean esJugador) {
        return (esJugador ? equipoJugador : equipoRival).getIntegrantes();
    }

    private int indiceActivo(boolean esJugador) {
        return esJugador ? indiceActivoJugador : indiceActivoRival;
    }

    private Pokemon activo(boolean esJugador) {
        return pokemones(esJugador).get(indiceActivo(esJugador));
    }

    private String nombreDe(boolean esJugador) {
        return esJugador ? entrenador.getNombre() : NOMBRE_RIVAL;
    }

    private void cambiarActivo(boolean esJugador, int indice) throws AccionInvalidaException {
        validarCambio(esJugador, indice);
        if (esJugador) {
            indiceActivoJugador = indice;
        } else {
            indiceActivoRival = indice;
        }
        agregarMensaje(nombreDe(esJugador) + " cambia a " + activo(esJugador).getNombre() + ".");
    }

    private List<Integer> indicesCambioPosibles(boolean esJugador) {
        List<Pokemon> equipo = pokemones(esJugador);
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < equipo.size(); i++) {
            if (!equipo.get(i).estaDebilitado() && i != indiceActivo(esJugador)) {
                indices.add(i);
            }
        }
        return indices;
    }

    private boolean haySobrevivientes(boolean esJugador) {
        return primerDisponible(esJugador) >= 0;
    }

    private int primerDisponible(boolean esJugador) {
        List<Pokemon> equipo = pokemones(esJugador);
        for (int i = 0; i < equipo.size(); i++) {
            if (!equipo.get(i).estaDebilitado()) {
                return i;
            }
        }
        return -1;
    }

    private List<String> pokemonEnPie(boolean esJugador) {
        List<String> enPie = new ArrayList<>();
        for (Pokemon pokemon : pokemones(esJugador)) {
            if (!pokemon.estaDebilitado()) {
                enPie.add(pokemon.getNombre() + " (" + pokemon.getPsActual()
                        + "/" + pokemon.getPsMax() + " PS)");
            }
        }
        return enPie;
    }

    private void agregarMensaje(String texto) {
        mensajes.add(texto);
    }
}