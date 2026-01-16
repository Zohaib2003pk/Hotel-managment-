package SC;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Test;

public class HotelManagementSystemTest {

	@Test
    public void testHotelCreation() {
        HotelManagementSystem.Hotel hotel =
                new HotelManagementSystem.Hotel("Grand Palace");
        assertNotNull(hotel);
    }
	@Test
    public void testAddRoom() {
        HotelManagementSystem.Hotel hotel =
                new HotelManagementSystem.Hotel("Grand Palace");
        HotelManagementSystem.Room room =
                new HotelManagementSystem.Room(101,
                        HotelManagementSystem.RoomType.DELUXE, 250.0);

        hotel.addRoom(room);
        assertEquals(101, room.getRoomNumber());
    }
