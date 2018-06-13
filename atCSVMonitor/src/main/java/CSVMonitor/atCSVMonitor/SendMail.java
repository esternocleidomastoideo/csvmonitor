package CSVMonitor.atCSVMonitor;


import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * autor: Marcelo AraujoS
 * Sysmap Solutions
 */

public class SendMail {

	
	public static Properties getProp() throws IOException{
		
		Properties props = new Properties();
		 
		// path do servidor onde será aplicado  
		//FileInputStream file = new FileInputStream("/home/docker/CSVMonitor.properties");  
		
		//path de testes
		FileInputStream file = new FileInputStream("CSVMonitor.properties");
		  props.load(file);
		  return props;
		  
	}
	
	
	public void sendMail(String arquivo) throws EmailException, IOException {
	
		
	// Cria anexo
	  EmailAttachment attachment = new EmailAttachment();
	  
	  attachment.setPath(arquivo);
	  attachment.setDisposition(EmailAttachment.ATTACHMENT);
	  attachment.setDescription(arquivo);
	  attachment.setName(arquivo);

	  // Mensagem
	  
	  Properties prop = getProp();
	  
	  try {
	
	  MultiPartEmail email = new MultiPartEmail();
	  //email.setStartTLSEnabled(true);
	  email.setDebug(true);
	  email.setBoolHasAttachments(true);
	  email.setHostName(prop.getProperty("hostName"));
	  email.setAuthentication(prop.getProperty("user"),prop.getProperty("password"));
	  email.setSmtpPort(Integer.parseInt(prop.getProperty("porta")));
	  email.addTo(prop.getProperty("addTo"));
	  email.setFrom(prop.getProperty("from"));
	  email.setSubject(prop.getProperty("subject"));
	  email.setMsg(prop.getProperty("message"));
	  
	
	//email.setHostName("smtp.gmail.com");
	//email.setAuthentication("marcelo.araujo.s***p@gmail.com","*******");
	//email.setSmtpPort(587);
	//email.addTo("xyz@hotmail.com", "Teste");  
	//email.setFrom("xyz@gmail.com", "Novo Arquivo Disponivel"); 
	//email.setMsg("Acaba de ser gerado um novo arquivo do diretorio /home/oracle/lab");
      

	  // adiciona o anexo
	  email.attach(attachment);

	  // envia o email
	  email.send();
	  }catch (Exception e) {
		System.out.println(e.getStackTrace());
	}
	  	  
	}
	  
}
