package za.co.knox.restservice.service;


import za.co.knox.restservice.entity.ScreeningQuestionsEntity;
import za.co.knox.restservice.exceptions.ServiceException;

import java.util.List;

public interface QuestionsService {

    List<ScreeningQuestionsEntity> getScreeningQuestions() throws ServiceException;
}
