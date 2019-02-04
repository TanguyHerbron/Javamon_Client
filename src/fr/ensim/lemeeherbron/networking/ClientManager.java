package fr.ensim.lemeeherbron.networking;

import fr.ensim.lemeeherbron.entities.Pokemon;
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

    private List<Pokemon> serverPokemons;

    public ClientManager(String ip) throws IOException
    {
        clientSocket = new Socket(ip, PORT);

        pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        serverPokemons = new ArrayList<>();

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

            jsonArray.put(pokemonObject);
        }

        mainObject.put("pokemon", jsonArray);

        pw.println(mainObject.toString());
        pw.flush();
    }

    public List<Pokemon> updatePokemons()
    {
        try {
            if(br.ready())
            {
                serverPokemons.clear();

                String str = br.readLine();

                JSONArray jsonArray = new JSONArray(str);

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    serverPokemons.add(new Pokemon(jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getInt("x"),
                            jsonObject.getInt("y"),
                            jsonObject.get("orientation").toString().charAt(0)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverPokemons;
    }
}
