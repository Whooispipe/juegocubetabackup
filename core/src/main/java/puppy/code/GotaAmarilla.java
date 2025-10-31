package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class GotaAmarilla extends Gota {
    public GotaAmarilla(float x, float y, float vy, Texture textura, Movimiento movimiento) {
        super(x, y, 0f, vy, textura, movimiento);
    }

    @Override
    public void aplicar(IGotaReceptor receptor) {
        receptor.aplicarBoostVelocidad(0.25f, 8f);
        setActiva(false);
    }
}