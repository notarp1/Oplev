package Controller;

public class ChatController {
    private static ChatController instance = null;


    private ChatController(){


        this.instance = this;

    }

    public static ChatController getInstance(){
        if (instance == null) instance = new ChatController();
        return instance;
    }


}
