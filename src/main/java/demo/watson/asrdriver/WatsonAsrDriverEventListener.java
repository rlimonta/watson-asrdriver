package demo.watson.asrdriver;

import demo.watson.asrdriver.exception.AsrException;

/**
 * @author Ricardo Limonta
 */
public class WatsonAsrDriverEventListener implements AsrDriver.AsrDriverEventListener {

    @Override
    public void onSpeechRecognized(String text, boolean isFinal) {
        System.out.println("------------------------------------------------");
        System.out.println("Final: " + isFinal);
        System.out.println("Transcription: " + text);
        System.out.println("------------------------------------------------");
    }

    @Override
    public void onError(AsrException error) {
        
    }
}