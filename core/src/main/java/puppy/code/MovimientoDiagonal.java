package puppy.code;

public class MovimientoDiagonal implements Movimiento {
        @Override
        public void actualizar(Gota gota, float delta, float ancho, float alto) {
            float nx = gota.getX() + gota.getVelocidadX() * delta;
            float ny = gota.getY() - gota.getVelocidadY() * delta;

            if (nx < 0) {
                nx = 0;
                gota.setVelocidadX(Math.abs(gota.getVelocidadX()));
            } else if (nx + gota.getAncho() > ancho) {
                nx = ancho - gota.getAncho();
                gota.setVelocidadX(-Math.abs(gota.getVelocidadX()));
            }

            gota.setX(nx);
            gota.setY(ny);

            if (gota.getY() + gota.getAlto() < 0) {
                gota.setActiva(false);
            }
        }
    }