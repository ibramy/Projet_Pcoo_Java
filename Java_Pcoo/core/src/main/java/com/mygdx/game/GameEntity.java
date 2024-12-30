package com.mygdx.game;
//Importation des bibliothèques nécessaires pour la gestion du jeu, des graphismes, des entrées, et des cartes Tiled.

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


public abstract class GameEntity {
    protected Vector2 position;  // La position de l'entité dans le jeu (ex: joueur, alien)
    protected Sprite sprite;     // Le sprite représentant l'entité dans le jeu

    // Méthode abstraite pour dessiner l'entité à l'écran, chaque classe dérivée devra la définir
    public abstract void draw(SpriteBatch batch);

    // Méthode générique pour mettre à jour l'état de l'entité
    public void update(float deltaTime) {
        // Méthode par défaut, peut être redéfinie dans les classes dérivées
    }

    // Getter pour la position de l'entité
    public Vector2 getPosition() {
        return position;
    }

    // Setter pour la position de l'entité
    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
