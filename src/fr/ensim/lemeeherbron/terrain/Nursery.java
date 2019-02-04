package fr.ensim.lemeeherbron.terrain;

import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.entities.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Nursery {

    private static List<Pokemon> pokemons = new ArrayList<>();

    public static void init()
    {
        Pokemon pokemon = null;

        pokemon = new Pokemon("leviator", 512, 512, 5, true, Terrain.getInstance());
        pokemon.setPosition(256, 100);
        pokemons.add(pokemon);

        pokemon = new Pokemon("ptera", 512, 512, 5, true, Terrain.getInstance());
        pokemon.setPosition(256, 125);
        pokemons.add(pokemon);
    }

    public static void addPokemon(Pokemon pokemon)
    {
        pokemons.add(pokemon);
    }

    public static void removePokemon(Pokemon pokemon)
    {
        pokemons.remove(pokemon);
    }

    public static List<Pokemon> getPokemons()
    {
        return pokemons;
    }

    public static void triggerBehaviors()
    {
        for(Pokemon pokemon : pokemons)
        {
            pokemon.simulateBehavior();
        }
    }
}
