package fr.ensim.lemeeherbron.entities.interaction;

public class Dialog {

    private String currentMessage;
    private Dialog nextDialog = null;
    private boolean mustChoosePokemon = false;

    public Dialog(String text)
    {
        this.currentMessage = text;
    }

    public void setNextDialog(Dialog dialog)
    {
        this.nextDialog = dialog;
    }

    public Dialog getNextDialog()
    {
        return nextDialog;
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
}
