package com.mutualfund.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mutualfund.model.MutualFund;
import com.mutualfund.serviceimpl.MutualFundService;


@Component
@RestController
public class MutualFundController {
@Autowired
MutualFundService service;

Logger logger = LoggerFactory.getLogger(MutualFundController.class);
  
//add mutualfund account
  
@PostMapping("/addMutualFunds")
public ResponseEntity<MutualFund> addhospital(@RequestBody MutualFund mutualfund) {
MutualFund fundDetails = service.addMutualFundDetails(mutualfund);
return new ResponseEntity<MutualFund>(fundDetails, HttpStatus.OK);
}

//get mutualfund account by PANno

@GetMapping(value = "/getFundDetails/{panNo}", produces = "application/json")
public List<MutualFund> getAccountDetail(@PathVariable String panNo) {
List<MutualFund> funds = service.getDetail(panNo);
if (funds.isEmpty()) {
throw new RuntimeException("No details found ");
} else {
logger.info("Response Executed");
}
return funds;
}

//get investment details using PANno and fundId
  
@GetMapping(value = "/getInvestmentDetails/{panNo}/{fundId}", produces = "application/json")
public List<MutualFund> findTransactionDetails(@PathVariable String panNo, @PathVariable Integer fundId) {
List<MutualFund> fundDetails = service.getTransactionDetail(panNo, fundId);
if (fundDetails.isEmpty()) {
throw new RuntimeException("No details found ");
} else {
logger.info("Response Executed");
}
return fundDetails;
}

}

