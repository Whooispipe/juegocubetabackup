package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class GotaAzul extends Gota {
    public GotaAzul(float x, float y, float vy, Texture textura, Movimiento movimiento) {
        super(x, y, 0f, vy, textura, movimiento);
    }

    @Override
    public void aplicar(IGotaReceptor receptor) {
        receptor.sumarPuntos(1);
        setActiva(false);
    }
}