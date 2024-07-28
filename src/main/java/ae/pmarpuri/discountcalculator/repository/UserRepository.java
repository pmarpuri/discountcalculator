package ae.pmarpuri.discountcalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ae.pmarpuri.discountcalculator.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
