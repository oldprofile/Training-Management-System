package com.exadel.training.service.impl;

import com.exadel.training.dao.LessonDAO;
import com.exadel.training.dao.domain.Lesson;
import com.exadel.training.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService{

    @Autowired
    private LessonDAO lessonDAO;

    @Override
    public List<Lesson> getLessonByTraining(long trainingId) {
        return lessonDAO.getLessonListByTraining(trainingId);
    }

    @Override
    public Long getStartDateByTraining(long trainingId) {
        return lessonDAO.getStartDateByTraining(trainingId);
    }

    @Override
    public Long getEndDateByTraining(long trainingId) {
        return lessonDAO.getEndDateByTraining(trainingId);
    }
}