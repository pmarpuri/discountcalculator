package ae.pmarpuri.discountcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Item {
	private String category;
	private String name;
	private int quantity;
	private double pricePerUnit;
	private double totalPrice;
	private double discountPrice;
	private double discountPercentage;
	private double priceAfterDiscount;
}
