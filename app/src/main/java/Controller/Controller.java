package Controller;

public class Controller {
    private Controller instance;

    private Controller(){
        this.instance = this;
    }


    public Controller getInstance(){
        if (instance == null) instance = new Controller();
        return instance;
    }

}
