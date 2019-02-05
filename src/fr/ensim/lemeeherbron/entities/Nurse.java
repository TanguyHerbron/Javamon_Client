package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.entities.interaction.Dialog;

import java.util.HashMap;

public class Nurse extends NPC {

    public Nurse(String spriteName, char orientation) {
        super(spriteName, orientation);

        Dialog giveDialog = new Dialog("Which pokemon do you want to give us ?");
        giveDialog.setMustChoosePokemon(true);
        giveDialog.setNextDialog(new Dialog("See you soon !"));

        Dialog takeDialog = new Dialog("Which pokemon do you want to take with you ?");
        takeDialog.setMustChoosePokemon(true);
        takeDialog.setNextDialog(new Dialog("See you soon !"));

        dialog = new Dialog("What do you want ?");
        dialog.setHasChoice(true);
        HashMap<Integer, String> choiceMap = new HashMap<>();
        choiceMap.put(0, "Give Pokemon");
        choiceMap.put(1, "Take Pokemon");
        dialog.setChoices(choiceMap);
        dialog.setNextDialog(0, giveDialog);
        dialog.setNextDialog(1, takeDialog);
    }
}
