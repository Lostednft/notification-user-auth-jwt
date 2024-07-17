package com.Notification.config;

import com.Notification.domain.Channel;
import com.Notification.domain.Status;
import com.Notification.repositories.ChannelRepository;
import com.Notification.repositories.StatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final ChannelRepository channelRepository;
    private final StatusRepository statusRepository;


    public DataLoader(ChannelRepository channelRepository, StatusRepository statusRepository) {
        this.channelRepository = channelRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Arrays.stream(Status.StatusLoad.values())
                .map(Status.StatusLoad::toStatus)
                .forEach(statusRepository::save);

        Arrays.stream(Channel.ChannelLoad.values())
                .map(Channel.ChannelLoad::toChannel)
                .forEach(channelRepository::save);
    }
}
