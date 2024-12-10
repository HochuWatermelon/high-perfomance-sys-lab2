package com.company.orderservice.service.clients;

import com.company.orderservice.configurations.feign.FeignConfiguration;
import com.company.workerservice.model.dto.profession.ProfessionDto;
import com.company.workerservice.model.dto.profession.WorkerProfessionDto;
import com.company.workerservice.model.dto.worker.WorkerDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(value = "${spring.cloud.openfeign.worker-service.name}", configuration = FeignConfiguration.class)
public interface WorkerServiceClient {

    @Headers("Content-Type: application/json")
    @RequestMapping(method = RequestMethod.GET, value = "/profession/{name}")
    ProfessionDto getProfessionByName(@PathVariable("name") String name);

    @Headers("Content-Type: application/json")
    @RequestMapping(method = RequestMethod.GET, value = "/worker-profession/{workerId}")
    WorkerProfessionDto getWorkerProfessionByWorkerId(@PathVariable("workerId") UUID workerId);

    @Headers("Content-Type: application/json")
    @RequestMapping(method = RequestMethod.GET, value = "/worker/{workerId}")
    WorkerDto getWorkerByWorkerId(@PathVariable("workerId") UUID workerId);

    @Headers("Content-Type: application/json")
    @RequestMapping(method = RequestMethod.PATCH, value = "/worker")
    void saveWorker(@RequestBody WorkerDto workerDto);

}
