
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.lmh.prepaid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmh.util.IngestFiles;
import com.lmh.util.Utilities;

/**
 *
 * @author RajuY
 */
public class PrepaidInputFile {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrepaidInputFile.class);

	public void prepaid() {
		LOGGER.info("IngestionModule|Prepaid meta file was placed");
	    String feedFileFolderLogger = (new File(Utilities.getProperty("dir.prepaid.input.feed")).canRead()) && (new File(Utilities.getProperty("dir.prepaid.input.feed")).canWrite()) ? "able to access prepaid feed file location" : "not able to access prepaid feed file location";
	    
	    LOGGER.info("IngestionModule|Application is " + feedFileFolderLogger);
	    
	    String logosFolderLogger = (new File(Utilities.getProperty("dir.prepaid.logo")).canRead()) && (new File(Utilities.getProperty("dir.prepaid.logo")).canWrite()) ? "able to access prepaid brand logos location" : "not able to access prepaid brand logos location";
	    
	    LOGGER.info("IngestionModule|Application is " + logosFolderLogger);
	    
	    String mvnoFileLocationLogger = (new File(Utilities.getProperty("dir.prepaid.output.images")).canRead()) && (new File(Utilities.getProperty("dir.prepaid.output.images")).canWrite()) ? "able to access prepaid temporary MVNO files location" : "not able to access prepaid temporary MVNO files location";
	    
	    LOGGER.info("IngestionModule|Application is " + mvnoFileLocationLogger);
	    
	    String prepaidTempVaultLocationLogger = (new File(Utilities.getProperty("dir.prepaid.output.download")).canRead()) && (new File(Utilities.getProperty("dir.prepaid.output.download")).canWrite()) ? "able to access prepaid vault temporary location" : "not able to access prepaid vault temporary location";
	    
	    LOGGER.info("IngestionModule|Application is " + prepaidTempVaultLocationLogger);
	    
	    String processedPrepaidFileLogger = (new File(Utilities.getProperty("dir.prepaid.output.Processed")).canRead()) && (new File(Utilities.getProperty("dir.prepaid.output.Processed")).canWrite()) ? "able to access prepaid processed files location" : "not able to access prepaid processed files location";
	    
	    LOGGER.info("IngestionModule|Application is " + processedPrepaidFileLogger);
	    
		File indexFolder = new File(Utilities.getProperty("dir.prepaid.input.feed"));
		File[] listofIndexFiles = indexFolder.listFiles();
		for (File file : listofIndexFiles) {
			System.gc();
			if (file.isFile() && file.getName().endsWith(".txt")) {
				LOGGER.info("IngestionModule| Processing File name is :" + file.getName());

				try (FileReader fileReader = new FileReader(file)) {
					// BufferedReader bufferedReader = new
					// BufferedReader(fileReader);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					List<String> pageData = new ArrayList<>();
					int count = 0;
					String line = bufferedReader.readLine();

					String brandName = line.split("\\|")[1].split("_")[1];
					LOGGER.info("Brand name is:" + brandName);

					while ((line = bufferedReader.readLine()) != null) {
						if (!line.startsWith("B1")) {
							String[] record = line.split("\\|");
							pageData.add(line);

							if (record[0].equals("D4")) {
								createPDFFile(brandName, pageData);

								pageData.clear();
								count = count + 1;
							}

						}

					}
					LOGGER.info("IngestionModule| Generated PDF's Count:" + count);

				} catch (FileNotFoundException e) {
					LOGGER.error("Error:{}", e);
				} catch (IOException e) {
					LOGGER.error("Error:{}", e);
				}
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				// Moving to another folder
				Path temp = null;
				try {
					temp = Files.move(Paths.get(Utilities.getProperty("dir.prepaid.input.feed") + file.getName()),
							Paths.get(Utilities.getProperty("dir.prepaid.output.Processed")
									+ FilenameUtils.removeExtension(file.getName()) + "_" + sdf.format(timestamp)
									+ ".txt"));
				} catch (IOException e) {
					LOGGER.error("Error:{}", e);
				}
				if (temp != null) {
					LOGGER.info("IngestionModule| {} file process completed", file.getName());
				} else {
					LOGGER.info("IngestionModule| {} file process is not completed", file.getName());
				}
			}
		}

	}

	private static void createPDFFile(String brandName, List<String> pageData) {
		PDFObject pdfObject = new PDFObject();
		PDFGenerate generate = new PDFGenerate();
		PrepaidIndexFileGenerator indexFileGenerator = new PrepaidIndexFileGenerator();
		List<String> dt = new ArrayList<>();
		for (String data : pageData) {

			if (data.startsWith("A1")) {
				pdfObject.setA1(data);
			}
			if (data.startsWith("H1")) {
				pdfObject.setH1(data);
			}
			if (data.startsWith("D1")) {
				dt.add(data);
			}
			if (data.startsWith("D2")) {
				pdfObject.setD2(data);
			}
			if (data.startsWith("D3")) {
				pdfObject.setD3(data);
			}
			if (data.startsWith("D4")) {
				pdfObject.setD4(data);
				pdfObject.setD1(dt);
			}

		}
		LOGGER.info("Customer Data:" + pdfObject);

		if (pdfObject.getA1() != null && pdfObject.getH1() != null) {
			generate.generate(pdfObject, brandName);
			if (brandName.equals("Celcom")) {
				indexFileGenerator.indexGenerator(pdfObject);
			}
			IngestFiles.moveFilesToVaultDownload(Utilities.getProperty("dir.prepaid.output.download"));
		}
	}

}
