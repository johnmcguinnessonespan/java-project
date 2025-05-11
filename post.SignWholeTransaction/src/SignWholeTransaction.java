import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.TextAnchorPosition;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
import com.silanis.esl.sdk.builder.TextAnchorBuilder;

public class SignWholeTransaction  {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS/docs/anchor_contract.pdf";
  public static final String PACKAGE_NAME = "SignWholeTransaction ";
  public static final String[] SIGNERS = {"john.mcguinness+sign1@gmail.com", "john.cyclist.mcguinness+sign2@gmail.com"};
 
  public static void main(String[] args) throws IOException
    {
    String env = "CA.SBX";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    //Package (Applying All Signatures in One Call)
    PackageBuilder packageToBuild = PackageBuilder.newPackageNamed(PACKAGE_NAME);

    //  Signer one
    SignerBuilder signer1 = SignerBuilder.newSignerWithEmail(SIGNERS[0])
        .withFirstName(prop.getProperty("FORENAME"))
        .withLastName("Signer_one")
        .withCompany("One");
	  // Signer two
    SignerBuilder signer2 = SignerBuilder.newSignerWithEmail(SIGNERS[1])
        .withFirstName(prop.getProperty("FORENAME"))
        .withLastName("Signer")
        .withCustomId("Signer");
    // Signer signs on first document
    SignatureBuilder signature1 = SignatureBuilder.signatureFor(SIGNERS[0])
  //      .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Signature of Signer one")
//        .atPosition(TextAnchorPosition.TOPLEFT)
        .withSize(150, 40)
        .withOffset(0, -50)
        .withCharacter(0)
        .withOccurence(0));
  	
    // Signer two signs on first document
    SignatureBuilder signature2 = SignatureBuilder.signatureFor(SIGNERS[1])
    //    .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Signature of Signer two")
      //  .atPosition(TextAnchorPosition.TOPLEFT)
        .withSize(150, 40)
        .withOffset(0, -50)
        .withCharacter(0)
        .withOccurence(0));
  	
    // Get document
    DocumentBuilder document1 = DocumentBuilder.newDocumentWithName("Cleaning Contract")
        .fromFile(FILE_PATH)
        .withSignature(signature1)
        .withSignature(signature2);
  	
    //Build package
    DocumentPackage packageToSend = packageToBuild.withSigner(signer1).withSigner(signer2)
  	    .withDocument(document1)
        .withDocument(document1)
    .build();

    //Create and send package
    PackageId packageId = eslClient.createAndSendPackage(packageToSend);

    //sign all documents for sender
    eslClient.signDocuments(packageId); 
    }
  
  public static Properties readPropertiesFile(String fileName) throws IOException
    {
    FileInputStream fis = null;
    Properties prop = null;

    try
      {
      fis = new FileInputStream(fileName);
      prop = new Properties();
      prop.load(fis);
      }
    catch(FileNotFoundException fnfe)
      {
      fnfe.printStackTrace();
      }
    catch(IOException ioe)
      {
      ioe.printStackTrace();
      }
    finally
      {
      fis.close();
      }

    return prop;
	}
  }