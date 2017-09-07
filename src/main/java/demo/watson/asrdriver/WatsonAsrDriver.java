package demo.watson.asrdriver;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import demo.watson.asrdriver.exception.AsrException;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricardo Limonta
 */
public class WatsonAsrDriver implements AsrDriver {

    private SpeechToText service;
    private RecognizeOptions options;
    private int responseTimeout;
    private AsrDriverEventListener listener;
    private Map<String, String> languages;
    
    @Override
    public void configure(Map<String, String> parameters) {
        //create service instance
        this.service = new SpeechToText(parameters.get("WATSON_API_USERNAME"), 
                                   parameters.get("WATSON_API_PASSWORD"));
        
        //configure response timeout
        if (parameters.containsKey("RESPONSE_TIMEOUT")) {
            this.responseTimeout = Integer.parseInt(parameters.get("RESPONSE_TIMEOUT"));
        }
        
        //create a list of supported languages (supports only NarrowbandModel 8000KHz)
        languages = new HashMap<>();
        languages.put("en-GB", "en-GB_NarrowbandModel");
        languages.put("en-US", "en-US_NarrowbandModel");
        languages.put("es-ES", "es-ES_NarrowbandModel");
        languages.put("ja-JP", "ja-JP_NarrowbandModel");
        languages.put("pt-BR", "pt-BR_NarrowbandModel");
        languages.put("zh-CN", "zh-CN_NarrowbandModel");
    }

    @Override
    public void startRecognizing(String lang, List<String> hints) {
        
        //verify if language is supported
        if (!languages.containsKey(lang)) {
            throw new AsrException("Language not supported: " + lang);
        }
        
        //create the recognize options
        options = new RecognizeOptions.Builder().contentType(HttpMediaType.AUDIO_WAV)
                                                .model(languages.get(lang))
                                                .interimResults(true)
                                                .build();
    }

    @Override
    public void write(byte[] data, int offset, int len) {
        service.recognizeUsingWebSocket(new ByteArrayInputStream(data), options, new BaseRecognizeCallback() 
            {
                @Override
                public void onTranscription(SpeechResults speechResults) {
                    StringBuilder result = new StringBuilder();
                    for (Transcript transcript : speechResults.getResults()) {
                        for (SpeechAlternative alternative : transcript.getAlternatives()) {
                            result.append(alternative.getTranscript());
                        }
                    }
                    
                    listener.onSpeechRecognized(result.toString(), speechResults.isFinal());
                }
                
                @Override
                public void onError(Exception e) {
                    listener.onError(new AsrException(e)); 
                }   
            }
        );
    }

    @Override
    public void finishRecognizing() {
    }

    @Override
    public void setListener(AsrDriverEventListener listener) {
        this.listener = listener;
    }

    @Override
    public int getResponseTimeoutInMilliseconds() {
        return responseTimeout;
    }
}