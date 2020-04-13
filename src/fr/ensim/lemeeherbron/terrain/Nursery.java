package fr.ensim.lemeeherbron.terrain;

import fr.ensim.lemeeherbron.PokemonBuilder;
import fr.ensim.lemeeherbron.PokemonNotLoadedException;
import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.entities.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Nursery {

    private static List<Pokemon> pokemons = new ArrayList<>();

    public static void init()
    {
        try {
            pokemons.add(PokemonBuilder.build("chetiflor", 512, 512));
            pokemons.get(pokemons.size() - 1).setPosition(320, 256);
            pokemons.add(PokemonBuilder.build("bulbizarre", 512, 512));
            pokemons.get(pokemons.size() - 1).setPosition(320, 256);
            pokemons.add(PokemonBuilder.build("magicarpe", 512, 512));
            pokemons.get(pokemons.size() - 1).setPosition(320, 256);
            pokemons.add(PokemonBuilder.build("mystherbe", 512, 512));
            pokemons.get(pokemons.size() - 1).setPosition(320, 256);
            pokemons.add(PokemonBuilder.build("pikachu", 512, 512));
            pokemons.get(pokemons.size() - 1).setPosition(320, 256);
            pokemons.add(PokemonBuilder.build("ptera", 512, 512));
            pokemons.get(pokemons.size() - 1).setPosition(320, 256);
        } catch (PokemonNotLoadedException e) {
            e.printStackTrace();
        }
    }

    public static void addPokemon(Pokemon pokemon)
    {
        pokemon.setPosition(320, 256);
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

    public static void setPokemons(List<Pokemon> newPokemons)
    {
        pokemons = newPokemons;
    }

    public static void triggerBehaviors()
    {
        for(Pokemon pokemon : pokemons)
        {
            pokemon.simulateBehavior();
            pokemon.pex();
        }
    }
}
