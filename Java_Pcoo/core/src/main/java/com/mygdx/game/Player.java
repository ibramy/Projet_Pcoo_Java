package com.mygdx.game;

// Importation des classes nécessaires pour gérer les entrées, textures, sprites, et mathématiques.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

// Définition de la classe Player, représentant le joueur dans le jeu.
public class Player extends GameEntity {
    // Position actuelle du joueur dans l'espace 2D.
    public Vector2 position;

    // Position actuelle de la balle tirée par le joueur.
    public Vector2 position_bullet;

    // Sprite représentant l'image graphique du joueur.
    public Sprite sprite;

    // Sprite représentant la balle du joueur.
    public Sprite sprite_bullet;

    // Vitesse de déplacement du joueur (en pixels par seconde).
    public float speed = 300;

    // Vitesse de déplacement de la balle (en pixels par seconde).
    public float speed_bullet = 1000;

    // Constructeur de la classe Player, qui initialise le joueur et sa balle.
    // img : texture utilisée pour le sprite du joueur.
    // img_bullet : texture utilisée pour le sprite de la balle.
    public Player(Texture img, Texture img_bullet) {
        // Initialisation du sprite du joueur avec l'image fournie.
        sprite = new Sprite(img);

        // Initialisation du sprite de la balle avec l'image fournie.
        sprite_bullet = new Sprite(img_bullet);

        // Mise à l'échelle de la balle.
        sprite_bullet.setScale(4);

        // Mise à l'échelle du joueur.
        sprite.setScale(4);

        // Position initiale du joueur : centré horizontalement, en bas de l'écran.
        position = new Vector2(Gdx.graphics.getWidth() / 2, sprite.getScaleY() * sprite.getHeight() / 2);

        // Position initiale de la balle : en dehors de l'écran.
        position_bullet = new Vector2(0, 10000);
    }

    // Mise à jour de la position du joueur et de la balle en fonction des entrées et du temps écoulé.
    // deltaTime : intervalle de temps depuis la dernière mise à jour.
    public void Update(float deltaTime) {
        // Si la barre d'espace est enfoncée et que la balle est hors écran, repositionne la balle.
        if (Gdx.input.isKeyPressed(Keys.SPACE) && position_bullet.y >= Gdx.graphics.getHeight()) {
            position_bullet.x = position.x + 4; // Place la balle au centre du joueur.
            position_bullet.y = 0; // Ramène la balle au bas de l'écran.
        }

        // Déplace le joueur vers la gauche si la flèche gauche est enfoncée.
        if (Gdx.input.isKeyPressed(Keys.LEFT)) position.x -= deltaTime * speed;

        // Déplace le joueur vers la droite si la flèche droite est enfoncée.
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) position.x += deltaTime * speed;

        // Empêche le joueur de sortir des limites de l'écran à gauche.
        if (position.x - (sprite.getWidth() * sprite.getScaleX() / 2) <= 0)
            position.x = (sprite.getWidth() * sprite.getScaleX() / 2);

        // Empêche le joueur de sortir des limites de l'écran à droite.
        if (position.x + (sprite.getWidth() * sprite.getScaleX() / 2) >= Gdx.graphics.getWidth())
            position.x = Gdx.graphics.getWidth() - (sprite.getWidth() * sprite.getScaleX() / 2);

        // Déplace la balle vers le haut à la vitesse définie.
        position_bullet.y += deltaTime * speed_bullet;
    }

    // Dessine le joueur et sa balle à l'écran.
    // batch : SpriteBatch utilisé pour gérer les dessins des sprites.
    public void draw(SpriteBatch batch) {
        // Met à jour les positions du joueur et de la balle.
        Update(Gdx.graphics.getDeltaTime());

        // Place et dessine le sprite du joueur.
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);

        // Place et dessine le sprite de la balle.
        sprite_bullet.setPosition(position_bullet.x, position_bullet.y);
        sprite_bullet.draw(batch);
    }
}
