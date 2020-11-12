package DAL;

import com.google.firebase.firestore.FirebaseFirestore;

public class DBAccess {
    static DBAccess instance = null;

    public FirebaseFirestore db;

    private DBAccess DBAccess(){
        this.db = FirebaseFirestore.getInstance();
        return this;

    }

    public static DBAccess getInstance() {
        if(instance == null) instance = new DBAccess();
        return instance;
    }
}



