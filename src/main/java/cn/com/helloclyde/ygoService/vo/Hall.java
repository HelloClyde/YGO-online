package cn.com.helloclyde.ygoService.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class Hall {
    private static volatile List<Room> rooms = new ArrayList<>();

    static {
        for (int i = 0; i < 60; i++) {
            Hall.rooms.add(new Room());
        }
    }

    public static List<Room> getRooms() {
        return rooms;
    }

    private static void setRooms(List<Room> rooms) {
        Hall.rooms = rooms;
    }

}
