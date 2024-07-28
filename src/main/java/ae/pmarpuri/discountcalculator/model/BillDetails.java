package ae.pmarpuri.discountcalculator.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BillDetails {
	private Long userId;
	private ArrayList<Item> items;
	private double totalAmount;
	private double totalAmountPercentageBasedDiscount;
	private double totalAmountGeneralBasedDiscount;
	private double finalBillAmount;
}
