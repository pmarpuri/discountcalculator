package ae.pmarpuri.discountcalculator;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class DiscountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testDiscountOnBill()  throws Exception {
		String disreq = "{\"userId\": \"1\",\"items\": [{\"category\": \"grocery\", \"name\": \"Milk\",\"quantity\": 2,\"pricePerUnit\": 1.5,\"totalPrice\": 3}, {\"category\": \"cleaning supplies\", \"name\": \"Dish Soap\", \"quantity\": 3, \"pricePerUnit\": 100, \"totalPrice\": 300}]}";		 
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/discount/getDiscount").content(disreq).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
			System.out.println(result.getResponse());		 
	}

}
