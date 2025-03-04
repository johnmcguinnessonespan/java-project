import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.silanis.esl.sdk.AccountRole;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.builder.AccountRoleBuilder;

public class ManageRoles
  {
  public static final String PACKAGE_TITLE = "WSL: ManageRoles";	
  public static final String NEW_ROLE_NAME = "Madman"; 	
 
  public static void main(String[] args) throws IOException
    {
    String env = "US2.SBX";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
			    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    //Create a customized Account Role
    AccountRole accountRole = AccountRoleBuilder.newAccountRole()
	    .withName(NEW_ROLE_NAME)
        .withPermissions(Arrays.asList(
//        	"transaction.transaction"
//            "sender_admin.custom_fields,"
//            "sender_admin.users",
//            "templates_layouts.share_templates,
//            "sender_admin.subscription",
//            "transaction.transaction",
//            "transaction.in_person",
//            "sender_admin.api_access",
//            "transaction.change_signer",
//            "sender_admin.sub_account_management",
//            "groups.group_signing_management",
//            "sender_admin.event_notification",
//            "sender_admin.data_management",
//            "sender_admin.customization",
//            "sender_admin.notary",
//            "sender_admin.security_settings",
//            "templates_layouts.share_layouts",
//            "sender_admin.self_serve_account_settings",
//            "transaction.delegation_visibility",
//            "sender_admin.reports",
            "sender_admin.role"
       		))
        .withDescription("Customized Role " + NEW_ROLE_NAME)
        .withEnabled(true)
        .withId("")
    .build();

    eslClient.getAccountService().addAccountRole(accountRole);
			
    //List all existing Account Roles
    String accountRoleID = "";
    List<AccountRole> accountRoles = eslClient.getAccountService().getAccountRoles();

    for (AccountRole ar : accountRoles)
      {
      if(NEW_ROLE_NAME.equals(ar.getName()))
        {
        accountRoleID = ar.getId();
        }

      System.out.println("Account Role Name: " + ar.getName() 
                       + "\n Account Role ID: " + ar.getId() 
                       + "\n  Acccount Role Permissions: " + ar.getPermissions()
                       + "\n");
      }
			
			
//    //Update an existing Account Role
//    if(accountRoleID != null && !"".equals(accountRoleID))
//      {
//      AccountRole updatedAccountRole = eslClient.getAccountService().getAccountRole(accountRoleID);
//      
//      updatedAccountRole.setDescription("Updated Description");
//      updatedAccountRole.getPermissions()
//          .addAll(Arrays.asList("sender_admin.security_settings","sender_admin.reports"));
//		
//      System.out.println(updatedAccountRole.getPermissions());
//
//      eslClient.getAccountService().updateAccountRole(accountRoleID, updatedAccountRole);
//      }
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