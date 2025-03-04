import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.silanis.esl.sdk.Direction;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PageRequest;

// Collect Groups 
public class SendersGroups {
  public static final String PACKAGE_TITLE = "SendersGroup";
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";

  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // get list of groups
    Collection<com.silanis.esl.sdk.GroupSummary> groupSummary = eslClient.getGroupService().getGroupSummaries();

    System.out.println("List of groups:");

    for (com.silanis.esl.sdk.GroupSummary groupSumm : groupSummary) {
      System.out.format("%s,with group Email address is: %s\n", groupSumm.getName(), groupSumm.getEmail());
    }

    // get list of senders
    int i = 1, j = 5;
    Map<String, com.silanis.esl.sdk.Sender> accountMembers = eslClient.getAccountService()
        .getSenders(Direction.ASCENDING, new PageRequest(i, j));

    System.out.println("\nList of senders:");
    while (!accountMembers.isEmpty()) {
      for (Map.Entry entry : accountMembers.entrySet()) {
        System.out.print("Email address: " + entry.getKey() + "\n");
        i++;
      }

      accountMembers = eslClient.getAccountService().getSenders(Direction.ASCENDING, new PageRequest(i, j));
    }

    System.out.println();
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