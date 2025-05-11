import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.TextAnchorPosition;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.FieldBuilder;
import com.silanis.esl.sdk.builder.TextAnchorBuilder;

import static com.silanis.esl.sdk.builder.DocumentBuilder.newDocumentWithName;
import static com.silanis.esl.sdk.builder.PackageBuilder.newPackageNamed;
import static com.silanis.esl.sdk.builder.SignerBuilder.newSignerWithEmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static com.silanis.esl.sdk.builder.SignatureBuilder.signatureFor;

// transaction with anchor tags and two signers
public class AnchorTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String DOCUMENT = "/home/john/Documents/OSS/docs/anchor_contract.pdf";
  public static final String PACKAGE_TITLE = "Anchor-Transaction";
  public static final String[] SIGNER = { "john.cyclist.mcguinness+client@gmail.com", "john.cyclist.mcguinness+contractor@gmail.com" };

  public static void main(String[] args) throws Exception {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object adding 2 Signers
    DocumentPackage superDuperPackage = newPackageNamed(PACKAGE_TITLE)
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            .withDefaultTimeBasedExpiry()
            .withRemainingDays(3))
        .withSigner(newSignerWithEmail(SIGNER[0])
            .withFirstName("John")
            .withLastName("Doe")
            .withCustomId("client")
            .signingOrder(0))
        .withSigner(newSignerWithEmail(SIGNER[1])
            .withFirstName("Jane")
            .withLastName("Doe")
            .withCustomId("contractor")
            .signingOrder(1))
        .withDocument(newDocumentWithName("Anchor Contract")
            .fromFile(DOCUMENT)
            .enableExtraction()
            // add text based on x:y for Signer 1
            .withSignature(signatureFor(SIGNER[0])
                .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Signature of the Client")
                    .atPosition(TextAnchorPosition.TOPLEFT)
                    .withSize(150, 40)
                    .withOffset(0, -50)
                    .withCharacter(0)
                    .withOccurence(0))
                .withField(FieldBuilder.signerName()
                    .withPositionAnchor(TextAnchorBuilder.newTextAnchor("(hereafter referred to as")
                        .atPosition(TextAnchorPosition.TOPRIGHT)
                        .withSize(150, 20)
                        .withOffset(-175, -5)
                        .withCharacter(0)
                        .withOccurence(0)))
                .withField(FieldBuilder.signatureDate()
                    .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Date")
                        .atPosition(TextAnchorPosition.TOPRIGHT)
                        .withSize(75, 40)
                        .withCharacter(4)
                        .withOffset(10, -30)
                        .withOccurence(0))))
            // add text based on x:y for Signer 1
            .withSignature(signatureFor(SIGNER[1])
                .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Signature of the Contractor")
                    .atPosition(TextAnchorPosition.TOPLEFT)
                    .withSize(150, 40)
                    .withOffset(0, -50)
                    .withCharacter(0)
                    .withOccurence(0))
                .withField(FieldBuilder.signerName()
                    .withPositionAnchor(TextAnchorBuilder.newTextAnchor("(hereafter referred to as")
                        .atPosition(TextAnchorPosition.TOPLEFT)
                        .withSize(150, 20)
                        .withOffset(-175, -5)
                        .withCharacter(0)
                        .withOccurence(1)))
                .withField(FieldBuilder.signatureDate()
                    .withPositionAnchor(TextAnchorBuilder.newTextAnchor("Date")
                        .atPosition(TextAnchorPosition.TOPRIGHT)
                        .withSize(75, 40)
                        .withOffset(10, -30)
                        .withCharacter(4)
                        .withOccurence(1)))))
        .build();

    PackageId packageId = eslClient.createAndSendPackage(superDuperPackage);

    System.out.println("{\n" + packageId + "\n}");

    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "client"), SIGNER[0]);
    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "contractor"), SIGNER[1]);
  }

  // Print the link to Signing Ceremony
  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
  }

  // Read account details from file
  public static Properties readPropertiesFile(String fileName) throws IOException {
    FileInputStream fis = null;
    Properties prop = null;

    try {
      fis = new FileInputStream(fileName);
      prop = new Properties();
      prop.load(fis);
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      fis.close();
    }

    return prop;
  }
}