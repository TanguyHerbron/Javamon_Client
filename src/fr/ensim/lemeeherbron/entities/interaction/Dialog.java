package fr.ensim.lemeeherbron.entities.interaction;

public class Dialog {

    private String currentMessage;
    private Dialog nextDialog = null;

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
}
