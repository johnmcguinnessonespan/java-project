import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.*;

public class AuthentificationToken {
  public static final String PACKAGE_TITLE = "AuthenticationToken";
  public static final String TRANSACTIONID = "Wmv0fuv-uGfOWyl_cvvW_VU5R-g=";
  public static final String IDVTRANSACTIONID = "8b734331-bc5b-4843-9784-d4ece4b7dc22";

  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // create user authentication token
    String userAuthToken = eslClient.getAuthenticationTokensService().createUserAuthenticationToken();
    String ospnLogin = prop.getProperty(env + ".URL");

    System.out.println("\nUser Token for login to " + env + ":");
    System.out.println(ospnLogin.substring(0, ospnLogin.length() - 4) + "/auth?authenticationToken=" + userAuthToken
        + "&target=" + ospnLogin.substring(0, ospnLogin.length() - 4) + "/a/dashboard");

    // //Create sender authentication token
    // String senderAuthToken = eslClient.getAuthenticationTokensService().createSenderAuthenticationToken("5vjLRY5MWrDJ6MzRAEyCKOy5IH0=");
    // System.out.println("Sender Token: " + senderAuthToken);
    //
    // //Create signer authentication token
    // String signerAuthToken = eslClient.getAuthenticationTokensService().createSignerAuthenticationToken("5vjLRY5MWrDJ6MzRAEyCKOy5IH0=",
    // "8b734331-bc5b-4843-9784-d4ece4b7dc22");
    // System.out.println("Signer Token: " + signerAuthToken);
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