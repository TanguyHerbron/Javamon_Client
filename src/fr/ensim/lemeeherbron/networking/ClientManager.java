package fr.ensim.lemeeherbron.networking;

import fr.ensim.lemeeherbron.entities.Pokemon;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientManager {

    private final static int PORT = 7777;

    private Socket clientSocket;
    private int id;

    private PrintWriter pw;
    private BufferedReader bf;

    public ClientManager(String ip) throws IOException
    {
        clientSocket = new Socket(ip, PORT);

        pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        id = Integer.parseInt(bf.readLine());

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

            jsonArray.put(pokemonObject);
        }

        mainObject.put("pokemon", jsonArray);

        pw.println(mainObject.toString());
        pw.flush();
    }

}
