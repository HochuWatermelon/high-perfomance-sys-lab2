package com.company.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import com.company.authservice.model.entity.embedded.ProfessionWorkerId;

@Embeddable
@Data
@Entity
@Table(name = "worker_profession")
public class WorkerProfessionEntity {
    @EmbeddedId
    ProfessionWorkerId professionWorkerId;

    @ManyToOne
    @MapsId("workerId")
    @JoinColumn(name = "worker_id")
    private WorkerEntity worker;

    @ManyToOne
    @MapsId("professionId")
    @JoinColumn(name = "profession_id")
    private ProfessionEntity profession;

    @Column(nullable = false, length = 32)
    private String rank;

    public WorkerProfessionEntity(){ professionWorkerId = new ProfessionWorkerId(); }

    public WorkerProfessionEntity(WorkerEntity worker, ProfessionEntity profession, String rank) {
        this.profession = profession;
        this.worker = worker;
        this.rank = rank;
        this.professionWorkerId = new ProfessionWorkerId(worker.getId(), profession.getId());
    }

    public void setWorker(WorkerEntity worker) {
        this.worker = worker;
        this.professionWorkerId.setWorkerId(worker.getId());
    }

    public void setProfession(ProfessionEntity profession) {
        this.profession = profession;
        this.professionWorkerId.setProfessionId(profession.getId());
    }
}
