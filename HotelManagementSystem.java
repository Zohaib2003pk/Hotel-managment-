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
    
    // --- Reservation Class ---
    public static class Reservation {
        private final String reservationId;
        private final Customer customer;
        private final Room room;
        private final Date checkIn;
        private final Date checkOut;
        private boolean checkedIn;
        
        public Reservation(Customer customer, Room room, Date checkIn, Date checkOut) {
            this.reservationId = UUID.randomUUID().toString().substring(0, 8);
            this.customer = customer;
            this.room = room;
            this.checkIn = new Date(checkIn.getTime());
            this.checkOut = new Date(checkOut.getTime());
            this.checkedIn = false;
        }
        
        public boolean overlaps(Date start, Date end) {
            return !checkOut.before(start) && !checkIn.after(end);
        }
        
        public boolean checkIn() {
            if (checkedIn) return false;
            checkedIn = room.checkIn(customer.getGuestInfo());
            return checkedIn;
        }
        
        public double calculateTotalCost() {
            long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
            return nights * room.getPricePerNight();
        }
        
        // Getters
        public String getId() { return reservationId; }
        public Customer getCustomer() { return customer; }
        public Room getRoom() { return room; }
        public Date getCheckIn() { return new Date(checkIn.getTime()); }
        public Date getCheckOut() { return new Date(checkOut.getTime()); }
    }
    
    // --- Customer Class ---
    public static class Customer {
        private final String customerId;
        private final Name name;
        private final Address address;
        private final CreditCard paymentMethod;
        private final List<Reservation> reservations;
        
        public Customer(Name name, Address address, CreditCard paymentMethod) {
            this.customerId = UUID.randomUUID().toString().substring(0, 8);
            this.name = name;
            this.address = address;
            this.paymentMethod = paymentMethod;
            this.reservations = new ArrayList<>();
        }
        
        public void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }
        
        public Guest getGuestInfo() {
            return new Guest(name, address);
        }
        
        // Getters
        public String getId() { return customerId; }
        public Name getName() { return name; }
    }
    
    // --- Guest Class (Simpler) ---
    public record Guest(Name name, Address address) {}
    
    // --- Main Method ---
    public static void main(String[] args) {
        try {
            // 1. Create Hotel
            Hotel hotel = new Hotel("Grand Palace");
            
            // 2. Add Rooms
            hotel.addRoom(new Room(101, RoomType.DELUXE, 250.00));
            hotel.addRoom(new Room(102, RoomType.DELUXE, 250.00));
            hotel.addRoom(new Room(201, RoomType.SINGLE, 120.00));
            hotel.addRoom(new Room(202, RoomType.DOUBLE, 180.00));
            
            // 3. Create Customer
            Customer customer = new Customer(
                new Name("John", "Doe"),
                new Address("123 Maple St", "New York", "10001"),
                new CreditCard("1234-5678-9012-3456", "12/28")
            );
            
            // 4. Make Reservation
            Calendar cal = Calendar.getInstance();
            Date checkIn = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, 3);
            Date checkOut = cal.getTime();
            
            Reservation reservation = hotel.makeReservation(customer, checkIn, checkOut, RoomType.DELUXE);
            customer.addReservation(reservation);
            
            System.out.println("Reservation created successfully!");
            System.out.println("Reservation ID: " + reservation.getId());
            System.out.println("Total cost: $" + reservation.calculateTotalCost());
            
            // 5. Check in
            if (hotel.checkIn(reservation)) {
                System.out.println("Customer checked in successfully!");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}