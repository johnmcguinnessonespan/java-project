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

public class SignAllTransaction
{
  public static final String PACKAGE_TITLE = "Sign-All-Transaction";
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS/sampleAgreement.pdf";
  public static final String[] SIGNER {"john.mcguinness+sender@skiff.com", "john.mcguinness+second@skiff.com"};

  public static void main(String[] args) throws IOException
  {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Package (Applying All Signatures in One Call)
    PackageBuilder packageToBuild = PackageBuilder.newPackageNamed(PACKAGE_TITLE);

    // Sender
    SignerBuilder signer1 = SignerBuilder.newSignerWithEmail("john.mcguinness+sender@skiff.com")
        .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Sender")
            .withCustomId("Sender")
        );

    // Client
    SignerBuilder signer2 = SignerBuilder.newSignerWithEmail("john.mcguinness+client@skiff.com")
        .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer")
            .withCustomId("Signer")

        );

    // Sender signature on first document
    SignatureBuilder signature1 = SignatureBuilder.signatureFor("john.mcguinness+sender@skiff.com")
        .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Signature of the Contractor")
            .atPosition(TextAnchorPosition.TOPLEFT)
            .withSize(150, 40)
            .withOffset(0, -50)
            .withCharacter(0)
            .withOccurence(0));

            // Client signature on first document

    SignatureBuilder signature2 = SignatureBuilder.signatureFor("john.mcguinness+client@skiff.com")
        .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Signature of the Client")
            .atPosition(TextAnchorPosition.TOPLEFT)
            .withSize(150, 40)
            .withOffset(0, -50)
            .withCharacter(0)
            .withOccurence(0));

    // First document
    DocumentBuilder document1 = DocumentBuilder.newDocumentWithName("Cleaning Contract")
        .fromFile("DOC_FILE_PATH")
        .withSignature(signature1)
        .withSignature(signature2);
    // Build package
    DocumentPackage packageToSend = packageToBuild.withSigner(signer1)
        .withSigner(signer2)
        .withDocument(document1)
        .withDocument(document2)
        .build();

    // Create and send package
    PackageId packageId = EslClient.createAndSendPackage(packageToSend);

    // sign all documents for sender
    EslClient.signDocuments(packageId); // TODO Auto-generated method stub
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
    catch (FileNotFoundException fnfe)
    {
      fnfe.printStackTrace();
    }
    catch (IOException ioe)
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
