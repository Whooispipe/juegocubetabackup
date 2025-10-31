package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Texture dropTexture;

    // Botón "Start Game"
    private float startX, startY, startWidth, startHeight;
    // Botón "Exit"
    private float exitX, exitY, exitWidth, exitHeight;

    public MainMenuScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Carga la imagen de la gota
        dropTexture = new Texture(Gdx.files.internal("drop_blue.png"));

        // Botón "Start Game"
        startWidth = 360;
        startHeight = 60;
        startX = 220;
        startY = 190;
        // Botón "Exit"
        exitWidth = 360;
        exitHeight = 60;
        exitX = 220;
        exitY = 110;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.4f, 0.6f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Título
        font.setColor(Color.YELLOW);
        font.getData().setScale(3.5f);
        GlyphLayout titleLayout = new GlyphLayout(font, "CATCH");
        font.draw(batch, "CATCH", (800-titleLayout.width)/2, 410);
        titleLayout.setText(font, "THE DROP");
        font.draw(batch, "THE DROP", (800-titleLayout.width)/2, 340);

        // Imagen de gota a la derecha
        batch.draw(dropTexture, 600, 220, 120, 120);

        // Botones
        font.getData().setScale(2.2f);
        drawButton(batch, "START GAME", startX, startY, startWidth, startHeight, Color.ORANGE, Color.BROWN);
        drawButton(batch, "EXIT", exitX, exitY, exitWidth, exitHeight, Color.ORANGE, Color.BROWN);

        batch.end();

        handleInput();
    }

    private void drawButton(SpriteBatch batch, String text, float x, float y, float width, float height, Color color, Color borderColor) {
        // Fondo del botón (puedes mejorar con ShapeRenderer si quieres)
        // Aquí solo se dibuja el texto centrado
        font.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout(font, text);
        float textX = x + (width - layout.width) / 2;
        float textY = y + (height + layout.height) / 2;
        font.draw(batch, text, textX, textY);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 480 - Gdx.input.getY();

            // Botón "Start Game"
            if (mouseX >= startX && mouseX <= startX+startWidth && mouseY >= startY && mouseY <= startY+startHeight) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
            // Botón "Exit"
            if (mouseX >= exitX && mouseX <= exitX+exitWidth && mouseY >= exitY && mouseY <= exitY+exitHeight) {
                Gdx.app.exit();
            }
        }

        // Enter para iniciar, ESC para salir
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void show() { }
    @Override
    public void resize(int width, int height) { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
    @Override
    public void dispose() {
        dropTexture.dispose();
        font.dispose();
        batch.dispose();
    }
}