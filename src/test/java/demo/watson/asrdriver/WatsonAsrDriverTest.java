package demo.watson.asrdriver;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ricardo Limonta
 */
public class WatsonAsrDriverTest {
    
    private WatsonAsrDriver watsonAsrDriver;
    private WatsonAsrDriverEventListener eventListener;
    
    @Before
    public void setUp() {
        //create a driver instance
        watsonAsrDriver = new WatsonAsrDriver();
        
        //create a driver listener instance
        eventListener = new WatsonAsrDriverEventListener();
        
        //setup parameters
        Map<String, String> params = new HashMap<>();
        params.put("WATSON_API_USERNAME", "321965b7-b4fc-4a94-bdbe-cbf77a2bf029");
        params.put("WATSON_API_PASSWORD", "VcedeadGFMzq");
        
        //call configure method
        watsonAsrDriver.configure(params);
        
        //set event listener
        watsonAsrDriver.setListener(eventListener);
        
        //call start recognition
        watsonAsrDriver.startRecognizing("pt-BR", null);   
    }
    
    @Test
    public void transcriptionTest() throws Exception {
        // Demo audio file
        URL url = WatsonAsrDriver.class.getResource("/audio/audio_demo.wav");
	Path path = Paths.get(url.toURI());
	byte[] data = Files.readAllBytes(path);
        
        //process transcription
        watsonAsrDriver.write(data, 0, 0);

        Thread.sleep(15000); 
    }
    
    @After
    public void tearDown() {
        watsonAsrDriver.finishRecognizing();
    }
}