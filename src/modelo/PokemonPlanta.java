package modelo;

public class PokemonPlanta extends Pokemon {

    public PokemonPlanta(String nombre, int psMax, int ataque, int defensa, int velocidad) {
        super(nombre, TipoPokemon.PLANTA, psMax, ataque, defensa, velocidad, "Megaagotar", 18);
    }

    @Override
    protected String efectoEspecial(Pokemon objetivo) {
        int danio = golpear(objetivo, getPotenciaEspecial(), 1.0);
        String texto = textoGolpe(objetivo, danio);
        int absorbido = curar(danio / 2);
        if (absorbido > 0) {
            texto += " " + getNombre() + " absorbió " + absorbido + " PS.";
        }
        return texto;
    }
}
