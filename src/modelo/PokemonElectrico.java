package modelo;

public class PokemonElectrico extends Pokemon {

    public static final double PROBABILIDAD_CRITICO = 0.25;
    public static final double MULTIPLICADOR_CRITICO = 1.5;

    public PokemonElectrico(String nombre, int psMax, int ataque, int defensa, int velocidad) {
        super(nombre, TipoPokemon.ELECTRICO, psMax, ataque, defensa, velocidad, "Impactrueno", 17);
    }

    @Override
    protected String efectoEspecial(Pokemon objetivo) {
        boolean critico = Math.random() < PROBABILIDAD_CRITICO;
        double extra = 1.0;
        if (critico) {
            extra = MULTIPLICADOR_CRITICO;
        }
        int danio = golpear(objetivo, getPotenciaEspecial(), extra);
        String texto = "";
        if (critico) {
            texto = "¡Golpe crítico! ";
        }
        return texto + textoGolpe(objetivo, danio);
    }
}
