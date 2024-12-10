package com.company.orderservice.model.entity;


import com.company.orderservice.model.entity.embedded.WaitingWorkerId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Embeddable
@Data
@Entity
@Table(name = "waiting_worker")
public class WaitingWorkerEntity {
    @EmbeddedId
    WaitingWorkerId waitingWorkerId;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public  WaitingWorkerEntity() {waitingWorkerId = new WaitingWorkerId();}

    public WaitingWorkerEntity(UUID workerId, OrderEntity order) {
        this.order = order;
        this.waitingWorkerId = new WaitingWorkerId(workerId, order.getId());
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
        this.waitingWorkerId.setOrderId(order.getId());
    }

}
