package com.Notification.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Status {

    @Id
    private Long id;

    private String status;

    public enum StatusLoad{

        CANCELED(1L, "canceled"),
        ERROR(2L, "error"),
        PENDING(3L, "pending"),
        SUCCESS(4L, "success");

        private Long id;
        private String statusResponse;

        StatusLoad(Long id, String statusRequest){
            this.id = id;
            this.statusResponse = statusRequest;
        }

        public Status toStatus(){
            return new Status(id, statusResponse);
        }
    }
}
