

package modelo;

import java.util.ArrayList;
import java.util.Random;

public class Catalogo {

    private ArrayList<Pokemon> originales;
    private Random random;

    public Catalogo() {
        originales = new ArrayList<>();
        random = new Random();
        cargarPokemon();
    }

    // 12 Pokémon por defecto 
    private void cargarPokemon() {
        //                                  nombre        PS  Atq Def Vel
        originales.add(new PokemonFuego    ("Charmander",  90, 14,  8, 11));
        originales.add(new PokemonFuego    ("Vulpix",      85, 13,  9, 12));
        originales.add(new PokemonFuego    ("Growlithe",   95, 14,  9, 10));
        originales.add(new PokemonAgua     ("Squirtle",   100, 11, 12,  9));
        originales.add(new PokemonAgua     ("Psyduck",     95, 12,  9, 10));
        originales.add(new PokemonAgua     ("Poliwag",     85, 11, 10, 13));
        originales.add(new PokemonPlanta   ("Bulbasaur",  100, 12, 10,  8));
        originales.add(new PokemonPlanta   ("Oddish",      90, 13,  8,  7));
        originales.add(new PokemonPlanta   ("Chikorita",   95, 11, 12,  8));
        originales.add(new PokemonElectrico("Pikachu",     80, 14,  7, 15));
        originales.add(new PokemonElectrico("Voltorb",     75, 12,  9, 16));
        originales.add(new PokemonElectrico("Mareep",      85, 13,  9, 11));
    }

    public int getCantidad() {
        return originales.size();
    }

    
    public Pokemon obtenerCopia(int indice) {
        return originales.get(indice).clone();
    }

    public Pokemon obtenerCopiaAleatoria() {
        int indice = random.nextInt(originales.size());
        return obtenerCopia(indice);
    }

    
    public ArrayList<Pokemon> obtenerTodos() {
        ArrayList<Pokemon> lista = new ArrayList<>();
        for (int i = 0; i < originales.size(); i++) {
            lista.add(obtenerCopia(i));
        }
        return lista;
    }
}