package DAL.Interfaces;

import java.util.List;

import DTO.EventDTO;

public interface CallBackEventList {
    public void onCallback(List<EventDTO> events);
}
