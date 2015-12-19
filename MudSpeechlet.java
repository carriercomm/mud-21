package mud;

import com.mongodb.MongoClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class MudSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(MudSpeechlet.class);

    private MongoClient mongoClient;
    private MudManager mudManager;    

    private void initializeManager() {
    	if (mongoClient == null) {
        	mongoClient = new MongoClient();
        	mudManager = new MudManager(mongoClient);
        	log.info("initializeManager: connected to mongoDB");
    	}
    }

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeManager();
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return mudManager.getLaunchResponse(request, session);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeManager();

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        switch (intentName) {
            case "LookIntent":
                return mudManager.getLookIntentResponse(intent, session);
                break;
            case "HintIntent":
                return mudManager.getHintIntentResponse(intent, session);
                break;
            case "AMAZON.HelpIntent":
                return mudManager.getHelpIntentReponse(intent, session);
                break;
            case "AMAZON.StopIntent":
            case "AMAZON.CancelIntent":
                return mudManager.getExitIntentResponse(intent, session);
                break;
            default:
                throw new SpeechletException("Invalid intent:" + intent.getName());
                break;
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any cleanup logic goes here
    }
}