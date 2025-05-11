import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.FieldCondition;
import com.silanis.esl.sdk.FieldId;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.SignatureId;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.FieldBuilder;
import com.silanis.esl.sdk.builder.FieldConditionBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConditionalTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS//docs/capture_transaction.pdf";
  public static final String PACKAGE_TITLE = "Conditional-Transaction";
  public static final String SIGNER = "john.cyclist.mcguinness+capture@gmail.com";

  public static void main(String[] args) throws IOException {
    String env = "EU.PROD";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    FieldCondition condition_one = FieldConditionBuilder.newFieldCondition()
        .withId("ConditionId")
        .withCondition("document['DocumentId'].field['checkbox1'].value == 'X'")
        .withAction("document['DocumentId'].field['textfield1'].disabled = false")
        .build();

    FieldCondition condition_two = FieldConditionBuilder.newFieldCondition()
        .withId("ConditionId")
        .withCondition("document['DocumentId'].field['checkbox2'].value == 'X'")
        .withAction("document['DocumentId'].field['textfield2'].disabled = false")
        .build();

    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .describedAs("Description")
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNER)
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Condition")
            .withCustomId("Conditional"))
        .withDocument(DocumentBuilder.newDocumentWithName("ConditionDocument")
            .withId("DocumentId")
            .fromFile(FILE_PATH)
            .withSignature(SignatureBuilder.signatureFor(SIGNER)
                .withId(new SignatureId("signatureId"))
                .onPage(0)
                .atPosition(400, 600)
                .withField(FieldBuilder.checkBox()
                    .withId(new FieldId("checkbox1"))
                    .onPage(0)
                    .atPosition(100, 100))
                .withField(FieldBuilder.textField()
                    .withId(new FieldId("textfield1"))
                    .onPage(0)
                    .atPosition(400, 250))
                .withField(FieldBuilder.checkBox()
                    .withId(new FieldId("checkbox2"))
                    .onPage(0)
                    .atPosition(100, 350))
                .withField(FieldBuilder.textField()
                    .withId(new FieldId("textfield2"))
                    .onPage(0)
                    .atPosition(400, 500))))
        .withCondition(condition_one)
        .withCondition(condition_two)
        .build();

    PackageId packageId = eslClient.createPackageOneStep(pkg);
    eslClient.sendPackage(packageId);

    System.out.println("{\n" + packageId + "\n}");
    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Conditional"), SIGNER);
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