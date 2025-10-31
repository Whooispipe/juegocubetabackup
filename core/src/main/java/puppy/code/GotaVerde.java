package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class GotaVerde extends Gota {
    public GotaVerde(float x, float y, float vy, Texture textura, Movimiento movimiento) {
        super(x, y, 0f, vy, textura, movimiento);
    }

    @Override
    public void aplicar(IGotaReceptor receptor) {
        receptor.curar(1);
        setActiva(false);
    }
}