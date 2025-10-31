package puppy.code;

public class MovimientoVertical implements Movimiento {
    @Override
        public void actualizar(Gota gota, float delta, float ancho, float alto) {
            gota.setY(gota.getY() - gota.getVelocidadY() * delta);
            if (gota.getY() + gota.getAlto() < 0) {
                gota.setActiva(false);
            }
        }
}
