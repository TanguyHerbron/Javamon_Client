package fr.ensim.lemeeherbron;

import fr.ensim.lemeeherbron.entities.Pokemon;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.URL;
import java.util.Properties;

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

            newPokemon = new Pokemon( spriteName,
                    borderX, borderY, pokemonSpeed,
                    pokemonBehavior);
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
