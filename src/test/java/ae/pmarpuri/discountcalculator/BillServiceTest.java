package ae.pmarpuri.discountcalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
import org.springframework.boot.test.context.SpringBootTest;

import ae.pmarpuri.discountcalculator.model.BillDetails;
import ae.pmarpuri.discountcalculator.model.Item;
import ae.pmarpuri.discountcalculator.model.User;
import ae.pmarpuri.discountcalculator.model.UserType;
import ae.pmarpuri.discountcalculator.repository.UserRepository;
import ae.pmarpuri.discountcalculator.service.BillService;
import ae.pmarpuri.discountcalculator.service.UserService;

@SpringBootTest
class BillServiceTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private BillService billService;

	@Mock
	private UserRepository userRepository;

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
	}

	@Test
	void testCategoryElectronics() {
		Long userId = 1L;
		User user = new User(userId, "Purushothma", UserType.EMPLOYEE, LocalDate.now());
		when(userService.getUserById(userId)).thenReturn(Optional.of(user));

		when(userService.getDiscountPercentage(user)).thenReturn(0.30); // 30% discount'
		
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item("electronics", "Headphones", 1, 25, 25, 0, 0, 0));
		BillDetails billdetails = new BillDetails(userId, items, 0.0, 0.0, 0.0, 0.0);
		// Execute the method to be tested
		BillDetails cd = billService.calculateDiscount(billdetails);
		Item item = cd.getItems().stream().filter(t -> t.getCategory().equalsIgnoreCase("electronics")).findFirst()
				.get();
		// Verify the results
		assertEquals(7.5, item.getDiscountPrice());
		assertEquals(0.3, item.getDiscountPercentage());
		assertEquals(17.5, item.getPriceAfterDiscount());
	}

	@Test
	void testCategoryGrocery() {
		Long userId = 1L;
		User user = new User(3L, "Ram", UserType.CUSTOMER, LocalDate.of(2019, 7, 23));
		when(userService.getUserById(userId)).thenReturn(Optional.of(user));

		when(userService.getDiscountPercentage(user)).thenReturn(0.0); // 0% discount

		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item("grocery", "Milk", 2, 1.5, 3, 0, 0, 0));
		BillDetails billdetails = new BillDetails(userId, items, 0.0, 0.0, 0.0, 0.0);
		// Execute the method to be tested
		BillDetails cd = billService.calculateDiscount(billdetails);
		Item item = cd.getItems().stream().filter(t -> t.getCategory().equalsIgnoreCase("grocery")).findFirst().get();
		// Verify the results
		assertEquals(0.0, item.getDiscountPrice());
		assertEquals(0.0, item.getDiscountPercentage());
		assertEquals(0.0, item.getPriceAfterDiscount());
	}
}
