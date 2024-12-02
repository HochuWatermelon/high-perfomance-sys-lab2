package com.company.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.authservice.exceptions.WaitingWorkerNotFoundException;
import com.company.authservice.model.entity.WaitingWorkerEntity;
import com.company.authservice.repository.WaitingWorkerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingWorkerService {
    private final WaitingWorkerRepository waitingWorkerRepository;

    public WaitingWorkerEntity findWaitingWorkerByWorkerIdAndOrderIdAndOrderCustomerId(UUID workerId, UUID orderId, UUID customerId){
        return waitingWorkerRepository.findByWorkerIdAndOrderIdAndOrderCustomerId(workerId, orderId, customerId).orElseThrow(() ->
                new WaitingWorkerNotFoundException(String.format("Работник с id = %s не находится в списке ожидания заказа с id = %s заказчика с id = %s", workerId, orderId, customerId)));
    }

    public void deleteWaitingWorkersByOrderId(UUID orderId){
        List<WaitingWorkerEntity> waitingWorkers = waitingWorkerRepository.findByOrderId(orderId);
        waitingWorkerRepository.deleteAll(waitingWorkers);
    }

    public void saveWaitingWorker(WaitingWorkerEntity waitingWorker){
        waitingWorkerRepository.save(waitingWorker);
    }

    public void deleteWaitingWorker(WaitingWorkerEntity waitingWorker){
        waitingWorkerRepository.delete(waitingWorker);
    }

    public List<WaitingWorkerEntity> findByOrderCustomerIdAndOrderId(UUID customerId, UUID orderId){
        return waitingWorkerRepository.findByOrderCustomerIdAndOrderId(customerId, orderId);
    }

    public Page<WaitingWorkerEntity> findByOrderCustomerIdAndOrderId(UUID customerId, UUID orderId, Pageable pageable){
        return waitingWorkerRepository.findAllByOrderCustomerIdAndOrderId(customerId, orderId, pageable);
    }
}
