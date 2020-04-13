package fr.ensim.lemeeherbron.networking;

import fr.ensim.lemeeherbron.entities.Entity;
import fr.ensim.lemeeherbron.entities.Player;
import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.terrain.Nursery;
import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.application.Platform;
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

    public void sendEntities(List<Pokemon> pokemons)
    {
        JSONObject mainObject = new JSONObject();
        JSONObject playerObject = new JSONObject();

        playerObject.put("sprite", Player.getInstance().getSpriteName());
        playerObject.put("x", Player.getInstance().getX());
        playerObject.put("y", Player.getInstance().getY());
        playerObject.put("orientation", String.valueOf(Player.getInstance().getOrientation()));
        playerObject.put("map", Terrain.getInstance().getValue());
        playerObject.put("walking", Player.getInstance().isWalking());

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

        mainObject.put("player", playerObject);
        mainObject.put("pokemon", jsonArray);

        pw.println(mainObject.toString());
        pw.flush();
    }

    public List<Entity> updateData()
    {
        List<Entity> serverEntities = new ArrayList<>();

        try {
            if(br.ready())
            {
                String str = br.readLine();

                JSONObject mainObject = new JSONObject(str);

                JSONArray entitiesArray = mainObject.getJSONArray("entities");

                for(int i = 0; i < entitiesArray.length(); i++)
                {
                    JSONObject jsonObject = entitiesArray.getJSONObject(i);

                    Player player = new Player(jsonObject.getString("sprite"),
                            jsonObject.getDouble("x"),
                            jsonObject.getDouble("y"),
                            (char) Integer.parseInt(jsonObject.get("orientation").toString()),
                            jsonObject.getBoolean("walking"));

                    serverEntities.add(player);
                }

                JSONArray pokemonArray = mainObject.getJSONArray("pokemon");

                for(int i = 0; i < pokemonArray.length(); i++)
                {
                    JSONObject jsonObject = pokemonArray.getJSONObject(i);

                    Pokemon pokemon = new Pokemon(jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getInt("x"),
                            jsonObject.getInt("y"),
                            (char) Integer.parseInt(jsonObject.get("orientation").toString()));

                    pokemon.setSpeed(jsonObject.getInt("speed"));

                    serverEntities.add(pokemon);

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

        return serverEntities;
    }
}
