import java.util.*;

public class HotelManagementSystem {
    
    // --- Enums ---
    public enum RoomType { SINGLE, DOUBLE, SUITE, DELUXE }
    
    // --- Records (Data Classes) ---
    public record Money(double amount, String currency) {}
    public record Address(String street, String city, String zipCode) {}
    public record Name(String firstName, String lastName) {}
    public record CreditCard(String cardNumber, String expiryDate) {}
    public record CustomerId(String id, String idType) {}

    
    // --- Main Hotel Class ---
    public static class Hotel {
        private final String name;
        private final List<Room> rooms;
        private final List<Reservation> reservations;
        
        public Hotel(String name) {
            this.name = Objects.requireNonNull(name, "Hotel name cannot be null");
            this.rooms = new ArrayList<>();
            this.reservations = new ArrayList<>();
        }
        
        public void addRoom(Room room) {
            rooms.add(room);
        }
        
        public Reservation makeReservation(Customer customer, Date checkIn, Date checkOut, RoomType roomType) {
            Room availableRoom = findAvailableRoom(checkIn, checkOut, roomType);
            
            if (availableRoom == null) {
                throw new IllegalStateException("No available rooms of type " + roomType);
            }
            
            Reservation reservation = new Reservation(customer, availableRoom, checkIn, checkOut);
            reservations.add(reservation);
            return reservation;
        }
    

