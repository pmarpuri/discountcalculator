package ae.pmarpuri.discountcalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ae.pmarpuri.discountcalculator.model.User;
import ae.pmarpuri.discountcalculator.model.UserType;
import ae.pmarpuri.discountcalculator.repository.UserRepository;
import ae.pmarpuri.discountcalculator.service.UserService;

@SpringBootTest
class UserServiceTests {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

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

		when(userRepository.saveAll(any(List.class))).thenReturn(userList);
	}

	@Test
	void testSaveUsers() {
		List<User> savedUsers = userService.saveUsers(userList);
		assertEquals(userList.size(), savedUsers.size());
		assertEquals("Purushothma", savedUsers.get(0).getName());
		assertEquals(UserType.EMPLOYEE, savedUsers.get(0).getType());
	}

	@Test
	void testGetUserById() {
		Long userId = 1L;
		User user = new User(userId, "Purushothma", UserType.EMPLOYEE, LocalDate.now());
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		Optional<User> foundUser = userService.getUserById(userId);

		assertTrue(foundUser.isPresent());
		assertEquals("Purushothma", foundUser.get().getName());
		assertEquals(UserType.EMPLOYEE, foundUser.get().getType());

		verify(userRepository, times(1)).findById(userId);
	}

	@Test
	void testEmployeeDiscount() {
		double calculateDiscount = userService
				.getDiscountPercentage(new User(1L, "Purushothma", UserType.EMPLOYEE, LocalDate.now()));
		assertEquals(0.30, calculateDiscount);
	}

	@Test
	void testAffiliateDiscount() {
		double calculateDiscount = userService
				.getDiscountPercentage(new User(4L, "Mokshagna", UserType.AFFILIATE, LocalDate.now()));
		assertEquals(0.10, calculateDiscount);

	}

	@Test
	void testCustomerDiscountMoreThanTwoYears() {
		double calculateDiscount = userService
				.getDiscountPercentage(new User(3L, "Ram", UserType.CUSTOMER, LocalDate.of(2019, 7, 23)));
		assertEquals(0.05, calculateDiscount);
	}

	@Test
	void testCustomerDiscountLessThanTwoYears() {
		double calculateDiscount2 = userService
				.getDiscountPercentage(new User(8L, "Alexa", UserType.CUSTOMER, LocalDate.now()));
		assertEquals(0, calculateDiscount2);
	}
}
