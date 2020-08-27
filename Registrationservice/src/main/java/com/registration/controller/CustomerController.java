package com.registration.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.registration.constants.Constants;
import com.registration.model.Account;
import com.registration.model.ApplicationUser;
import com.registration.model.MutualFund;
import com.registration.serviceimpl.CustomerServiceImplementation;

@RestController
@RequestMapping("/users")
public class CustomerController {

	@Autowired
	CustomerServiceImplementation customerService;
	@Autowired
	ApplicationUser users;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	Account account;

	Logger logger = LoggerFactory.getLogger(CustomerController.class);

//user registration

	@PostMapping(value = "/sign-up", produces = "application/json")
	public ApplicationUser createUser(@Valid @RequestBody ApplicationUser user) 
	{
		ApplicationUser details = customerService.createUser(user);
		return details;
	}
	
//get userdetails using PAN no
	
    @GetMapping(value = "/getUser/{panNo}", produces = "application/json")
	public ApplicationUser getUser(@PathVariable("panNo") String panNo) 
    {
		ApplicationUser user = customerService.getUserByPan(panNo);
		return user;
	}

//adding account

	@PostMapping(value = "/AddAccount", produces = "application/json")
	public Object createAccount(@Valid @RequestBody Account user) 
	{
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Object responseEntity = restTemplate.postForObject(Constants.AddAccountUrl, user, Object.class,users.getPanNo()); 
		return responseEntity;
	}

//adding mutual fund

	@PostMapping(value = "/AddMutualFund", produces = "application/json")
	public MutualFund createMutualFund(@Valid @RequestBody MutualFund fund)
	{
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		MutualFund responseEntity = restTemplate.postForObject(Constants.AddMutualFundUrl, fund,
		MutualFund.class, users.getPanNo());
		return responseEntity;
	}

//get account details with pan

	@GetMapping(value = "/getBypanNo/{panNo}", produces = "application/json")
	@HystrixCommand(fallbackMethod = "getFallbackCatalog", threadPoolProperties =
    {
	@HystrixProperty(name = "coreSize", value = "99") })
	    public ResponseEntity<String> getUserById(@PathVariable("panNo") String panNo) 
		{
		HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(50000000);
		final String Url = "http://localhost:6579/get/{panNo}";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> user =restTemplate.getForEntity(Url, String.class, panNo);
		return user;
	}

//fallback method

	public ResponseEntity<String> getFallbackCatalog(@PathVariable("panNo") String panNo) {

		return (new ResponseEntity<String>("Something went wrong!!! Please try After Sometime", HttpStatus.BAD_REQUEST));
	}

//get investment details

	@GetMapping(value = "/getInvestmentDetails/{panNo}/{fundId}", produces = "application/json")
	public String getTransactionDetails(@PathVariable("panNo") String panNo, @PathVariable("fundId") Integer fundId) 
	{
		RestTemplate restTemplate = new RestTemplate();
		String user = restTemplate.getForObject(Constants.GetInvestmentDetailsUrl, String.class, panNo, fundId);
		return user;
	}
	
//get all accounts

	@GetMapping(value = "/getAllAccounts", produces = "application/json")
	public ResponseEntity<String> getAllAccountDetails() {
		HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(50000000);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> user = restTemplate.getForEntity(Constants.GetAllAccountsUrl, String.class);
		return user;
	}

//delete account

	@DeleteMapping("/deleteAccount")
	public ResponseEntity<HttpStatus> deleteAccount(String panNo)
	{
		Map<String, String> params=new HashMap<String,String>();
		params.put(panNo, panNo);
		restTemplate.delete(Constants.DeleteAccountUrl,params);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK); 
		
	}
		


}