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
                private Room findAvailableRoom(Date checkIn, Date checkOut, RoomType roomType) {
            for (Room room : rooms) {
                if (room.getType() == roomType && room.isAvailable(checkIn, checkOut)) {
                    return room;
                }
            }
            return null;
        }
        
        public boolean checkIn(Reservation reservation) {
            return reservation.checkIn();
        }
        
        public void checkOut(Room room) {
            room.checkOut();
        }
    }
    
    // --- Room Class ---
    public static class Room {
        private final int roomNumber;
        private final RoomType type;
        private final double pricePerNight;
        private Guest currentGuest;
        private final List<Reservation> reservations;
        
        public Room(int roomNumber, RoomType type, double pricePerNight) {
            this.roomNumber = roomNumber;
            this.type = type;
            this.pricePerNight = pricePerNight;
            this.reservations = new ArrayList<>();
        }
        
        public boolean isAvailable(Date checkIn, Date checkOut) {
            for (Reservation res : reservations) {
                if (res.overlaps(checkIn, checkOut)) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean checkIn(Guest guest) {
            if (currentGuest != null) return false;
            this.currentGuest = guest;
            return true;
        }
        
        public void checkOut() {
            this.currentGuest = null;
        }
        
        public int getRoomNumber() { return roomNumber; }
        public RoomType getType() { return type; }
        public double getPricePerNight() { return pricePerNight; }
        public boolean isOccupied() { return currentGuest != null; }
    }


