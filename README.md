# Javamon

![Javamon](https://github.com/TanguyHerbron/Javamon_Client/raw/master/img/javamon.png)

Javamon is a multiplayer game programmed in Java and using the JavaFX framework for rendering. This game has been built as a showcase for different technologies (sprite rendering, pathfinding etc.). The only goal in this game so far is to reproduce as much Pokemons as you can.

## Graphic engine

Because JavaFX is not made to be a graphic engine for games, I had to program a small engine on top using canvas to render sprites.

### Terrain management
#### Dynamic terrain generation

Each map is a 32x32 grid on which we apply a texture. To generate those maps and there interactive objects/obstacles, a method of storing all those points had to be found. This method had to allow quick and easy loading of each map, without having to hard code them.

The storage of this information is done via a PNG file of 32x32 pixels, where each pixel is a texture of the game. To find which texture to apply for a given pixel, the engine decomposes the RGB components of each pixel to find the following values :

- R : Free space, can be used later in development for additional information
- G : Obstacles and interactive elements
- B : Corresponding texture

![Texturing](https://github.com/TanguyHerbron/Javamon_Client/raw/master/img/texturing.png)

To not waste any precious entry of the color encoding, variant textures are saved using the same color value. The engine find automatically which texture should be applied among the possible variants depending on the texture's surrounding.

![Dynamic tile](https://github.com/TanguyHerbron/Javamon_Client/raw/master/img/dynamic_tile.png)

#### Obstacle management

Obstacles are used to block the path of a player as well as a Pokemon using the Pathfinder (described later). Those obstacles are generated using the previously described method, each obstacle can then be set on any texture. When an obstacle is created, it is stored in a table on the Terrain and each obstacle is tested for collision when the current frame is redrawn (about 60 times per second).

![Collision](https://github.com/TanguyHerbron/Javamon_Client/raw/master/img/collision.png)
