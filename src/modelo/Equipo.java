
package modelo;

import Excepciones.EquipoLlenoException;
import Excepciones.LimiteCopiasException;
import Excepciones.SeleccionVaciaException ;
import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable {

    public static final int MAXIMO = 6;
    public static final int MAX_COPIAS = 2;

    private ArrayList<Pokemon> integrantes;

    public Equipo() {
        integrantes = new ArrayList<>();
    }

    // Armado del equipo

    public void agregar(Pokemon pokemon)
            throws SeleccionVaciaException, EquipoLlenoException, LimiteCopiasException {
        if (pokemon == null) {
            throw new SeleccionVaciaException("Selecciona un Pokémon del catálogo para agregarlo");
        }
        if (integrantes.size() >= MAXIMO) {
            throw new EquipoLlenoException("El equipo ya tiene " + MAXIMO + " Pokémon ");
        }
        if (contarCopias(pokemon.getNombre()) >= MAX_COPIAS) {
            throw new LimiteCopiasException("Solo puedes tener " + MAX_COPIAS + " copias de "
                    + pokemon.getNombre() + ".");
        }
        integrantes.add(pokemon);
    }

    public void quitar(int indice) throws SeleccionVaciaException {
        if (indice < 0 || indice >= integrantes.size()) {
            throw new SeleccionVaciaException("Selecciona un Pokémon de tu equipo para quitarlo ");
        }
        integrantes.remove(indice);
    }

    public int contarCopias(String nombre) {
        int copias = 0;
        for (Pokemon p : integrantes) {
            if (p.getNombre().equals(nombre)) {
                copias++;
            }
        }
        return copias;
    }

   

    
    public int sumarPsRestantes() {
        return sumarPsDesde(0);
    }

    private int sumarPsDesde(int indice) {
        if (indice >= integrantes.size()) {           // caso base
            return 0;
        }
        return integrantes.get(indice).getPsActual() 
                + sumarPsDesde(indice + 1);       
    }

    
    public int buscarSiguienteDisponible(int desde) {
        if (desde >= integrantes.size()) {            // caso base 1
            return -1;
        }
        if (integrantes.get(desde).estaDebilitado()) {
            return buscarSiguienteDisponible(desde + 1);
        }
        return desde;                                 // caso base 2
    }

    public boolean todosDebilitados() {
        return buscarSiguienteDisponible(0) == -1;
    }

    //Datos para el resultado 

    public int contarDebilitados() {
        int total = 0;
        for (Pokemon p : integrantes) {
            if (p.estaDebilitado()) {
                total++;
            }
        }
        return total;
    }

    public int contarEnPie() {
        return integrantes.size() - contarDebilitados();
    }

    

    
    public Equipo copiar() {
        Equipo copia = new Equipo();
        for (Pokemon p : integrantes) {
            copia.integrantes.add(p.clone());
        }
        return copia;
    }

    //Equipo rival
    public static Equipo generarRival(Catalogo catalogo, int cantidad) {
        Equipo rival = new Equipo();
        for (int i = 0; i < cantidad; i++) {
            rival.integrantes.add(catalogo.obtenerCopiaAleatoria());
        }
        return rival;
    }

    // Getters

    public Pokemon get(int indice) {
        return integrantes.get(indice);
    }

    public int getCantidad() {
        return integrantes.size();
    }

    public boolean estaVacio() {
        return integrantes.isEmpty();
    }

    public String getTextoCantidad() {
        return "Equipo: " + integrantes.size() + "/" + MAXIMO;
    }

    public ArrayList<Pokemon> getIntegrantes() {
        return new ArrayList<>(integrantes);
    }
}
