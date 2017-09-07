package demo.watson.asrdriver;

import demo.watson.asrdriver.exception.AsrException;
import java.util.List;
import java.util.Map;

/**
 * @author Ricardo Limonta
 */
public interface AsrDriver {

    void configure(Map<String, String> parameters);

    void startRecognizing(String lang, List<String> hints);

    void write(byte[] data, int offset, int len);

    void finishRecognizing();

    void setListener(AsrDriverEventListener listener);

    int getResponseTimeoutInMilliseconds();

    interface AsrDriverEventListener {

        void onSpeechRecognized(String text, boolean isFinal);

        void onError(final AsrException error);
    }
}
