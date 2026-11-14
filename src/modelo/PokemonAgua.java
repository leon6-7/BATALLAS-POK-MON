package modelo;

public class PokemonAgua extends Pokemon {

    public PokemonAgua(String nombre, int psMax, int ataque, int defensa, int velocidad) {
        super(nombre, TipoPokemon.AGUA, psMax, ataque, defensa, velocidad, "Pistola Agua", 18);
    }

    @Override
    protected String efectoEspecial(Pokemon objetivo) {
        int danio = golpear(objetivo, getPotenciaEspecial(), 1.0);
        String texto = textoGolpe(objetivo, danio);
        int recuperado = curar(getPsMax() / 10);
        if (recuperado > 0) {
            texto += " " + getNombre() + " recuperó " + recuperado + " PS.";
        }
        return texto;
    }
}
