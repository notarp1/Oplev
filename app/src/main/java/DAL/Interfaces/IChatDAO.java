package DAL.Interfaces;

import DAL.Classes.ChatDAO;
import DTO.ChatDTO;

public interface IChatDAO {

    ChatDTO getChat(String chatId);
    void createChat(ChatDTO chat);
    void updateChat(ChatDAO.FirestoreCallback firestoreCallback, ChatDTO chat);
    void deleteChat(String chatId);
    void readChat(ChatDAO.FirestoreCallback firestoreCallback, String chatId);
}
