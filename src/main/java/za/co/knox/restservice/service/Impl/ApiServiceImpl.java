package za.co.knox.restservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.domain.Api;
import za.co.knox.restservice.entity.ApiEntity;
import za.co.knox.restservice.repository.ApiRepository;
import za.co.knox.restservice.service.ApiService;


@Service
public class ApiServiceImpl implements ApiService {



   private ApiRepository apiRepository;

    @Autowired
    public ApiServiceImpl(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Override
    public Api getVersion() {
        Api api = new Api();

        ApiEntity apiEntity = apiRepository.findById(1).get();

        api.setApiVersion("Api Version: " + apiEntity.getApiVersion());

        return api;
    }
}
