//package module;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FietsTest {
//    @Test
//    public void testMerkEnTypeBijElkaarHoren() {
//        Fiets fiets = new Fiets("1", "Gazelle", "City Bike", "500", "15 kg", "7", "V-brakes", "Stadsfiets met comfortabel rijgedrag", "afbeelding.jpg", "28 inch", "53 cm", "Staal", "Verende voorvork", "LED-verlichting", "Geen bagagedrager", "Ringslot", "https://www.example.com/fiets1");
//
//        assertEquals("Gazelle", fiets.getMerk());
//        assertEquals("City Bike", fiets.getType());
//    }
//    @Test
//    public void alsPrijsNegatiefNietInstellen() {
//        Fiets fiets = new Fiets("1", "Gazelle", "City Bike", "500", "15 kg", "7", "V-brakes", "Stadsfiets met comfortabel rijgedrag", "afbeelding.jpg", "28 inch", "53 cm", "Staal", "Verende voorvork", "LED-verlichting", "Geen bagagedrager", "Ringslot", "https://www.example.com/fiets1");
//
//        String positievePrijs = "500.0";
//        fiets.setPrijs(positievePrijs);
//        assertEquals(positievePrijs, fiets.getPrijs());
//
//        String negatievePrijs = "-100.0";
//        fiets.setPrijs(negatievePrijs);
//        assertEquals(positievePrijs, fiets.getPrijs());
//    }
//}