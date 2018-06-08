package com.lmh.soap;

import com.lmh.search_ws.GetBillsListRequest;
import com.lmh.search_ws.GetBillsListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class BillsListEndpoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(BillsListEndpoint.class);
	private static final String NAMESPACE_URI = "http://lmh.com/search-ws";
	private BillsListRepository repository;

	@Autowired
	public BillsListEndpoint(BillsListRepository billsListRepository) {
		this.repository = billsListRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBillsListRequest")
	@ResponsePayload
	public GetBillsListResponse getBillsList(@RequestPayload GetBillsListRequest request) {
		LOGGER.info("Bills list request processing");
		GetBillsListResponse response = new GetBillsListResponse();
		if (request.getRepositoryName() == null) {
			LOGGER.info("Enter mandatory field values");
		} else if ((request.getCAccountNo() == null) && (request.getCMobileNo() == null)
				&& (request.getCCustomerName() == null) && (request.getCBillDate() == null)) {
			LOGGER.info("Entered databaseName is: {}", request.getRepositoryName());
			response.setBillsList(this.repository.findBillsListByDatabase(request.getRepositoryName()));
		} else if (request.getCBillDate() == null) {

			/*
			 * if (request.getCAccountNo() != null && request.getCMobileNo() != null) {
			 * LOGGER.
			 * info("Entered databaseName :{} and accountNumber :{} and mobileNumber :{}",
			 * request.getRepositoryName(), request.getCAccountNo(),
			 * request.getCMobileNo()); response.setBillsList(
			 * this.repository.findBillsList(request.getRepositoryName(),
			 * request.getCAccountNo()));
			 * 
			 * } else
			 */
			if (request.getCMobileNo() == null && request.getCCustomerName() == null) {
				LOGGER.info("Entered databaseName :{} and accountNumber :{}", request.getRepositoryName(),
						request.getCAccountNo());
				response.setBillsList(
						this.repository.findBillsListByAccount(request.getRepositoryName(), request.getCAccountNo()));

			} else if (request.getCAccountNo() == null && request.getCCustomerName() == null) {
				LOGGER.info("Entered databaseName :{} and mobileNumber :{}", request.getRepositoryName(),
						request.getCMobileNo());
				response.setBillsList(
						this.repository.findBillsListByMobile(request.getRepositoryName(), request.getCMobileNo()));
			} else if (request.getCAccountNo() == null && request.getCMobileNo() == null) {
				LOGGER.info("Entered databaseName :{} and customerName :{}", request.getRepositoryName(),
						request.getCCustomerName());
				response.setBillsList(
						this.repository.findBillsListByName(request.getRepositoryName(), request.getCCustomerName()));
			}

		} else {
			if (request.getCMobileNo() == null && request.getCCustomerName() == null) {
				LOGGER.info("Entered fields values");
				LOGGER.info("Repository:{} | Account_number:{} |Bill_date:{}",request.getRepositoryName(),
						request.getCAccountNo(), request.getCBillDate());
				response.setBillsList(this.repository.findBillsListByAccountDate(request.getRepositoryName(),
						request.getCAccountNo(), request.getCBillDate()));
			} else if (request.getCAccountNo() == null && request.getCCustomerName() == null) {
				LOGGER.info("Entered fields values");
				LOGGER.info("Repository:{} | Mobile_number:{} |Bill_date:{}",request.getRepositoryName(),
						request.getCMobileNo(), request.getCBillDate());
				response.setBillsList(this.repository.findBillsListByMobileDate(request.getRepositoryName(),
						request.getCMobileNo(), request.getCBillDate()));
			} else if (request.getCAccountNo() == null && request.getCMobileNo() == null) {
				LOGGER.info("Entered fields values");
				LOGGER.info("Repository:{} | Customer_name:{} |Bill_date:{}",request.getRepositoryName(),
						request.getCCustomerName(), request.getCBillDate());
				response.setBillsList(this.repository.findBillsListByNameDate(request.getRepositoryName(),
						request.getCCustomerName(), request.getCBillDate()));
			}else {
				if(request.getCAccountNo() != null) {
					response.setBillsList(this.repository.findBillsListByAccountDate(request.getRepositoryName(),
							request.getCAccountNo(), request.getCBillDate()));
				}else if(request.getCMobileNo() != null) {
					response.setBillsList(this.repository.findBillsListByMobileDate(request.getRepositoryName(),
							request.getCMobileNo(), request.getCBillDate()));
				}else {
					response.setBillsList(this.repository.findBillsListByNameDate(request.getRepositoryName(),
							request.getCCustomerName(), request.getCBillDate()));
				}
			}
		}
		return response;
	}
}
