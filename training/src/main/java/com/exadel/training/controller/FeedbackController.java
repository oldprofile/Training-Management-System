package com.exadel.training.controller;

import com.exadel.training.controller.model.feedbackModels.AddFeedbackModel;
import com.exadel.training.controller.model.feedbackModels.FeedbackModel;
import com.exadel.training.dao.domain.Feedback;
import com.exadel.training.service.FeedbackService;
import com.exadel.training.validate.annotation.LegalID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayudovin on 20.10.2015.
 */
@RestController
@RequestMapping("/feedback_controller")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @Secured({"ADMIN", "USER"})
    @RequestMapping(value = "/feedbacks_of_user/{idUser}", method = RequestMethod.GET)
    @LegalID
    public List<FeedbackModel> getFeedbacListOfUser(@PathVariable("idUser") long idUser) {
         List<FeedbackModel> feedbackModelList = new ArrayList<FeedbackModel>();

        for(Feedback feedback : feedbackService.getFeedbackListForUser(idUser)) {
            FeedbackModel feedbackModel = new FeedbackModel(feedback);
            feedbackModelList.add(feedbackModel);

        }

        return feedbackModelList;
    }

    @Secured({"ADMIN", "USER", "EX_COACH"})
    @RequestMapping(value = "/add_feedback", method = RequestMethod.POST, consumes = "application/json")
    public void addFeedback(@RequestBody AddFeedbackModel addFeedbackModel) {
        feedbackService.addFeedback(addFeedbackModel);
    }
}
