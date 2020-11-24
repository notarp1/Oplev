package DAL.Interfaces;

import DAL.Classes.ChatDAO;
import DTO.ChatDTO;

public interface IChatDAO {
    void createChat(ChatDTO chat);
    void updateChat(ChatDAO.FirestoreCallback firestoreCallback, ChatDTO chat);
    void deleteChat(ChatDAO.FirestoreCallback firestoreCallback, String chatId);
    void readChat(ChatDAO.FirestoreCallback firestoreCallback, String chatId);
}
