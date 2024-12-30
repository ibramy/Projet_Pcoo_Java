package com.mygdx.game;

// Importation des classes nécessaires pour gérer les textures, les sprites et les vecteurs mathématiques.
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

// Définition de la classe Alien, représentant un ennemi dans le jeu.
public class Alien extends GameEntity{
    // Position actuelle de l'Alien dans l'espace 2D.
    public Vector2 position;

    // Position initiale de l'Alien, utilisée pour des réinitialisations éventuelles.
    public Vector2 position_initial;

    // Sprite représentant l'image graphique de l'Alien.
    public Sprite sprite;

    // Statut de vie de l'Alien. "true" signifie que l'Alien est en vie.
    public Boolean Alive = true;

    // Constructeur de la classe Alien, qui initialise sa position et son sprite.
    // _position : position initiale en tant que vecteur 2D.
    // img : texture utilisée pour créer le sprite.
    public Alien(Vector2 _position, Texture img) {
        // Initialisation de la position actuelle à partir du paramètre.
        position = _position;

        // Enregistrement de la position initiale.
        position_initial = position;

        // Création du sprite à partir de la texture fournie.
        sprite = new Sprite(img);

        // Mise à l'échelle de l'alien.
        sprite.setScale(4);
    }

    // Méthode pour dessiner l'Alien à l'écran.
    public void draw(SpriteBatch batch) {
        // Positionnement du sprite sur la scène en fonction de la position actuelle.
        sprite.setPosition(position.x, position.y);

        // Dessin du sprite dans le batch.
        sprite.draw(batch);
    }
}
