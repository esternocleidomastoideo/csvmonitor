import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.Properties;

import javax.swing.plaf.synth.SynthSeparatorUI;

import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Monitor {
	
	
public static Properties getProp() throws IOException{
		
		Properties props = new Properties();
		  FileInputStream file = new FileInputStream("/home/oracle/CSVMonitor.properties");
		  props.load(file);
		  return props;
		  
	}


	public static void main(String[] args) throws IOException {

		if(args.length>0) {
			if(args[0].equals("--help")) {
			
			System.out.println("----------------------------------CSVMonitor -------------------------------------------------");
			System.out.println("                                                                                              ");
			System.out.println("   Monitora (listener) um diretorio e envia um email a cada novo arquivo criado de acordo  com");
			System.out.println("   a extensão definida no arquivo CSVMonitor.properties localizado em /home/oracle");
			System.out.println("   Suporta Java 7.x ou superior");
			System.out.println("   2018-05-04 | Autor: Marcelo AraujoS | SysMap Solutions");
			System.out.println("                                                                                              ");    
			System.out.println("   Forma de execução: nohup java -jar CSVMonitor.jar &                                        ");
			System.out.println("                                                                                              ");
			System.out.println("----------------------------------------------------------------------------------------------");
			
			
			}
			
			System.exit(0);
			
		}
        
		Properties properties = getProp();
		//Path path = Paths.get("/home/oracle/lab/");
		
		
		Path path = Paths.get(properties.getProperty("dirMonitoring"));
        
        WatchService watchService = null;
        try{
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, 
            StandardWatchEventKinds.ENTRY_CREATE);

        }catch(IOException io){
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
                                       	
                    	
                    	//System.out.println("Enviar o arquivo: "+arquivo);
                    
                    	SendMail sm = new SendMail();
                    
                    try{
                    	
                    	sm.sendMail(path+"/"+arquivo);	
                    	
                    	}catch (Exception e) {
						
                    		e.printStackTrace();
                    	}
                    
                    }
                    
                }
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }

            boolean reset = key.reset();
            if(!reset){
                break;
            }
        }


    }

}
