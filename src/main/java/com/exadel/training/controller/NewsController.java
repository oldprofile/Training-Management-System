package com.exadel.training.controller;

import com.exadel.training.controller.model.NewsModel;
import com.exadel.training.controller.model.PageModel;
import com.exadel.training.dao.domain.Comment;
import com.exadel.training.dao.domain.Feedback;
import com.exadel.training.dao.domain.News;
import com.exadel.training.dao.domain.Training;
import com.exadel.training.service.CommentService;
import com.exadel.training.service.FeedbackService;
import com.exadel.training.service.NewsService;
import com.exadel.training.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FeedbackService feedbackService;

    @Secured({"ADMIN"})
    @MessageMapping("/news")
    @SendTo("/pipe/news")
//    @RequestMapping("/news")
//    @ResponseBody
    public List<NewsModel> getNewsList(@RequestBody PageModel pageModel) {
        List<NewsModel> newsModelList = new ArrayList<NewsModel>();
        for(News news : newsService.getNewsList(pageModel.getPage(), pageModel.getPage_size())) {
            NewsModel newsModel = new NewsModel(news);
            switch(news.getTableName()) {
                case TRAINING:
                    Training training = trainingService.getTraining(news.getIdRow());
                    newsModel.setTitle(training.getTitle());
                    break;
                case COMMENT:
                    Comment comment = commentService.getComment(news.getIdRow());
                    newsModel.setTitle(comment.getTraining().getTitle());
                    break;
                case FEEDBACK:
                    Feedback feedback = feedbackService.getFeedback(news.getIdRow());
                    newsModel.setTitle(feedback.getTraining().getTitle());
            }
            newsModelList.add(newsModel);
        }
        return newsModelList;
    }
}