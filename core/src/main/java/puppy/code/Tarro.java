package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Tarro implements IGotaReceptor {
    private Rectangle bucket;
    private Texture bucketImage;
    private Texture bucketHurt;
    private Sound sonidoHerido;

    private int vidas = 3;
    private int vidaMax = 3;
    private static final int VIDA_TOPE = 5;
    private int puntos = 0;

    private float velBase = 400f;
    private float velMultiplicador = 1f;
    private float boostRestante = 0f;

    private boolean invulnerable = false;
    private float invulnRestante = 0f;

    public Tarro(Texture tex, Texture hurt, Sound ss) {
        bucketImage = tex;
        bucketHurt = hurt;
        sonidoHerido = ss;
    }

    public void crear() {
        bucket = new Rectangle();
        bucket.x = 800 / 2f - 64 / 2f;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
    }

    public void updateTimers(float delta) {
        if (invulnerable) {
            invulnRestante -= delta;
            if (invulnRestante <= 0f) {
                invulnerable = false;
                invulnRestante = 0f;
            }
        }
        if (boostRestante > 0f) {
            boostRestante -= delta;
            if (boostRestante <= 0f) {
                boostRestante = 0f;
                velMultiplicador = 1f;
            }
        }
    }

    @Override
    public void recibirDano(int d) {
        if (invulnerable) return;
        vidas = Math.max(0, vidas - d);
        invulnerable = true;
        invulnRestante = 1.5f;
        if (sonidoHerido != null) sonidoHerido.play();
    }

    @Override
    public void curar(int c) {
        if (vidaMax < VIDA_TOPE) vidaMax = Math.min(VIDA_TOPE, vidaMax + 1);
        vidas = Math.min(vidaMax, vidas + c);
    }

    @Override
    public void aplicarBoostVelocidad(float porcentaje, float duracionSeg) {
        velMultiplicador = 1f + porcentaje;
        boostRestante = duracionSeg;
    }

    public void dibujar(SpriteBatch batch) {
        boolean flicker = invulnerable && ((int)(TimeUtils.millis() / 120) % 2 == 0);
        Texture tex = (flicker && bucketHurt != null) ? bucketHurt : bucketImage;

        if (!flicker)  {
            batch.draw(tex, bucket.x, bucket.y);
        } else {
            batch.draw(tex, bucket.x, bucket.y + MathUtils.random(-5,5));
        }
    }

    public void actualizarMovimiento() {
        float vel = getVelocidadActual();
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= vel * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += vel * Gdx.graphics.getDeltaTime();
        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;
    }

    @Override
    public void sumarPuntos(int pp) { puntos += pp; }

    public Rectangle getArea() { return bucket; }

    public void destruir() {
        if (bucketImage != null) bucketImage.dispose();
        if (bucketHurt != null) bucketHurt.dispose();
    }

    public int getVidas() { return vidas; }
    public int getVidaMax() { return vidaMax; }
    public int getPuntos() { return puntos; }
    @Override
    public boolean estaMuerto() { return vidas <= 0; }
    public float getVelocidadActual() { return velBase * velMultiplicador; }
}