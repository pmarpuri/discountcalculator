package ae.pmarpuri.discountcalculator.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ae.pmarpuri.discountcalculator.model.BillDetails;
import ae.pmarpuri.discountcalculator.model.Item;
import ae.pmarpuri.discountcalculator.model.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillService {

	private UserService userService;

	public BillService(UserService userService) {
		this.userService = userService;
	}

	public BillDetails calculateDiscount(BillDetails billDetails) {
		log.info("calculateDiscount execution started");
		Optional<User> findById = userService.getUserById(billDetails.getUserId());
		if (findById.isPresent()) {
			double discount = userService.getDiscountPercentage(findById.get());
			ArrayList<Item> items = billDetails.getItems();
			for (Iterator<Item> it = items.iterator(); it.hasNext();) {
				Item item = it.next();		
				if (!"grocery".equalsIgnoreCase(item.getCategory())) {
					this.processDiscount(discount, item);
				}
			}
			double totalAmount = items.stream().mapToDouble(Item::getTotalPrice).sum(); // total amount of all items
			billDetails.setTotalAmount(totalAmount);
			double gdp = items.stream().mapToDouble(Item::getDiscountPrice).sum(); // sum of discount price
			billDetails.setTotalAmountPercentageBasedDiscount(gdp);
			billDetails.setTotalAmountGeneralBasedDiscount((long) (totalAmount / 100) * 5.0); // a $ 5 discount on total amount
			billDetails.setFinalBillAmount(totalAmount - (billDetails.getTotalAmountPercentageBasedDiscount()
					+ billDetails.getTotalAmountGeneralBasedDiscount()));
		} else {
			log.info(String.format(": %s user id is not available hence discount not applicable",
					billDetails.getUserId()));
		}
		return billDetails;
	}

	private void processDiscount(double discount, Item item) {
		double totDiscoun = item.getTotalPrice() * discount;
		item.setPriceAfterDiscount(item.getTotalPrice());
		item.setDiscountPercentage(discount);
		item.setDiscountPrice(totDiscoun);
		item.setPriceAfterDiscount(item.getTotalPrice() - totDiscoun);
	}	
}
