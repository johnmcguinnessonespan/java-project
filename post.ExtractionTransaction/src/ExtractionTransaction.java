import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
import com.silanis.esl.sdk.internal.EslServerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ExtractionTransaction
  {
    public static final String PACKAGE_TITLE = "Extraction-Transaction";
    public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
    public static final String DOCUMENT = "/home/john/Documents/OSS/docs/extraction_transaction.pdf";
    public static final String SIGNER = "john.cyclist.mcguinness+extraction@gmail.com";
    
  public static void main(String[] args) throws IOException, EslServerException
    {
	String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);
		    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

	    // Build the DocumentPackage object
    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNER)
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer_one")
            .withCompany("One")
            .withCustomId("Signer1")
            .withEmailMessage("You can go first")
	        )
    	.withDocument(DocumentBuilder.newDocumentWithName(PACKAGE_TITLE)
            .fromFile(DOCUMENT)
            .enableExtraction()
            )
    	.build();

    PackageId packageId = eslClient.createPackageOneStep(pkg);
    eslClient.sendPackage(packageId);
    System.out.println("{\n" + packageId + "\n}");
    
    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Signer1"), SIGNER);
	}
	
  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
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