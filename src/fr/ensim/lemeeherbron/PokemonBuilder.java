package fr.ensim.lemeeherbron;

import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.terrain.Terrain;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

public class PokemonBuilder {

    public static Pokemon build(String name, double borderX, double borderY) throws PokemonNotLoadedException {
        Pokemon newPokemon = null;

        try {
            InputStream inputStream = PokemonBuilder.class.getResourceAsStream("/preset/pokemon/" + name + ".xml");
            Properties properties = new Properties();
            properties.loadFromXML(inputStream);

            String spriteName = properties.getProperty("sprite");
            int pokemonSpeed = Integer.parseInt(properties.getProperty("speed"));
            boolean pokemonBehavior = Boolean.parseBoolean(properties.getProperty("behavior"));

            newPokemon = new Pokemon(spriteName,
                    borderX, borderY, pokemonSpeed,
                    pokemonBehavior, Terrain.getInstance(), new Random().nextInt(2));

            String[] evolutions = properties.getProperty("evolution").split(Pattern.quote("$"));

            for(String evolution : evolutions)
            {
                if(evolution.length() > 0)
                {
                    newPokemon.addEvolutions(Integer.parseInt(evolution.substring(0, evolution.indexOf("|"))), evolution.substring(evolution.indexOf("|") + 1));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(newPokemon == null)
        {
            throw new PokemonNotLoadedException();
        }
        else
        {
            return newPokemon;
        }
    }
}
