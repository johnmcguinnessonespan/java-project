import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.builder.*;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;

public class InjectionTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String DOCUMENT_ONE = "/home/john/Documents/OSS/docs/simple-FieldsTest.pdf"; // add sign box
  public static final String PACKAGE_TITLE = "Injection-Transaction";
  public static final String[] SIGNERS = { "john.cyclist.mcguinness+client@gmail.com",
      "john.cyclist.mcguinness+contractor@gmail.com" };

  public static void main(String[] args) throws IOException, FileNotFoundException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[0])
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("GMAIL")
            .withCustomId("Client")
            .signingOrder(0))
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[1])
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("GMAIL")
            .withCustomId("Contractor")
            .signingOrder(1))
        .withDocument(DocumentBuilder.newDocumentWithName(PACKAGE_TITLE)
            .enableExtraction()
            .fromStream(new FileInputStream(DOCUMENT_ONE), DocumentType.PDF)
            .withSignature(SignatureBuilder.signatureFor(SIGNERS[0])
                .withName("Signer1.Capture")
                .withPositionExtracted()
                .withField(FieldBuilder.signatureDate().withName("Signer1.Date").withPositionExtracted())
                .withField(FieldBuilder.signerName().withName("Signer1.Name").withPositionExtracted())
                .onPage(0)
                .atPosition(120, 745))
            .withSignature(SignatureBuilder.signatureFor(SIGNERS[1])
                .withName("Signer2.Capture")
                .withPositionExtracted()
                .withField(FieldBuilder.signatureDate().withName("Signer2.Date").withPositionExtracted())
                .withField(FieldBuilder.signerName().withName("Signer2.Name").withPositionExtracted())
                .onPage(0)
                .atPosition(120, 820))
            .withInjectedField(FieldBuilder.checkBox()
                .withName("Text1")
                .withValue(true))
            .withInjectedField(FieldBuilder.textField()
                .withName("Text2")
                .withValue("Lawn mower"))
            .withInjectedField(FieldBuilder.textField()
                .withName("Text3")
                .withValue("Fertilizer"))
            .withInjectedField(FieldBuilder.textField()
                .withName("Text4")
                .withValue("100"))
            .withInjectedField(FieldBuilder.textArea()
                .withName("ManyCharacters")
                .withValue("A legion of horribles"))
            .withInjectedField(FieldBuilder.textField()
                .withName("Text5")
                .withValue("12   12   12")))
        .withTimezoneId("America/Sao_Paulo")
        .build();

    PackageId packageId = eslClient.createAndSendPackage(pkg);

    System.out.println("{\n" + packageId + "\n}");

    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Client"), SIGNERS[0]);
    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Contractor"), SIGNERS[1]);
  }

  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
  }

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