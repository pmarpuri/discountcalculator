package ae.pmarpuri.discountcalculator.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ae.pmarpuri.discountcalculator.model.User;
import ae.pmarpuri.discountcalculator.model.UserType;
import ae.pmarpuri.discountcalculator.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> saveUsers(List<User> users) {
		return userRepository.saveAll(users);
	}
	
	public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

	public double getDiscountPercentage(User user) {
		log.info("getDiscountPercentage execution started");
		double discount = 0;
		if (UserType.EMPLOYEE.equals(user.getType())) {
			discount = 0.30;
		} else if (UserType.AFFILIATE.equals(user.getType())) {
			discount = 0.10;
		} else if (UserType.CUSTOMER.equals(user.getType())) {
			Period userPeriod = Period.between(user.getRegistrationDate(), LocalDate.now());
			if (userPeriod.getYears() > 2) {
				discount = 0.05;
			}
		}
		log.info(String.format("getDiscountPercentage execution completed, discount: %.2f for the user type: %s",
				discount, user.getType()));
		return discount;
	}
}
