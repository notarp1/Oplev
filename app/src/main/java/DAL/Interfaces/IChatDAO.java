package DAL.Interfaces;

import DTO.ChatDTO;

public interface IChatDAO {

    ChatDTO getChat(String chatId);
    void createChat(ChatDTO chat);
    void updateChat(ChatDTO chat);
    void deleteChat(String chatId);
}
