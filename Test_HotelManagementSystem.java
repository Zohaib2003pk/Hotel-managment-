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
@Test
    public void testCreateCustomer() {
        HotelManagementSystem.Customer customer =
                new HotelManagementSystem.Customer(
                        new HotelManagementSystem.Name("John", "Doe"),
                        new HotelManagementSystem.Address("Street", "City", "12345"),
                        new HotelManagementSystem.CreditCard("1111", "12/30")
                );

        assertNotNull(customer);
        assertNotNull(customer.getId());
    }
		@Test
    public void testMakeReservation() {
        HotelManagementSystem.Hotel hotel =
                new HotelManagementSystem.Hotel("Grand Palace");

        hotel.addRoom(new HotelManagementSystem.Room(
                101, HotelManagementSystem.RoomType.SINGLE, 100.0));

        HotelManagementSystem.Customer customer =
                new HotelManagementSystem.Customer(
                        new HotelManagementSystem.Name("Ali", "Khan"),
                        new HotelManagementSystem.Address("A", "B", "C"),
                        new HotelManagementSystem.CreditCard("2222", "11/29")
                );
        Date today = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        java.util.Date later = cal.getTime();

        HotelManagementSystem.Reservation reservation =
                hotel.makeReservation(customer, today, later,
                        HotelManagementSystem.RoomType.SINGLE);

        assertNotNull(reservation);
    }
}
