package CSVMonitor.atCSVMonitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.Properties;

import com.sun.mail.imap.protocol.SearchSequence;

import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Monitor {
	
	
public static Properties getProp() throws IOException{
		
		Properties props = new Properties();
		//Path do servidor onde será aplicado 
		//FileInputStream file = new FileInputStream("/home/docker/CSVMonitor.properties");
		
		//Path de testes
		//FileInputStream file = new FileInputStream("/home/oracle/monitor/CSVMonitor.properties");
		FileInputStream file = new FileInputStream("CSVMonitor.properties");
		
		
		props.load(file);
		  return props;
		  
	}


	public static void main(String[] args) throws IOException {
		
		//System.out.println("Monitor ativando...");

		/*
		
		if(args.length>0) {
			if(args[0].equals("--help")) {
			
			System.out.println("----------------------------------CSVMonitor -------------------------------------------------");
			System.out.println("                                                                                              ");
			System.out.println("   Monitora (listener) um diretorio e envia um email a cada novo arquivo criado de acordo  com");
			System.out.println("   a extensao definida no arquivo CSVMonitor.properties localizado em /home/docker");
			System.out.println("   Suporta Java 7.x ou superior");
			System.out.println("   2018-05-04 | Autor: Marcelo AraujoS");
			System.out.println("                                                                                              ");    
			System.out.println("   Forma de execucao: nohup java -jar CSVMonitor.jar &                                        ");
			System.out.println("                                                                                              ");
			System.out.println("----------------------------------------------------------------------------------------------");
			
			
			}
			
			System.exit(0);
			
		}
        
        */
		
		Properties properties = getProp();
		Path path = Paths.get(properties.getProperty("dirMonitoring"));
        
        WatchService watchService = null;
        try{
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, 
            StandardWatchEventKinds.ENTRY_CREATE);
            //System.out.println("verificando tipo de arquivo...");

        }catch(IOException io){
        	System.out.println("dirMonitoring:" +path);
            io.printStackTrace();
        }
        
        WatchKey key = null;

        while(true){
        	
        	
            try{
                key = watchService.take();

                for(WatchEvent<?> event : key.pollEvents()){
                    Kind<?> kind = event.kind();
                    
                    String arquivo = event.context().toString();
                                        
                    if(arquivo.contains(properties.getProperty("typeFile"))){
                        
                    	//SendMail sm = new SendMail();
                    	//SendWattach sm = new SendWattach();
                    	
                    
                    try{
                    	
                    	//System.out.println("enviando email...");
                    	//System.out.println(path+"/"+arquivo);
                    	//sm.sendMail(path+"/"+arquivo);	
                    	
                    	String command = "python CSVMonitor_Send.py "+path+"/"+arquivo;
                    	//System.out.println(command);
                    	Runtime run = Runtime.getRuntime();
                    			run.exec(command);
                    
                    	
                    	}catch (Exception e) {
						
                    		e.printStackTrace();
                    	}
                    
                    }
                    
                }
            }catch(InterruptedException ie){
                System.out.println("erro: "+path);
            	ie.printStackTrace();
            }

            boolean reset = key.reset();
            if(!reset){
                break;
            }
        }


    }

}
