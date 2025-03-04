import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.Page;
import com.silanis.esl.sdk.PageRequest;

// Get a list of Templates
public class ListTemplates {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    System.out.println("Templates in environment: " + env + ":\n");

    int i = 1;
    ArrayList<String> templateIds = new ArrayList<String>();

    Page<DocumentPackage> templates = eslClient.getPackageService().getTemplates(new PageRequest(i, 50));

    for (DocumentPackage template : templates) {
      System.out.println(String.format("Template with id: %s named: %s", template.getId(), template.getName()));
      templateIds.add(template.getId().toString());
    }
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