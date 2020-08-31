package com.banking.controller;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
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

import com.banking.model.Account;
import com.banking.serviceimpl.BankAccountService;

@Component
@RestController
public class BankAccountController 
{
	@Autowired
	BankAccountService accountservice;
		Logger logger = LoggerFactory.getLogger(BankAccountController.class);
	
	@PostMapping("/addAccount")
	public ResponseEntity<Account> addAccount(@Valid @RequestBody Account account)
	{
		Account accountdetails = accountservice.addAccountDetails(account);
		return new ResponseEntity<Account>(accountdetails, HttpStatus.OK);
    }

// get account by PANno

	@GetMapping(value = "/get/{panNo}", produces = "application/json")
	public ResponseEntity<List<Account>>getDetail(@PathVariable String panNo)
	{
		List<Account> account=new ArrayList<Account>();
		account=accountservice.getDetail(panNo);
		if (account.isEmpty())
		{
			throw new RuntimeException("No Accountfound");
		} 
		else
		{
			logger.info("Response Successfully Executed");
		}
			return new ResponseEntity<List<Account>>(account,HttpStatus.OK);
	 }

// get all accounts

	@GetMapping("/getAll")
	List<Account> getAccountDetail()
	{
		List<Account> account = accountservice.getAccount();
		if (account.isEmpty()) 
		{
			throw new RuntimeException("No Account found");
		} 
		else 
		{
			logger.info(" details found");
		}
		return account;
	    }
}

