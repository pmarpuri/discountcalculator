package ae.pmarpuri.discountcalculator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ae.pmarpuri.discountcalculator.model.User;
import ae.pmarpuri.discountcalculator.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/saveUsers")
	public String saveUsers(@RequestBody List<User> users) {
		log.info("saveUsers execution started");
		userService.saveUsers(users);
		log.info("Users are saveed in to the db successfully");
		return "Users have been added successfully!";
	}
}
