package com.lmh.soap;

import com.g1.e2.vault.OutputFormat;
import com.g1.e2.vault.RenderOptions;
import com.g1.e2.vault.ResolutionWidth;
import com.g1.e2.vault.Rotation;
import com.g1.e2.vault.ServerErrorException;
import com.g1.e2.vault.VaultClient;
import com.g1.e2.vault.VaultException;
import com.lmh.bill_ws.GetBillObjectRequest;
import com.lmh.bill_ws.GetBillObjectResponse;
import com.pb.vault.exception.VaultBaseException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class BillObjectEndpoint
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BillObjectEndpoint.class);
  private static final String NAMESPACE_URI = "http://lmh.com/bill-ws";
  
  @PayloadRoot(namespace=NAMESPACE_URI, localPart="getBillObjectRequest")
  @ResponsePayload
  public GetBillObjectResponse getBillsList(@RequestPayload GetBillObjectRequest request)
  {
    GetBillObjectResponse response = new GetBillObjectResponse();
    LOGGER.info("Bill object request processing");
    String dbs = request.getRepositoryName();
    String docId = request.getObjectId();
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    VaultClient client = new VaultClient();
    try
    {
      client.connect("localhost", 6003);
      LOGGER.info("Connected newEDMS system");
      
      VaultClient.Database db = client.getDatabase(dbs);
      LOGGER.info(dbs + "database Connected");
      
      VaultClient.Document doc = db.getDocumentByGUID(docId, true);
      
      OutputFormat of = OutputFormat.PDF;
      
      RenderOptions renderOptions = new RenderOptions();
      renderOptions.setEnableBackground(false);
      
      doc.renderAllPages(bos, of, ResolutionWidth.NONE, Rotation.NONE, renderOptions);
    }
    catch (GeneralSecurityException|IOException|VaultBaseException|ServerErrorException|VaultException e)
    {
      LOGGER.error("Error:{}", e);
    }
    byte[] bytes = bos.toByteArray();
    DataHandler handler = new DataHandler(new ByteArrayDataSource(bytes, "application/octet-stream"));
    
    response.setBillObject(handler);
    
    return response;
  }
}
