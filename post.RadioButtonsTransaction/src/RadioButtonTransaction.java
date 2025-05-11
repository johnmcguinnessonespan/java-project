import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.Field;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.FieldBuilder;
import com.silanis.esl.sdk.builder.FieldValidatorBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class RadioButtonTransaction {
	public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
	public static final String DOCUMENT = "/home/john/Documents/OSS/docs/radio_button_transaction.pdf";
	public static final String PACKAGE_TITLE = "Radio-Button-Transaction";
	public static Map<String, String> signerInfo = ImmutableMap.of("firstname", "John", "lastname", "McGuinness",
			"email", "mc_cyclist@yahoo.co.uk",
			"phone", "+447485213049", "address", "12 West End Lane, London, Canada");

	public static void main(String[] args) throws Exception {
		String env = "US2.SKF";
		Properties prop = readPropertiesFile(CONFIG_PATH);
		EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

		try {
			Field radio1 = FieldBuilder.radioButton("group1").withName("signer1_group1_radio1")
					.withPositionExtracted().withValue(true).withValidation(FieldValidatorBuilder.basic().required())
					.build();
			Field radio2 = FieldBuilder.radioButton("group1").withName("signer1_group1_radio2")
					.withPositionExtracted().withValue(false).withValidation(FieldValidatorBuilder.basic().required())
					.build();
			Field radio3 = FieldBuilder.radioButton("group1").withName("signer1_group1_radio3")
					.withPositionExtracted().withValue(false).withValidation(FieldValidatorBuilder.basic().required())
					.build();

			DocumentPackage documentPackage = PackageBuilder.newPackageNamed("Test Package with Radio Button Fields")
					.withSigner(SignerBuilder.newSignerWithEmail(signerInfo.get("email"))
							.withCustomId("signer1")
							.withFirstName(signerInfo
									.get("firstname"))
							.withLastName(signerInfo
									.get("lastname")))
					.withDocument(DocumentBuilder.newDocumentWithName("Radio Button Transaction")
							.fromFile(DOCUMENT)
							.enableExtraction()
							.withSignature(SignatureBuilder.signatureFor(signerInfo.get("email"))
									.withPositionExtracted().withName("signer1_fullname1")
									.withField(FieldBuilder.signatureDate().withPositionExtracted()
											.withName("signer1_fullname1_date"))
									.withField(radio1)
									.withField(radio2)
									.withField(radio3))
							.withInjectedField(FieldBuilder.textField().withName("signer1_name")
									.withValue(signerInfo.get("firstname") + " " + signerInfo.get("lastname")))
							.withInjectedField(FieldBuilder.textField().withName("signer1_email")
									.withValue(signerInfo.get("email")))
							.withInjectedField(FieldBuilder.textField().withName("signer1_phone")
									.withValue(signerInfo.get("phone")))
							.withInjectedField(FieldBuilder.textField().withName("signer1_address")
									.withValue(signerInfo.get("address"))))
					.build();

			PackageId packageId = eslClient.createPackageOneStep(documentPackage);

			eslClient.sendPackage(packageId);
			System.out.println("{\n" + packageId + "\n}");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
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