package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class GotaRoja extends Gota {
    public GotaRoja(float x, float y, float vy, Texture textura, Movimiento movimiento) {
        super(x, y, 0f, vy, textura, movimiento);
    }

    @Override
    public void aplicar(IGotaReceptor receptor) {
        receptor.recibirDano(1);
        setActiva(false);
    }
}