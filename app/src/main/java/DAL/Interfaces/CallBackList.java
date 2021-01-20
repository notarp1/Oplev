package DAL.Interfaces;

import com.google.protobuf.Enum;

import java.util.List;

import DTO.UserDTO;

public interface CallBackList {
    void onCallback(List<String> list);
}
