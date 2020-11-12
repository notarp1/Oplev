package DAL.Interfaces;

import DTO.ChatDTO;

public interface IChatDAO {

    IChatDAO getChat(int chatId);
    void createChat(ChatDTO chat);
    void updateChat(ChatDTO chat);
    void deleteChat(int chatId);
}
