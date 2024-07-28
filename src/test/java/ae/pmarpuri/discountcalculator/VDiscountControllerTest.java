package ae.pmarpuri.discountcalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ae.pmarpuri.discountcalculator.controller.DiscountController;
import ae.pmarpuri.discountcalculator.model.BillDetails;
import ae.pmarpuri.discountcalculator.model.Item;
import ae.pmarpuri.discountcalculator.model.User;
import ae.pmarpuri.discountcalculator.model.UserType;
import ae.pmarpuri.discountcalculator.repository.UserRepository;
import ae.pmarpuri.discountcalculator.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class VDiscountControllerTest {

	@Mock
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private DiscountController discountController;

	private List<User> userList;

	@BeforeEach
	public void setUp() {

		MockitoAnnotations.openMocks(this);

		userList = Arrays.asList(new User(1L, "Purushothma", UserType.EMPLOYEE, LocalDate.now()),
				new User(2L, "Hrutvik", UserType.AFFILIATE, LocalDate.of(2020, 1, 15)),
				new User(3L, "Ram", UserType.CUSTOMER, LocalDate.of(2019, 7, 23)));
		
		when(userRepository.findById(anyLong())).thenAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
		});
		
		when(userService.getUserById(anyLong())).thenAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
		});
	}

	@Test
	void testDiscountOnBill() throws Exception {

		Long userId = 1L;
		User user = new User(userId, "Purushothma", UserType.EMPLOYEE, LocalDate.now());
		when(userService.getUserById(userId)).thenReturn(Optional.of(user));
		when(userService.getDiscountPercentage(user)).thenReturn(0.30); // 30% discount'

		String disreq = "{\"userId\": \"1\",\"items\": [{\"category\": \"grocery\", \"name\": \"Milk\",\"quantity\": 2,\"pricePerUnit\": 1.5,\"totalPrice\": 3}, {\"category\": \"cleaning supplies\", \"name\": \"Dish Soap\", \"quantity\": 3, \"pricePerUnit\": 100, \"totalPrice\": 300}]}";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/discount/getDiscount").content(disreq)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		System.out.println("responseContent::" + responseContent);
		BillDetails billDetails = objectMapper.readValue(responseContent, new TypeReference<BillDetails>() {
		});

		// Perform validations
		assertNotNull(billDetails);
		ArrayList<Item> items = billDetails.getItems();
		assertEquals(2, items.size());

		// Validate the bill details
		assertEquals(303.0, billDetails.getTotalAmount());
		assertEquals(90.0, billDetails.getTotalAmountPercentageBasedDiscount());
		assertEquals(15.0, billDetails.getTotalAmountGeneralBasedDiscount());
		assertEquals(198.0, billDetails.getFinalBillAmount());

		// Validate the first itemmm m
		Item firstItem = items.get(0);
		assertEquals("grocery", firstItem.getCategory());
		assertEquals("Milk", firstItem.getName());
		assertEquals(2, firstItem.getQuantity());
		assertEquals(1.5, firstItem.getPricePerUnit());
		assertEquals(0.0, firstItem.getDiscountPrice());
		assertEquals(0.0, firstItem.getDiscountPercentage());
		assertEquals(0.0, firstItem.getPriceAfterDiscount());

		// Validate the second item
		Item secondItem = items.get(1);
		assertEquals("cleaning supplies", secondItem.getCategory());
		assertEquals("Dish Soap", secondItem.getName());
		assertEquals(3, secondItem.getQuantity());
		assertEquals(100, secondItem.getPricePerUnit());
		assertEquals(300, secondItem.getTotalPrice());
		assertEquals(90.0, secondItem.getDiscountPrice());
		assertEquals(0.3, secondItem.getDiscountPercentage());
		assertEquals(210.0, secondItem.getPriceAfterDiscount());

	}

}
