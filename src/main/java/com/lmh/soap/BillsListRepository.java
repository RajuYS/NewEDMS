package com.lmh.soap;

import com.g1.e2.vault.*;
import com.pb.vault.exception.VaultBaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.g1.e2.vault.VaultClient.Account;
import com.g1.e2.vault.VaultClient.Document;
import com.lmh.search_ws.BillsList;

@Component
public class BillsListRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(BillsListRepository.class.getName());

	public List<BillsList> findBillsListByDatabase(String database) {
		List<BillsList> bLists = new ArrayList<>();

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get All the Documents for the Account
			// List<Document> documents = account.getAllDocuments();
			List<Account> accounts = db.getAllAccounts();
			LOGGER.info("==== Bills list with the database ====");
			for (Account account : accounts) {
				List<Document> documents = account.getAllDocuments();

				for (Document document : documents) {
					BillsList b1 = new BillsList();

					String[] nameArray = document.getFile().split("-");
					String fileName = "";
					for (int i = 0; i < nameArray.length; i++) {
						if (i == 0) {
							fileName = nameArray[i];
						} else {
							if (i != nameArray.length - 1) {
								fileName = fileName + "_" + nameArray[i];
							} else {
								fileName = fileName + "." + nameArray[i];
							}
						}
					}

					// String docName = document.getFile().substring(9, 18) + "_" +
					// document.getFile().substring(19, 25)+ ".pdf";
					b1.setObjectName(fileName);
					b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
					b1.setResponseDate(String.valueOf(Instant.now()));
					LOGGER.info("Bill-Name:" + fileName + ",ID:"
							+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0) + ",ResponseDate:"
							+ b1.getResponseDate());
					bLists.add(b1);
				}
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException | VaultException
				| TooManyResultsException | ServerLoopingException e) {
			LOGGER.error("Error:{}", e);
		}

		return bLists;

	}

	public List<BillsList> findBillsListByAccount(String database, String accountNumber) {

		List<BillsList> bLists = new ArrayList<>();

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get the account with specific account number
			VaultClient.Account account = db.getAccount(accountNumber);

			// Get All the Documents for the Account
			List<Document> documents = account.getAllDocuments();
			LOGGER.info("==== Bills list with the Account Number ====");
			for (Document document : documents) {
				BillsList b1 = new BillsList();
				String[] nameArray = document.getFile().split("-");
				String fileName = "";
				for (int i = 0; i < nameArray.length; i++) {
					if (i == 0) {
						fileName = nameArray[i];
					} else {
						if (i != nameArray.length - 1) {
							fileName = fileName + "_" + nameArray[i];
						} else {
							fileName = fileName + "." + nameArray[i];
						}
					}
				}

				b1.setObjectName(fileName);
				b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
				b1.setResponseDate(String.valueOf(Instant.now()));
				LOGGER.info("Bill-Name:" + fileName + ",ID:"
						+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0) + ",ResponseDate:"
						+ b1.getResponseDate());
				bLists.add(b1);
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException
				| TooManyResultsException | ServerLoopingException | VaultException e) {
			LOGGER.error("Error:" + e);
		}

		return bLists;

	}

	public List<BillsList> findBillsListByAccountDate(String database, String accountNumber, String date) {
		List<BillsList> bLists = new ArrayList<>();
		// Date Formatter
		DateTimeFormatter vformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter sformatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// Conversion specified date string to Local date format
		LocalDate originalDate = LocalDate.parse(date, sformatter);
		LocalDate compareDate = LocalDate.parse(date, sformatter).minusMonths(6);

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get the account with specific account number
			VaultClient.Account account = db.getAccount(accountNumber);

			// Get All the Documents for the Account
			List<Document> documents = account.getAllDocuments();
			LOGGER.info("==== Last 6 months documents list from specified Date ====");

			for (Document document : documents) {
				BillsList b1 = new BillsList();
				// Conversion document date to LocalDate format
				LocalDate docDate = LocalDate.parse(document.getDate(), vformatter);

				if (docDate.compareTo(compareDate) > 0 && docDate.compareTo(originalDate) < 0) {
					String[] nameArray = document.getFile().split("-");
					String fileName = "";
					for (int i = 0; i < nameArray.length; i++) {
						if (i == 0) {
							fileName = nameArray[i];
						} else {
							if (i != nameArray.length - 1) {
								fileName = fileName + "_" + nameArray[i];
							} else {
								fileName = fileName + "." + nameArray[i];
							}
						}
					}

					LOGGER.info("Bill-Name:" + fileName + ",ID:"
							+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0) + ",ResponseDate:"
							+ b1.getResponseDate());
					b1.setObjectName(fileName);
					b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
					b1.setResponseDate(String.valueOf(Instant.now()));
					bLists.add(b1);
				}
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException | VaultException
				| TooManyResultsException | ServerLoopingException e) {
			LOGGER.error("Error: {}", e);
		}

		return bLists;

	}

	public List<BillsList> findBillsListByMobile(String database, String mobileNumber) {

		List<BillsList> bLists = new ArrayList<>();

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get all accounts
			List<VaultClient.Account> accounts = db.getAllAccounts();

			LOGGER.info("==== Bills list with the mobile number ====");

			for (VaultClient.Account account : accounts) {

				// Get All the Documents for the Account
				List<Document> documents = account.getAllDocuments();

				for (Document document : documents) {

					if (document.getDocumentInfo().getCustomAttributes().get("MBL").get(0).equals(mobileNumber)) {
						BillsList b1 = new BillsList();
						String[] nameArray = document.getFile().split("-");
						String fileName = "";
						for (int i = 0; i < nameArray.length; i++) {
							if (i == 0) {
								fileName = nameArray[i];
							} else {
								if (i != nameArray.length - 1) {
									fileName = fileName + "_" + nameArray[i];
								} else {
									fileName = fileName + "." + nameArray[i];
								}
							}
						}

						b1.setObjectName(fileName);
						b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
						b1.setResponseDate(String.valueOf(Instant.now()));
						LOGGER.info("Bill-Name:" + fileName + ",ID:"
								+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0) + ",ResponseDate:"
								+ b1.getResponseDate());
						bLists.add(b1);
					}
				}
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException
				| TooManyResultsException | ServerLoopingException | VaultException e) {
			LOGGER.error("Error:" + e);
		}

		return bLists;

	}

	public List<BillsList> findBillsListByMobileDate(String database, String mobileNumber, String date) {
		List<BillsList> bLists = new ArrayList<>();
		// Date Formatter
		DateTimeFormatter vformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter sformatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// Conversion specified date string to Local date format
		LocalDate originalDate = LocalDate.parse(date, sformatter);
		LocalDate compareDate = LocalDate.parse(date, sformatter).minusMonths(6);

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get all accounts
			List<VaultClient.Account> accounts = db.getAllAccounts();

			LOGGER.info("==== Last 6 months documents list from specified Date ====");
			for (VaultClient.Account account : accounts) {

				// Get All the Documents for the Account
				List<Document> documents = account.getAllDocuments();

				for (Document document : documents) {
					if (document.getDocumentInfo().getCustomAttributes().get("MBL").get(0).equals(mobileNumber)) {
						BillsList b1 = new BillsList();
						// Conversion document date to LocalDate format
						LocalDate docDate = LocalDate.parse(document.getDate(), vformatter);

						if (docDate.compareTo(compareDate) > 0 && docDate.compareTo(originalDate) < 0) {
							String[] nameArray = document.getFile().split("-");
							String fileName = "";
							for (int i = 0; i < nameArray.length; i++) {
								if (i == 0) {
									fileName = nameArray[i];
								} else {
									if (i != nameArray.length - 1) {
										fileName = fileName + "_" + nameArray[i];
									} else {
										fileName = fileName + "." + nameArray[i];
									}
								}
							}

							LOGGER.info("Bill-Name:" + fileName + ",ID:"
									+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0)
									+ ",ResponseDate:" + b1.getResponseDate());
							b1.setObjectName(fileName);
							b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
							b1.setResponseDate(String.valueOf(Instant.now()));
							bLists.add(b1);
						}
					}
				}
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException | VaultException
				| TooManyResultsException | ServerLoopingException e) {
			LOGGER.error("Error: {}", e);
		}

		return bLists;

	}

	public List<BillsList> findBillsListByName(String database, String mobileNumber) {

		List<BillsList> bLists = new ArrayList<>();

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get all accounts
			List<VaultClient.Account> accounts = db.getAllAccounts();

			LOGGER.info("==== Bills list with Name====");

			for (VaultClient.Account account : accounts) {

				// Get All the Documents for the Account
				List<Document> documents = account.getAllDocuments();

				for (Document document : documents) {

					if (document.getDocumentInfo().getCustomAttributes().get("Name").get(0).equals(mobileNumber)) {
						BillsList b1 = new BillsList();
						String[] nameArray = document.getFile().split("-");
						String fileName = "";
						for (int i = 0; i < nameArray.length; i++) {
							if (i == 0) {
								fileName = nameArray[i];
							} else {
								if (i != nameArray.length - 1) {
									fileName = fileName + "_" + nameArray[i];
								} else {
									fileName = fileName + "." + nameArray[i];
								}
							}
						}

						b1.setObjectName(fileName);
						b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
						b1.setResponseDate(String.valueOf(Instant.now()));
						LOGGER.info("Bill-Name:" + fileName + ",ID:"
								+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0) + ",ResponseDate:"
								+ b1.getResponseDate());
						bLists.add(b1);
					}
				}
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException
				| TooManyResultsException | ServerLoopingException | VaultException e) {
			LOGGER.error("Error:" + e);
		}
		return bLists;
	}

	public List<BillsList> findBillsListByNameDate(String database, String customerName, String date) {
		List<BillsList> bLists = new ArrayList<>();
		// Date Formatter
		DateTimeFormatter vformatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter sformatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// Conversion specified date string to Local date format
		LocalDate originalDate = LocalDate.parse(date, sformatter);
		LocalDate compareDate = LocalDate.parse(date, sformatter).minusMonths(6);

		VaultClient client = new VaultClient();
		try {
			// connecting with vault
			client.connect("localhost", 6003);

			// Connecting with vault database
			VaultClient.Database db = client.getDatabase(database);

			// Get all accounts
			List<VaultClient.Account> accounts = db.getAllAccounts();

			LOGGER.info("==== Last 6 months documents list from specified Date ====");
			for (VaultClient.Account account : accounts) {

				// Get All the Documents for the Account
				List<Document> documents = account.getAllDocuments();

				for (Document document : documents) {
					if (document.getDocumentInfo().getCustomAttributes().get("Name").get(0).equals(customerName)) {
						BillsList b1 = new BillsList();
						// Conversion document date to LocalDate format
						LocalDate docDate = LocalDate.parse(document.getDate(), vformatter);

						if (docDate.compareTo(compareDate) > 0 && docDate.compareTo(originalDate) < 0) {
							String[] nameArray = document.getFile().split("-");
							String fileName = "";
							for (int i = 0; i < nameArray.length; i++) {
								if (i == 0) {
									fileName = nameArray[i];
								} else {
									if (i != nameArray.length - 1) {
										fileName = fileName + "_" + nameArray[i];
									} else {
										fileName = fileName + "." + nameArray[i];
									}
								}
							}

							LOGGER.info("Bill-Name:" + fileName + ",ID:"
									+ document.getDocumentInfo().getCustomAttributes().get("DRN").get(0)
									+ ",ResponseDate:" + b1.getResponseDate());
							b1.setObjectName(fileName);
							b1.setObjectId(document.getDocumentInfo().getCustomAttributes().get("DRN").get(0));
							b1.setResponseDate(String.valueOf(Instant.now()));
							bLists.add(b1);
						}
					}
				}
			}

		} catch (GeneralSecurityException | IOException | VaultBaseException | ServerErrorException | VaultException
				| TooManyResultsException | ServerLoopingException e) {
			LOGGER.error("Error: {}", e);
		}
		return bLists;
	}
}
