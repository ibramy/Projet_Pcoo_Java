package com.mygdx.game;

//Importation des bibliothèques nécessaires pour la gestion du jeu, des graphismes, des entrées, et des cartes Tiled.
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

//Classe principale du jeu qui hérite d'ApplicationAdapter.
public class MyGdxGame extends ApplicationAdapter {
 // Gestionnaire de dessins des sprites.
 SpriteBatch batch;

 // Textures pour le joueur, les balles, et les aliens.
 Texture img;
 Texture img_bullet;
 Texture img_alien;

 // Objet représentant le joueur.
 Player player;

 // Tableau d'objets Alien représentant les ennemis.
 Alien[] aliens;

 // Paramètres pour configurer les rangées et colonnes des aliens.
 int NumWidth_aliens = 8;
 int NumHeight_aliens = 4;
 int spacing_aliens = 65; // Espacement entre les aliens.

 // Bornes pour les coordonnées des aliens.
 int minX_aliens;
 int minY_aliens;
 int maxX_aliens;
 int maxY_aliens;

 // Direction et vitesse des aliens.
 int direction_aliens = 1; // 1 pour la droite, -1 pour la gauche.
 float speed_aliens = 90;

 // Gestion de la carte Tiled.
 TiledMap tiledMap;
 OrthogonalTiledMapRenderer tiledMapRenderer;
 OrthographicCamera camera;

 // Offset pour déplacer les aliens.
 Vector2 offset_aliens;

 // Indicateur pour vérifier si le joueur est mort.
 boolean playerIsDead = false;

 @Override
 public void create() {
     // Chargement de la carte Tiled et initialisation du renderer.
     tiledMap = new TmxMapLoader().load("starfield.tmx");
     tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

     // Initialisation de la caméra pour centrer sur la carte.
     camera = new OrthographicCamera();
     camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
     float mapWidth = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);
     float mapHeight = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
     camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
     camera.update();

     // Initialisation de l'offset des aliens.
     offset_aliens = Vector2.Zero;

     // Chargement des textures et création des objets du jeu.
     batch = new SpriteBatch();
     img = new Texture("player.png");
     img_bullet = new Texture("bullet.png");
     img_alien = new Texture("alien.png");
     player = new Player(img, img_bullet);

     // Initialisation des aliens dans une grille.
     aliens = new Alien[NumWidth_aliens * NumHeight_aliens];
     int i = 0;
     for (int y = 0; y < NumHeight_aliens; y++) {
         for (int x = 0; x < NumWidth_aliens; x++) {
             Vector2 position = new Vector2(x * spacing_aliens, y * spacing_aliens);
             position.x += Gdx.graphics.getWidth() / 2;
             position.y += Gdx.graphics.getWidth();
             position.x -= (NumWidth_aliens / 2) * spacing_aliens;
             position.y -= (NumHeight_aliens) * spacing_aliens;
             aliens[i] = new Alien(position, img_alien);
             i++;
         }
     }
 }

 // Nombre d'aliens encore vivants.
 int amount_alive_aliens = 0;

 @Override
 public void render() {
     // Temps écoulé depuis le dernier rendu.
     float deltaTime = Gdx.graphics.getDeltaTime();

     // Mise à jour de la caméra et du renderer.
     camera.update();
     tiledMapRenderer.setView(camera);
     ScreenUtils.clear(0, 0, 0, 1); // Nettoyage de l'écran (noir).
     tiledMapRenderer.render();

     // Commence à dessiner les objets.
     batch.begin();

     // Affiche le joueur ou un écran de Game Over si le joueur est mort.
     if (!playerIsDead) {
         player.draw(batch);
     } else {
         batch.draw(new Texture("game_over.jpg"), Gdx.graphics.getWidth() / 2 - 225, Gdx.graphics.getHeight() / 2);
     }

     // Gestion des collisions balle-alien.
     for (int i = 0; i < aliens.length; i++) {
         if (aliens[i].Alive && player.sprite_bullet.getBoundingRectangle().overlaps(aliens[i].sprite.getBoundingRectangle())) {
             player.position_bullet.y = 10000; // Réinitialise la balle.
             aliens[i].Alive = false; // Alien détruit.
             break;
         }
     }

     // Calcul des limites des aliens vivants.
     minX_aliens = 10000;
     minY_aliens = 10000;
     maxX_aliens = 0;
     maxY_aliens = 0;
     amount_alive_aliens = 0;

     for (int i = 0; i < aliens.length; i++) {
         if (aliens[i].Alive) {
             int IndexX = i % NumWidth_aliens;
             int IndexY = i / NumWidth_aliens;
             if (IndexX > maxX_aliens) maxX_aliens = IndexX;
             if (IndexX < minX_aliens) minX_aliens = IndexX;
             if (IndexY > maxY_aliens) maxY_aliens = IndexY;
             if (IndexY < minY_aliens) minY_aliens = IndexY;
             amount_alive_aliens++;
         }
     }

     // Réinitialisation si tous les aliens sont détruits.
     if (amount_alive_aliens == 0) {
         for (int i = 0; i < aliens.length; i++) {
             aliens[i].Alive = true;
         }
         offset_aliens = new Vector2(0, 0);
         batch.end();
         speed_aliens = 150;
         return;
     }

     // Mise à jour des positions et mouvements des aliens.
     offset_aliens.x += direction_aliens * deltaTime * speed_aliens;
     if (aliens[maxX_aliens].position.x >= Gdx.graphics.getWidth()) {
         direction_aliens = -1;
         offset_aliens.y -= aliens[0].sprite.getHeight() * aliens[0].sprite.getScaleY() * 0.25f;
         speed_aliens += 3; // Augmente la vitesse.
     }
     if (aliens[minX_aliens].position.x <= 0) {
         direction_aliens = 1;
         offset_aliens.y -= aliens[0].sprite.getHeight() * aliens[0].sprite.getScaleY() * 0.25f;
         speed_aliens += 3;
     }

     // Dessin des aliens restants.
     for (int i = 0; i < aliens.length; i++) {
         aliens[i].position = new Vector2(aliens[i].position_initial.x + offset_aliens.x, aliens[i].position_initial.y + offset_aliens.y);
         if (aliens[i].Alive) {
             aliens[i].draw(batch);

             // Vérifie si un alien touche le joueur.
             if (aliens[i].sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())) {
                 playerIsDead = true; // Le joueur meurt.
             }
         }
     }

     batch.end();
 }

 @Override
 public void dispose() {
     // Libération des ressources.
     batch.dispose();
     img.dispose();
     tiledMap.dispose();
     tiledMapRenderer.dispose();
 }
}
