package modelo;

import Excepciones.AccionInvalidaException;

public class Recuperacion implements Habilidad {
    public static final int USOS_MAXIMOS = 2;

    @Override
    public String ejecutar(Pokemon usuario, Pokemon objetivo) throws AccionInvalidaException {
        if (usuario.estaDebilitado()) {
            throw new AccionInvalidaException(usuario.getNombre() + " está debilitado y no puede actuar");
        }
        if (usuario.getUsosRecuperacion() <= 0) {
            throw new AccionInvalidaException(usuario.getNombre() + " ya no tiene usos de recuperación ");
        }
        if (usuario.getPsActual() == usuario.getPsMax()) {
            throw new AccionInvalidaException(usuario.getNombre() + " ya tiene los PS completos ");
        }
        usuario.consumirUsoRecuperacion();
        int cantidad = usuario.getPsMax() / 4;
        int recuperado = usuario.curar(cantidad);
        return usuario.getNombre() + " se recuperó " + recuperado + " PS.";
    }

    @Override
    public String obtenerNombre() {
        return "Recuperar";
    }
}
