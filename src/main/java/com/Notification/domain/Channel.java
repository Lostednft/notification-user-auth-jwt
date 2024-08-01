package com.Notification.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_channel")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    private Long id;

    private String channel;

    public enum ChannelLoad{

        SMS(1L, "sms"),
        WHATSAPP(2L, "whatsapp"),
        EMAIL(3L, "email");

        private Long id;
        private String channelResponse;

        ChannelLoad(Long id, String channelRequest){

            this.id = id;
            this.channelResponse = channelRequest;
        }

        public Channel toChannel(){
            return new Channel(id, channelResponse);
        }
    }
}
