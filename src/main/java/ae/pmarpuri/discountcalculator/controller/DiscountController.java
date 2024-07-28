package ae.pmarpuri.discountcalculator.controller;


import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ae.pmarpuri.discountcalculator.model.BillDetails;
import ae.pmarpuri.discountcalculator.service.BillService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/discount")
@Slf4j
public class DiscountController {

	private BillService billService;

	public DiscountController(BillService billService) {
		this.billService = billService;
	}

	@PostMapping("/getDiscount")
	public ResponseEntity<BillDetails> getDiscountOnBill(@RequestBody BillDetails billDetails) {
		log.info("getDiscountOnBill execution started");
		return new ResponseEntity<>(billService.calculateDiscount(billDetails), HttpStatusCode.valueOf(200));
	}
}
