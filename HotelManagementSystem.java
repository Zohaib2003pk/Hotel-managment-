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
    
