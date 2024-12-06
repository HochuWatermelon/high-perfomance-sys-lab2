package com.company.monolitservice.model.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import com.company.monolitservice.model.entity.embedded.WaitingWorkerId;

@Embeddable
@Data
@Entity
@Table(name = "waiting_worker")
public class WaitingWorkerEntity {
    @EmbeddedId
    WaitingWorkerId waitingWorkerId;

    @ManyToOne
    @MapsId("workerId")
    @JoinColumn(name = "worker_id")
    private WorkerEntity worker;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public  WaitingWorkerEntity() {waitingWorkerId = new WaitingWorkerId();}

    public WaitingWorkerEntity(WorkerEntity worker, OrderEntity order) {
        this.worker = worker;
        this.order = order;
        this.waitingWorkerId = new WaitingWorkerId(worker.getId(), order.getId());
    }

    public void setWorker(WorkerEntity worker) {
        this.worker = worker;
        this.waitingWorkerId.setWorkerId(worker.getId());
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
        this.waitingWorkerId.setOrderId(order.getId());
    }


}
