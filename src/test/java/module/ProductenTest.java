package module;

import model.Accessoires;
import model.Fiets;
import model.Producten;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductenTest {

    private Producten producten = Producten.getProduct();

    @Test
    public void testGetAllProducts() {
        List<Fiets> alleProducten = producten.getAllProducts();
        assertEquals(14, alleProducten.size());
    }

    @Test
    public void testGetFietsByType() {
        String fietsType = "Mountainbike";
        List<Fiets> fietsen = producten.getFietsByType(fietsType);

        assertEquals(4, fietsen.size());
    }

    @Test
    public void testGetAllAccessoires() {
        List<Accessoires> accessoires = producten.getAllAccessoires();
        assertEquals(9, accessoires.size());
    }

}
