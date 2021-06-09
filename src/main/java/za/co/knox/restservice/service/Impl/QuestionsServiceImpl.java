package za.co.knox.restservice.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.entity.ScreeningQuestionsEntity;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.repository.ScreeningQuestionsRepository;
import za.co.knox.restservice.service.QuestionsService;
import za.co.knox.restservice.utils.LoggerUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@Slf4j
public class QuestionsServiceImpl implements QuestionsService {

   private ScreeningQuestionsRepository screeningQuestionsRepository;

    @Autowired
    public QuestionsServiceImpl(ScreeningQuestionsRepository screeningQuestionsRepository) {
        this.screeningQuestionsRepository = screeningQuestionsRepository;
    }

    @Override
    public List<ScreeningQuestionsEntity> getScreeningQuestions() throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();

        try {
            log.info(loggerUtil.start("Getting screening questions ", null));
            List<ScreeningQuestionsEntity> screeningQuestionsEntityList = StreamSupport.stream(screeningQuestionsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            log.info(loggerUtil.end("Getting screening questions ", null));
            return screeningQuestionsEntityList;

        } catch (DataAccessException e) {
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_SCREENING_QUESTIONS  );
        }
    }
}
