package com.company.monolitservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.monolitservice.exceptions.CustomerNotFoundException;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.customer.CustomerPostDto;
import com.company.monolitservice.model.dto.customer.CustomerPutDto;
import com.company.monolitservice.model.dto.pageable.PageableGetDto;
import com.company.monolitservice.model.dto.waitingworker.WaitingWorkerGetDto;
import com.company.monolitservice.model.dto.worker.WorkerDto;
import com.company.monolitservice.model.entity.CustomerEntity;
import com.company.monolitservice.model.entity.WaitingWorkerEntity;
import com.company.monolitservice.model.mapper.CustomerMapper;
import com.company.monolitservice.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final WaitingWorkerService waitingWorkerService;
    private final WorkerService workerService;

    public ResponseEntity<String> createCustomer(CustomerPostDto customerDto) {
        CustomerEntity customer = customerMapper.customerPostDtoToEntity(customerDto);
        customerRepository.save(customer);
        String customerName = customer.getSecondName() + " " + customer.getFirstName() + " " + customer.getMiddleName();
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(String.format("Заказчик %s был создан с id = %s", customerName, customer.getId()));
    }

    public CustomerEntity findCustomerById(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException(String.format("Заказчик с id = %s не был найден", customerId)));
    }

    @Transactional
    public ResponseEntity<String> updateCustomer(UUID customerId, CustomerPutDto customerDto) {
        CustomerEntity customerEntity = findCustomerById(customerId);
        customerMapper.updateEntityFromCustomerPutDto(customerEntity, customerDto);
        customerRepository.save(customerEntity);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(String.format("Заказчик с id = %s был изменён", customerId));
    }


    @Transactional
    public ResponseEntity<String> getWaitingWorkersForOrderByCustomerId(WaitingWorkerGetDto waitingWorkerGetDto) {
        CustomerSelfDto customerSelfDto = waitingWorkerGetDto.getCustomer_self_dto();
        PageableGetDto pageableDto = waitingWorkerGetDto.getPageable_get_dto();
        Pageable pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize());
        UUID customerId = customerSelfDto.getCustomer_id();
        UUID orderId = customerSelfDto.getOrder_id();
        List<WaitingWorkerEntity> waitingWorkers = waitingWorkerService.findByOrderCustomerIdAndOrderId(customerId, orderId);
        Page<WaitingWorkerEntity> waitingWorkersPage = waitingWorkerService.findByOrderCustomerIdAndOrderId(customerId, orderId, pageable);
        HttpHeaders headers = new HttpHeaders();
        if (!waitingWorkers.isEmpty()) {
            if (pageable.getPageNumber() >= waitingWorkersPage.getTotalPages()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("На этой странице данных нет, попробуйте меньшее значение page или size.");
            }
            String response = waitingWorkersPage.getContent().stream().map(waitingWorkerEntity -> {
                WorkerDto worker = workerService.workerToWorkerDto(waitingWorkerEntity.getWorker());
                return worker.toString();
            }).collect(Collectors.joining("\n\n"));
            headers.add("X-Total-Count", String.valueOf(waitingWorkers.size()));
            return ResponseEntity.status(HttpStatus.OK)
                                 .headers(headers)
                                 .body(response);
        } else {
            headers.add("X-Total-Count", "0");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .headers(headers)
                                 .body("По вашему заказу не найдены работники, желающие его выполнить.");
        }
    }

    public void saveCustomer(CustomerEntity customer){
        customerRepository.save(customer);
    }
}
