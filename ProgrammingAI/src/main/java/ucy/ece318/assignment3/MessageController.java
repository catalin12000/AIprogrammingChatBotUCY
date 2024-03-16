package ucy.ece318.assignment3;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository repository;
    private String token = "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";
    CompletionRequest request;
    String model ="text-davinci-002";
    OpenAiService service;
    CompletionResult response;
    @GetMapping("/MessageList")
    public Iterable<Message> getMessages() {
        // change return null to something more appropriate
        return repository.findAll();

    }
    @GetMapping("/addMessageandResponse")
    public RedirectView addMessageandResponse(@RequestParam final String message) {

      /*
      1. You need to create a message object and save it into the repository
      2. Make an api request to OpenAI with the message’s text and receive the answer.
      3. Create a new message object with the AI response and save it to the  repository.

        Remember, that you need to set the type  of the message as “Question”
        for the message of the user and “Answer”

       */

        Message message1 = new Message();
        message1.setMessageText(message);
        repository.save(message1);


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String strDate= formatter.format(date);
        message1.setMessageDate(strDate);
        message1.setMessageType("Question");


        service = new OpenAiService(token);
        request = new CompletionRequest();

        request.setModel(model);
        request.setPrompt(message);
        response = service.createCompletion(request);

        List<CompletionChoice> responses = response.getChoices();

        Message message2=new Message();
        message2.setMessageType("Answer");

        for(CompletionChoice c: responses){
            System.out.println(c.getText());
            message2.setMessageText(c.getText());
            repository.save(message2);
        }



        return new RedirectView("/");
    }
    @GetMapping("/deleteMessage")
    public RedirectView deleteMessage(@RequestParam Integer id) {
        // add code to delete message
        repository.deleteById(id);

        return new RedirectView("");

    }

}