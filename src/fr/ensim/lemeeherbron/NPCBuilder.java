package fr.ensim.lemeeherbron;

import fr.ensim.lemeeherbron.entities.NPC;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class NPCBuilder {

    public static NPC build(String name, int tileX, int tileY) throws NPCNotLoadedException {
        NPC newNpc = null;

        try {
            InputStream inputStream = NPCBuilder.class.getResourceAsStream("/preset/npc/" + name + ".xml");
            Properties properties = new Properties();
            properties.loadFromXML(inputStream);

            String spriteName = properties.getProperty("sprite");
            char orientation = properties.getProperty(properties.getProperty("orientation")).charAt(0);

            newNpc = new NPC(spriteName, orientation, tileX, tileY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(newNpc == null)
        {
            throw new NPCNotLoadedException();
        }
        else
        {
            return newNpc;
        }
    }

}
