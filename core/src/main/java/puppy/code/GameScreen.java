package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final GameLluviaMenu game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Tarro tarro;
    private Lluvia lluvia;

    public GameScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();

        Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        Texture bucket = new Texture(Gdx.files.internal("bucket.png"));
        Texture bucketHurt = safeTexture("bucket_hurt.png");

        tarro = new Tarro(bucket, bucketHurt, hurtSound);

        Texture azul = safeTexture("drop_blue.png");
        Texture roja = safeTexture("drop_red.png");
        Texture verde = safeTexture("drop_green.png");
        Texture amarilla = safeTexture("drop_yellow.png");

        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        lluvia = new Lluvia(azul, roja, verde, amarilla, dropSound, rainMusic);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        tarro.crear();
        lluvia.crear();
    }

    private Texture safeTexture(String path) {
        try {
            return new Texture(Gdx.files.internal(path));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, "Gotas azules: " + lluvia.getAzulesRecogidas() + "/" + lluvia.getObjetivoAzules(), 5, 475);
        font.draw(batch, "Vidas: " + tarro.getVidas() + "/" + tarro.getVidaMax(), 670, 475);
        font.draw(batch, "Nivel: " + lluvia.getNivelNumero(), 380, 475);
        font.draw(batch, "Puntos: " + tarro.getPuntos(), 5, 450);
        font.draw(batch, "HighScore: " + game.getHigherScore(), camera.viewportWidth/2f - 50, 450);

        tarro.actualizarMovimiento();
        if (!lluvia.actualizarMovimiento(tarro)) {
            if (game.getHigherScore() < tarro.getPuntos())
                game.setHigherScore(tarro.getPuntos());
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        tarro.dibujar(batch);
        lluvia.actualizarDibujoLluvia(batch);

        batch.end();
    }

    @Override public void resize(int width, int height) { }
    @Override public void show() { }
    @Override public void hide() { }
    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void dispose() {
        tarro.destruir();
    }
}