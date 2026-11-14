package modelo;

public class PokemonFuego extends Pokemon {

    public PokemonFuego(String nombre, int psMax, int ataque, int defensa, int velocidad) {
        super(nombre, TipoPokemon.FUEGO, psMax, ataque, defensa, velocidad, "Lanzallamas", 20);
    }

    @Override
    protected String efectoEspecial(Pokemon objetivo) {
        int danio = golpear(objetivo, getPotenciaEspecial(), 1.0);
        return textoGolpe(objetivo, danio);
    }
}
