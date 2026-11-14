
package modelo;

import Excepciones.AccionInvalidaException;

public class AtaqueBasico implements Habilidad {
    public static final int POTENCIA = 10;

    @Override
    public String ejecutar(Pokemon usuario, Pokemon objetivo) throws AccionInvalidaException {
        if (usuario.estaDebilitado()) {
            throw new AccionInvalidaException(usuario.getNombre() + " está debilitado y no puede atacar.");
        }
        int danio = usuario.golpear(objetivo, POTENCIA, 1.0);
        return usuario.getNombre() + " usó " + obtenerNombre() + ". "
                + usuario.textoGolpe(objetivo, danio);
    }

    @Override
    public String obtenerNombre() {
        return "Ataque básico";
    }
}
