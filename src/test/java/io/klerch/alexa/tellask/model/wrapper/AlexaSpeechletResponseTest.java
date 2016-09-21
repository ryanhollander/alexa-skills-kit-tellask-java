package io.klerch.alexa.tellask.model.wrapper;

import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.StandardCard;
import io.klerch.alexa.tellask.dummies.AlexaStateModelSample;
import io.klerch.alexa.tellask.model.AlexaOutput;
import io.klerch.alexa.tellask.model.wrapper.AlexaSpeechletResponse;
import io.klerch.alexa.tellask.schema.type.AlexaOutputFormat;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlexaSpeechletResponseTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getResponseWithSlotsAndReprompt() throws Exception {
        final AlexaStateModelSample model = new AlexaStateModelSample();
        model.setName("Paul");

        final AlexaOutput output = AlexaOutput
                .ask("IntentWithReprompts")
                .withReprompt(true)
                .putSlot("credits", 123, AlexaOutputFormat.NUMBER)
                .putState(model).build();

        final AlexaSpeechletResponse response = new AlexaSpeechletResponse(output);
        Assert.assertEquals(output, response.getOutput());
        Assert.assertNotNull(response.getReprompt());
    }

    @Test
    public void getResponseWithSlots() throws Exception {
        final AlexaStateModelSample model = new AlexaStateModelSample();
        model.setName("Paul");

        final AlexaOutput output = AlexaOutput
                .ask("IntentWithOneUtteranceAndOneReprompt")
                .putSlot("credits", 123, AlexaOutputFormat.NUMBER)
                .putState(model).build();

        final AlexaSpeechletResponse response = new AlexaSpeechletResponse(output);
        Assert.assertEquals(output, response.getOutput());
        Assert.assertEquals("<speak>Hello <say-as interpret-as=\"spell-out\">Paul</say-as>. Your current score is <say-as interpret-as=\"number\">123</say-as></speak>",
                ((SsmlOutputSpeech)response.getOutputSpeech()).getSsml());
        Assert.assertNull(response.getReprompt());
    }

    @Test
    public void getResponseWithReprompt() throws Exception {
        final AlexaOutput output = AlexaOutput
                .ask("IntentWithNoSlots")
                .withReprompt(true)
                .build();

        final AlexaSpeechletResponse response = new AlexaSpeechletResponse(output);
        Assert.assertNotNull(response.getReprompt());
        Assert.assertEquals("<speak>Hello again</speak>",
                ((SsmlOutputSpeech)response.getReprompt().getOutputSpeech()).getSsml());
    }

    @Test
    public void getResponseWithCard() throws Exception {
        final StandardCard card = new StandardCard();
        final AlexaOutput output = AlexaOutput
                .ask("IntentWithNoSlots")
                .withCard(card)
                .build();

        final AlexaSpeechletResponse response = new AlexaSpeechletResponse(output);
        Assert.assertEquals(card, response.getCard());
    }

    @Test
    public void getResponseWithoutSlots() throws Exception {
        final AlexaOutput output = AlexaOutput
                .ask("IntentWithNoSlots").build();

        final AlexaSpeechletResponse response = new AlexaSpeechletResponse(output);
        Assert.assertEquals(output, response.getOutput());
        Assert.assertEquals("<speak>Hello there</speak>",
                ((SsmlOutputSpeech)response.getOutputSpeech()).getSsml());
    }

    @Test
    public void getResponseWithSlotMissingInOutput() throws Exception {
        final AlexaOutput output = AlexaOutput
                .ask("IntentWithOneUtteranceAndOneReprompt").build();

        exception.expect(NullPointerException.class);
        new AlexaSpeechletResponse(output);
    }
}