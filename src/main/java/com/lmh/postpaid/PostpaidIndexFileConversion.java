package com.lmh.postpaid;

import com.itextpdf.text.pdf.PdfReader;
import com.lmh.util.IngestFiles;
import com.lmh.util.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostpaidIndexFileConversion
{
  private static final Logger LOGGER = LoggerFactory.getLogger(PostpaidIndexFileConversion.class);
  
  public void postpaid()
  {
    LOGGER.info("IngestionModule|Postpaid meta file was placed");
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
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    File fileIndexFolder = new File(Utilities.getProperty("dir.postpaid.input.index"));
    File fileImagesFolder = new File(Utilities.getProperty("dir.postpaid.input.images"));
    File[] indexFileList = fileIndexFolder.listFiles();
    for (File file : indexFileList)
    {
      System.gc();
      if ((file.isFile()) && (file.getName().endsWith(".txt")))
      {
        LOGGER.info("IngestionModule| Processing File name is :" + file.getName());
        File feedFile = new File(Utilities.getProperty("dir.postpaid.output.soabatch.feed") + file.getName());
        try
        {
          FileReader fileReader = new FileReader(file);
          BufferedReader bufferedReader = new BufferedReader(fileReader);
          
          FileWriter feedWriter = new FileWriter(feedFile);
          String line;
          while ((line = bufferedReader.readLine()) != null)
          {
            String[] record = line.split("\\|");
            if (findPdfFile(fileImagesFolder, record[5]))
            {
              PdfReader pdfReader = new PdfReader(Utilities.getProperty("dir.postpaid.input.images") + record[5]);
              int pagesCount = pdfReader.getNumberOfPages();
              pdfReader.close();
              String uniqueID = UUID.randomUUID().toString();
              
              Files.move(Paths.get(Utilities.getProperty("dir.postpaid.input.images") + record[5], new String[0]), 
                Paths.get(Utilities.getProperty("dir.postpaid.output.vault.temp") + record[5], new String[0]), new CopyOption[0]);
              LOGGER.info("IngestionModule| Generated File name is: " + record[5]
                .substring(0, 16) + ".jrn");
              
              File writeFile = new File(Utilities.getProperty("dir.postpaid.output.vault.temp") + FilenameUtils.removeExtension(record[5]) + ".jrn");
              
              writeFile.createNewFile();
              FileWriter writer = new FileWriter(writeFile);Throwable localThrowable3 = null;
              try
              {
                writer.write("J|" + record[5] + "|" + dateFormat.format(df.parse(record[3])) + "\n" + "A|MBL|" + record[1] + "\n" + "A|Name|" + record[2] + "\n" + "A|DRN|" + uniqueID + "\n" + "A|doc.guid|" + uniqueID + "\n" + "D|" + record[0] + "|" + dateFormat
                
                  .format(df.parse(record[3])) + "|" + pagesCount + "\n");
                writer.flush();
              }
              catch (Throwable localThrowable1)
              {
                localThrowable3 = localThrowable1;throw localThrowable1;
              }
              finally
              {
                if (writer != null) {
                  if (localThrowable3 != null) {
                    try
                    {
                      writer.close();
                    }
                    catch (Throwable localThrowable2)
                    {
                      localThrowable3.addSuppressed(localThrowable2);
                    }
                  } else {
                    writer.close();
                  }
                }
              }
              IngestFiles.moveFilesToVaultDownload(Utilities.getProperty("dir.postpaid.output.vault.temp"));
            }
            else
            {
              LOGGER.info("IngestionModule| " + record[5] + "is not existed in the Image folder");
            }
            feedWriter.write(record[0] + "|" + record[1] + "|" + record[2] + "|" + record[3] + "|" + record[4] + "\n");
          }
          feedWriter.close();
          bufferedReader.close();
          fileReader.close();
          
          Path temp = null;
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
          Timestamp timestamp = new Timestamp(System.currentTimeMillis());
          temp = Files.move(Paths.get(Utilities.getProperty("dir.postpaid.input.index") + file.getName(), new String[0]), 
            Paths.get(Utilities.getProperty("dir.postpaid.output.processed") + 
            FilenameUtils.removeExtension(file.getName()) + "_" + sdf.format(timestamp) + ".txt", new String[0]), new CopyOption[0]);
          if (temp != null) {
            LOGGER.info("IngestionModule| {} file process completed", file.getName());
          } else {
            LOGGER.info("IngestionModule| {} file process is not completed", file.getName());
          }
        }
        catch (Exception e)
        {
          LOGGER.error("IngestionModule| Error: {}", e);
        }
      }
    }
  }
  
  public static boolean findPdfFile(File pdfFolder, String pdfName)
  {
    Boolean isThere = Boolean.valueOf(false);
    File[] listofPdfFiles = pdfFolder.listFiles();
    for (File pdfFile : listofPdfFiles) {
      if ((pdfFile.isFile()) && (pdfFile.getName().endsWith(".pdf")) && 
        (pdfFile.getName().equals(pdfName))) {
        isThere = Boolean.valueOf(true);
      }
    }
    return isThere.booleanValue();
  }
}
