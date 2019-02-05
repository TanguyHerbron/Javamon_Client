package fr.ensim.lemeeherbron.entities.interaction;

import java.util.HashMap;

public class Dialog {

    private String currentMessage;
    private HashMap<Integer, Dialog> dialogMap;
    private boolean mustChoosePokemon = false;
    private boolean hasChoice = false;
    private HashMap<Integer, String> choiceMap;

    public Dialog(String text)
    {
        this.currentMessage = text;
        dialogMap = new HashMap<>();
    }

    public void setNextDialog(Dialog dialog)
    {
        dialogMap.put(0, dialog);
    }

    public void setNextDialog(int index, Dialog dialog)
    {
        dialogMap.put(index, dialog);
    }

    public Dialog getNextDialog()
    {
        return dialogMap.get(0);
    }

    public Dialog getDialog(int index)
    {
        return dialogMap.get(index);
    }

    public String getText()
    {
        return currentMessage;
    }

    public boolean mustChoosePokemon() {
        return mustChoosePokemon;
    }

    public void setMustChoosePokemon(boolean mustChoosePokemon) {
        this.mustChoosePokemon = mustChoosePokemon;
    }

    public void setHasChoice(boolean hasChoice)
    {
        this.hasChoice = hasChoice;
    }

    public void setChoices(HashMap<Integer, String> choiceMap)
    {
        this.choiceMap = choiceMap;
    }

    public boolean hasChoice()
    {
        return hasChoice;
    }

    public HashMap<Integer, String> getChoices()
    {
        return choiceMap;
    }
}
