package com.lmh;

import com.lmh.util.Utilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application
{
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
  
  public static void main(String[] args)
    throws IOException
  {
    SpringApplication.run(Application.class, args);
    LOGGER.info("--------------------NewEDMS Application start-------------");
    
    String fileIndexFolderLogger = (new File(Utilities.getProperty("dir.postpaid.input.index")).canRead()) && (new File(Utilities.getProperty("dir.postpaid.input.index")).canWrite()) ? "able to access postpaid input meta file location" : "not able to access postpaid input meta file location";
    
    LOGGER.info("IngestionModule|Application is " + fileIndexFolderLogger);
    
    String fileImagesFolderLogger = (new File(Utilities.getProperty("dir.postpaid.input.images")).canRead()) && (new File(Utilities.getProperty("dir.postpaid.input.images")).canWrite()) ? "able to access postpaid input image files location" : "not able to access postpaid input image files location";
    
    LOGGER.info("IngestionModule|Application is " + fileImagesFolderLogger);
    
    String feedFileLogger = (new File(Utilities.getProperty("dir.postpaid.output.soabatch.feed")).canRead()) && (new File(Utilities.getProperty("dir.postpaid.output.soabatch.feed")).canWrite()) ? "able to access postpaid SOA feed files location" : "not able to access postpaid SOA feed files location";
    
    LOGGER.info("IngestionModule|Application is " + feedFileLogger);
    
    String tempVaultLocationLogger = (new File(Utilities.getProperty("dir.postpaid.output.vault.temp")).canRead()) && (new File(Utilities.getProperty("dir.postpaid.output.vault.temp")).canWrite()) ? "able to access postpaid vault temporary location" : "not able to access postpaid vault temporary location";
    
    LOGGER.info("IngestionModule|Application is " + tempVaultLocationLogger);
    
    String processedFileLogger = (new File(Utilities.getProperty("dir.postpaid.output.processed")).canRead()) && (new File(Utilities.getProperty("dir.postpaid.output.processed")).canWrite()) ? "able to access postpaid processed files location" : "not able to access postpaid processed files location";
    
    LOGGER.info("IngestionModule|Application is " + processedFileLogger);
    
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
    
    String vaultIngestLocationLogger = (new File(Utilities.getProperty("dir.vault.ingest")).canRead()) && (new File(Utilities.getProperty("dir.vault.ingest")).canWrite()) ? "able to access ingest files vault location" : "not able to access ingest files vault location";
    
    LOGGER.info("IngestionModule|Application is " + vaultIngestLocationLogger);
    
    String watchDirectoryPath = Utilities.getProperty("watch.dir.path");
    
    Path dir = Paths.get(watchDirectoryPath, new String[0]);
    PreProcess pre = new PreProcess(dir);
    pre.main();
  }
}
