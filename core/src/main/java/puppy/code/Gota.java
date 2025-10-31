package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Gota {
    private float x, y;
    private float velocidadX;
    private float velocidadY;
    private int ancho = 64, alto = 64;
    private Texture textura;
    private boolean activa = true;
    private Movimiento movimiento;

    public Gota(float x, float y, float velocidadX, float velocidadY, Texture textura, Movimiento movimiento) {
        this.x = x;
        this.y = y;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.textura = textura;
        this.movimiento = movimiento;
    }

    public void update(float delta, float w, float h) {
        if (!activa) return;
        movimiento.actualizar(this, delta, w, h);
    }

    public void render(SpriteBatch batch) {
        if (!activa || textura == null) return;
        batch.draw(textura, x, y, ancho, alto);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }

    // Encapsulamiento: getters y setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getVelocidadX() { return velocidadX; }
    public void setVelocidadX(float velocidadX) { this.velocidadX = velocidadX; }
    public float getVelocidadY() { return velocidadY; }
    public void setVelocidadY(float velocidadY) { this.velocidadY = velocidadY; }
    public int getAncho() { return ancho; }
    public void setAncho(int ancho) { this.ancho = ancho; }
    public int getAlto() { return alto; }
    public void setAlto(int alto) { this.alto = alto; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public Movimiento getMovimiento() { return movimiento; }
    public void setMovimiento(Movimiento movimiento) { this.movimiento = movimiento; }

    public abstract void aplicar(IGotaReceptor receptor);

    // Interfaz de movimiento (puede estar en archivo separado si lo prefieres)
    public interface Movimiento {
        void actualizar(Gota gota, float delta, float ancho, float alto);
    }
}