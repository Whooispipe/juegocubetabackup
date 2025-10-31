package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Importa las gotas
import puppy.code.Gota;
import puppy.code.GotaAzul;
import puppy.code.GotaRoja;
import puppy.code.GotaVerde;
import puppy.code.GotaAmarilla;

public class Lluvia {
    private final Array<Gota> gotas = new Array<>();
    private Nivel nivel;
    private Texture gotaAzul, gotaRoja, gotaVerde, gotaAmarilla;
    private Sound dropSound;
    private Music rainMusic;
    private long lastSpawnNs;

    public Lluvia(Texture azul, Texture roja, Texture verde, Texture amarilla, Sound dropSound, Music rainMusic) {
        this.gotaAzul = azul;
        this.gotaRoja = roja;
        this.gotaVerde = verde;
        this.gotaAmarilla = amarilla;
        this.dropSound = dropSound;
        this.rainMusic = rainMusic;
    }

    public void crear() {
        nivel = new Nivel(1);
        if (rainMusic != null) {
            rainMusic.setLooping(true);
            rainMusic.play();
        }
        lastSpawnNs = TimeUtils.nanoTime();
    }

    public boolean actualizarMovimiento(IGotaReceptor receptor) {
        float delta = Gdx.graphics.getDeltaTime();
        float ancho = 800f, alto = 480f;

        if (receptor instanceof Tarro) {
            ((Tarro)receptor).updateTimers(delta);
        }

        if (TimeUtils.nanoTime() - lastSpawnNs > 100_000_000) {
            List<Gota> nuevas = nivel.spawnTick(
                new Nivel.Textures(gotaAzul, gotaRoja, gotaVerde, gotaAmarilla), ancho, alto
            );
            for (Gota g : nuevas) gotas.add(g);
            lastSpawnNs = TimeUtils.nanoTime();
        }

        for (Gota g : gotas) {
            g.update(delta, ancho, alto);
        }

        Rectangle bTarro = null;
        if (receptor instanceof Tarro) {
            bTarro = ((Tarro)receptor).getArea();
        }

        for (Iterator<Gota> it = gotas.iterator(); it.hasNext();) {
            Gota g = it.next();
            if (!g.isActiva()) {
                it.remove();
                continue;
            }
            if (bTarro != null && g.getBounds().overlaps(bTarro)) {
                g.aplicar(receptor);
                nivel.onRecogerGota(g);
                if (g instanceof GotaAzul && dropSound != null) dropSound.play();
                it.remove();
            }
        }

        if (nivel.completo()) {
            int n = nivel.getNumero();
            if (n < 3) {
                nivel = new Nivel(n + 1);
            } else {
                nivel = new Nivel(1);
            }
            gotas.clear();
        }

        return !receptor.estaMuerto();
    }

    public void actualizarDibujoLluvia(SpriteBatch batch) {
        for (Gota g : gotas) {
            g.render(batch);
        }
    }

    public int getNivelNumero() { return nivel.getNumero(); }
    public int getObjetivoAzules() { return nivel.getObjetivoAzules(); }
    public int getAzulesRecogidas() { return nivel.getAzulesRecogidas(); }

    // Movimiento sigue aquí, ahora como clase externa o puedes extraerla a su propio archivo si prefieres
    public static class MovimientoVertical implements Gota.Movimiento {
        @Override
        public void actualizar(Gota gota, float delta, float ancho, float alto) {
            gota.setY(gota.getY() - gota.getVelocidadY() * delta);
            if (gota.getY() + gota.getAlto() < 0) {
                gota.setActiva(false);
            }
        }
    }

    public static class MovimientoDiagonal implements Gota.Movimiento {
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

    public static class Nivel {
        private final int numero;
        private final int objetivoAzules;
        private int azulesRecogidas = 0;

        public Nivel(int numero) {
            this.numero = numero;
            int objetivo;
            switch (numero) {
                case 1: objetivo = 30; break;
                case 2: objetivo = 45; break;
                default: objetivo = 55; break;
            }
            this.objetivoAzules = objetivo;
        }

        public boolean completo() {
            return azulesRecogidas >= objetivoAzules;
        }

        public void onRecogerGota(Gota g) {
            if (g instanceof GotaAzul) azulesRecogidas++;
        }

        public List<Gota> spawnTick(Textures t, float ancho, float alto) {
            List<Gota> nuevas = new ArrayList<>();
            if (MathUtils.randomBoolean(0.06f)) nuevas.add(crearAzul(t, ancho, alto));
            if (MathUtils.randomBoolean(0.03f)) nuevas.add(crearRoja(t, ancho, alto));
            if (numero >= 2 && MathUtils.randomBoolean(0.015f)) nuevas.add(crearAmarilla(t, ancho, alto));
            if (numero >= 2 && MathUtils.randomBoolean(0.015f)) nuevas.add(crearVerde(t, ancho, alto));
            return nuevas;
        }

        private Gota.Movimiento movimientoParaAzulYRoja() {
            if (numero >= 3 && MathUtils.randomBoolean(0.5f)) return new MovimientoDiagonal();
            return new MovimientoVertical();
        }

        private Gota.Movimiento vertical() { return new MovimientoVertical(); }

        private Gota crearAzul(Textures t, float ancho, float alto) {
            float x = MathUtils.random(0, ancho - 64);
            float vy = MathUtils.random(160f, 240f);
            Gota.Movimiento m = movimientoParaAzulYRoja();
            GotaAzul g = new GotaAzul(x, alto, vy, t.azul, m);
            if (m instanceof MovimientoDiagonal) {
                g.setVelocidadX(MathUtils.randomSign() * MathUtils.random(80f, 120f));
            }
            return g;
        }

        private Gota crearRoja(Textures t, float ancho, float alto) {
            float x = MathUtils.random(0, ancho - 64);
            float vy = MathUtils.random(180f, 270f);
            Gota.Movimiento m = movimientoParaAzulYRoja();
            GotaRoja g = new GotaRoja(x, alto, vy, t.roja, m);
            if (m instanceof MovimientoDiagonal) {
                g.setVelocidadX(MathUtils.randomSign() * MathUtils.random(110f, 150f));
            }
            return g;
        }

        private Gota crearAmarilla(Textures t, float ancho, float alto) {
            float x = MathUtils.random(0, ancho - 64);
            float vy = MathUtils.random(150f, 220f);
            return new GotaAmarilla(x, alto, vy, t.amarilla, vertical());
        }

        private Gota crearVerde(Textures t, float ancho, float alto) {
            float x = MathUtils.random(0, ancho - 64);
            float vy = MathUtils.random(140f, 200f);
            return new GotaVerde(x, alto, vy, t.verde, vertical());
        }

        public int getNumero() { return numero; }
        public int getObjetivoAzules() { return objetivoAzules; }
        public int getAzulesRecogidas() { return azulesRecogidas; }

        public static class Textures {
            public Texture azul, roja, verde, amarilla;
            public Textures(Texture azul, Texture roja, Texture verde, Texture amarilla) {
                this.azul = azul; this.roja = roja; this.verde = verde; this.amarilla = amarilla;
            }
        }
    }
}