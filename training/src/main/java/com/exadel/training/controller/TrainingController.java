package com.exadel.training.controller;

import com.exadel.training.controller.model.CommentModel;
import com.exadel.training.controller.model.ListenerModel;
import com.exadel.training.controller.model.TrainingModel;
import com.exadel.training.dao.domain.*;
import com.exadel.training.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private LessonService lessonService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    TrainingModel getTrainingMadel(@PathVariable("id") long trainingId) {
        TrainingModel trainingModel = new TrainingModel();
        Training training = trainingService.getTraining(trainingId);
        trainingModel.setTraining(training);
        trainingModel.setStartDate(lessonService.getStartDateByTraining(trainingId));
        trainingModel.setEndDate(lessonService.getEndDateByTraining(trainingId));
        return trainingModel;
    }

    @RequestMapping(value = "/{id}/lesson_list", method = RequestMethod.GET)
    List<Lesson> getLessonListByTraining(@PathVariable("id") long trainingId) {
        return lessonService.getLessonByTraining(trainingId);
    }

    @RequestMapping(value = "/{id}/listener_list")
    List<ListenerModel> getListenerList(@PathVariable("id") long trainingId) {
        List<Listener> listenerList = trainingService.getListenerListRecord(trainingId);
        List<ListenerModel> listenerModelList = new ArrayList<ListenerModel>();
        for (Listener listener : listenerList) {
            listenerModelList.add(new ListenerModel(listener));
        }
        return listenerModelList;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    void createTraining(@RequestBody AddingTrainingModel addingTrainingModel) {
        trainingService.createTraining(addingTrainingModel.getCoachId()
                , addingTrainingModel.getTitle()
                , addingTrainingModel.getDescription()
                , addingTrainingModel.getShortInfo()
                , addingTrainingModel.getLanguage()
                , addingTrainingModel.getMaxSize()
                , addingTrainingModel.isInner()
                , addingTrainingModel.getTagList()
                , addingTrainingModel.getAdditionalInfo()
                , addingTrainingModel.getIsRepeating()
                , addingTrainingModel.getLessonList()
                , addingTrainingModel.getRepeatModel());

    }

    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.POST)
    void confirmAddTraining(@PathVariable("id") long trainingId, @RequestBody AddingTrainingModel addingTrainingModel) {
        trainingService.confirmTraining(trainingId
                , addingTrainingModel.getTitle()
                , addingTrainingModel.getDescription()
                , addingTrainingModel.getShortInfo()
                , addingTrainingModel.getLanguage()
                , addingTrainingModel.getMaxSize()
                , addingTrainingModel.isInner()
                , addingTrainingModel.getTagList()
                , addingTrainingModel.getAdditionalInfo()
                , addingTrainingModel.getLessonList()
                , addingTrainingModel.getRepeatModel());
    }

    @RequestMapping(value = "cancel_create/{id}", method = RequestMethod.PUT)
    void cancelCreate(@PathVariable("id") Long trainingId) {
        trainingService.cancelCreate(trainingId);
    }

    @RequestMapping(value = "cancel_change/{id}", method = RequestMethod.PUT)
    void cancelChange(@PathVariable("id") Long trainingId) {
        trainingService.cancelChange(trainingId);
    }

    @RequestMapping(value = "/getAdd", method = RequestMethod.GET)
    AddingTrainingModel getAdd() {
        AddingTrainingModel addingTrainingModel = new AddingTrainingModel();
        List<LessonModel> lessonList = new ArrayList<LessonModel>();
        lessonList.add(new LessonModel());
        addingTrainingModel.setLessonList(lessonList);
        RepeatModel repeatModel = new RepeatModel();
        repeatModel.setLessonList(new LessonModel[7]);
        addingTrainingModel.setRepeatModel(repeatModel);
        return addingTrainingModel;
    }

    @RequestMapping(value = "/add_tag", method = RequestMethod.POST)
    public void addTag(@RequestBody Tag tag) {
        tagService.addTag(tag);
    }

    @RequestMapping(value = "/tag_list", method = RequestMethod.GET)
    public List<Tag> getTagList() {
        return tagService.getTagList();
    }

    //TODO: /training_list RequestParam - isActual, List<Tag>

    @RequestMapping(value = "/training/{id}/add_comment")
    public void addComment(@PathVariable("id") Long trainingId, @RequestBody CommentModel commentModel) {
        Comment comment = new Comment();
        comment.setClear(commentModel.getClear());
        comment.setCreativity(commentModel.getCreativity());
        comment.setEffective(commentModel.getEffective());
        comment.setInteresting(commentModel.getInteresting());
        comment.setNewMaterial(commentModel.getNewMaterial());
        comment.setRecommendation(commentModel.getRecommendation());
        comment.setOther(commentModel.getOther());
        comment.setDate(new Date().getTime());
        //Get current user
        User user = new User();
        comment.setUser(user);
        comment.setTraining(trainingService.getTraining(trainingId));
        commentService.addComment(comment);
    }

    @RequestMapping(value = "training/{trainingId}/remove_comment/{commentId}")
    public void removeComment(@PathVariable("trainingId") Long trainingId,
                              @PathVariable("commentId") Long commentId) {
        commentService.removeComment(commentId);
    }

    @RequestMapping(value = "/training/{id}/comment_list")
    public List<CommentModel> getTrainingCommentList(@PathVariable("id") Long trainingId) {
        List<Comment> commentList = commentService.getTrainingCommentList(trainingId);
        List<CommentModel> commentModelList = new ArrayList<CommentModel>();
        for (Comment comment : commentList) {
            CommentModel commentModel = new CommentModel();
            commentModel.setClear(comment.getClear());
            commentModel.setCreativity(comment.getCreativity());
            commentModel.setEffective(comment.getEffective());
            commentModel.setInteresting(comment.getInteresting());
            commentModel.setNewMaterial(comment.getNewMaterial());
            commentModel.setRecommendation(comment.getRecommendation());
            commentModel.setOther(comment.getOther());
            commentModel.setDate(comment.getDate());
            commentModel.setIsPositive(comment.getIsPositive());
            commentModel.setIsDeleted(comment.getIsDeleted());
            commentModel.setId(comment.getId());
            commentModel.setUserId(comment.getUser().getId());
            commentModel.setUserName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
            commentModelList.add(commentModel);
        }
        return commentModelList;
    }

    @RequestMapping(value = "/user/{id}/coach_comment_list")
    public List<CommentModel> getCoachCommentList(@PathVariable("id") Long coachId) {
        List<Comment> commentList = commentService.getCoachCommentList(coachId);
        List<CommentModel> commentModelList = new ArrayList<CommentModel>();
        for (Comment comment : commentList) {
            CommentModel commentModel = new CommentModel();
            commentModel.setClear(comment.getClear());
            commentModel.setCreativity(comment.getCreativity());
            commentModel.setEffective(comment.getEffective());
            commentModel.setInteresting(comment.getInteresting());
            commentModel.setNewMaterial(comment.getNewMaterial());
            commentModel.setRecommendation(comment.getRecommendation());
            commentModel.setOther(comment.getOther());
            commentModel.setDate(comment.getDate());
            commentModel.setIsPositive(comment.getIsPositive());
            commentModel.setIsDeleted(comment.getIsDeleted());
            commentModel.setId(comment.getId());
            commentModel.setUserId(comment.getUser().getId());
            commentModel.setUserName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
            commentModelList.add(commentModel);
        }
        return commentModelList;
    }

    @RequestMapping(value = "/user/{id}/comment_list")
    public List<CommentModel> getUserCommentList(@PathVariable("id") Long userId) {
        List<Comment> commentList = commentService.getUserCommentList(userId);
        List<CommentModel> commentModelList = new ArrayList<CommentModel>();
        for (Comment comment : commentList) {
            CommentModel commentModel = new CommentModel();
            commentModel.setClear(comment.getClear());
            commentModel.setCreativity(comment.getCreativity());
            commentModel.setEffective(comment.getEffective());
            commentModel.setInteresting(comment.getInteresting());
            commentModel.setNewMaterial(comment.getNewMaterial());
            commentModel.setRecommendation(comment.getRecommendation());
            commentModel.setOther(comment.getOther());
            commentModel.setDate(comment.getDate());
            commentModel.setIsPositive(comment.getIsPositive());
            commentModel.setIsDeleted(comment.getIsDeleted());
            commentModel.setId(comment.getId());
            commentModel.setUserId(comment.getUser().getId());
            commentModel.setUserName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
            commentModelList.add(commentModel);
        }
        return commentModelList;
    }
}
