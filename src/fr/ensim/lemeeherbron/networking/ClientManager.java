package fr.ensim.lemeeherbron.networking;

import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.terrain.Nursery;
import fr.ensim.lemeeherbron.terrain.Terrain;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    private final static int PORT = 7777;

    private Socket clientSocket;
    private int id;

    private PrintWriter pw;
    private BufferedReader br;

    public ClientManager(String ip) throws IOException
    {
        clientSocket = new Socket(ip, PORT);

        pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        id = Integer.parseInt(br.readLine());

        System.out.println("My id is " + id);
    }

    public void sendPokemons(List<Pokemon> pokemons)
    {
        JSONObject mainObject = new JSONObject();

        mainObject.put("clientId", id);

        JSONArray jsonArray = new JSONArray();

        for(Pokemon pokemon : pokemons)
        {
            JSONObject pokemonObject = new JSONObject();
            pokemonObject.put("id", pokemon.getId());
            pokemonObject.put("name", pokemon.getSpriteName());
            pokemonObject.put("x", pokemon.getX());
            pokemonObject.put("y", pokemon.getY());
            pokemonObject.put("orientation", String.valueOf(pokemon.getOrientation()));
            pokemonObject.put("speed", pokemon.getSpeed());
            pokemonObject.put("sexe", pokemon.getSexe());

            jsonArray.put(pokemonObject);
        }

        mainObject.put("pokemon", jsonArray);

        pw.println(mainObject.toString());
        pw.flush();
    }

    public List<Pokemon> updatePokemons()
    {
        List<Pokemon> serverPokemons = new ArrayList<>();

        try {
            if(br.ready())
            {
                String str = br.readLine();

                JSONArray jsonArray = new JSONArray(str);

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Pokemon pokemon = new Pokemon(jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getInt("x"),
                            jsonObject.getInt("y"),
                            (char) Integer.parseInt(jsonObject.get("orientation").toString()));

                    pokemon.setSpeed(jsonObject.getInt("speed"));

                    serverPokemons.add(pokemon);

                    if(pokemon.getId() == 0)
                    {
                        String spriteName = jsonObject.getString("name").substring(jsonObject.getString("name").lastIndexOf("/")+1);
                        Pokemon newPokemon = new Pokemon(spriteName
                                , 512
                                , 512
                                , jsonObject.getInt("speed")
                                , true
                                , Terrain.getInstance()
                                , jsonObject.getInt("sexe"));

                        Nursery.addPokemon(newPokemon);

                        newPokemon.setPosition(jsonObject.getDouble("x"), jsonObject.getDouble("y"));

                        System.out.println("Received new pokemon");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverPokemons;
    }
}
