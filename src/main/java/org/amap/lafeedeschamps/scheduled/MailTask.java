package org.amap.lafeedeschamps.scheduled;

import org.amap.lafeedeschamps.service.DistributionPlaceService;
import org.amap.lafeedeschamps.service.DistributionService;
import org.amap.lafeedeschamps.service.MailService;
import org.amap.lafeedeschamps.service.dto.DistributionDTO;
import org.amap.lafeedeschamps.service.mapper.DistributionMapper;
import org.amap.lafeedeschamps.service.mapper.DistributionPlaceMapper;
import org.amap.lafeedeschamps.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class MailTask {

    private static final Logger log = LoggerFactory.getLogger(MailTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final DistributionService distributionService;

    private final DistributionPlaceService distributionPlaceService;

    private final MailService mailService;

    private final UserMapper userMapper;

    private final DistributionMapper distributionMapper;

    private final DistributionPlaceMapper distributionPlaceMapper;

    public MailTask(DistributionService distributionService,
                    MailService mailService,
                    UserMapper userMapper,
                    DistributionMapper distributionMapper,
                    DistributionPlaceService distributionPlaceService,
                    DistributionPlaceMapper distributionPlaceMapper) {
        this.distributionService = distributionService;
        this.mailService = mailService;
        this.userMapper = userMapper;
        this.distributionMapper = distributionMapper;
        this.distributionPlaceService = distributionPlaceService;
        this.distributionPlaceMapper = distributionPlaceMapper;
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void sendReminderEmail() {
        log.info("Sending reminder email for tomorrow distributions");
        LocalDate now = LocalDate.now();
        List<DistributionDTO> distributions = distributionService.findByDates(
            now.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
            now.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        log.info("There is {} distributions tomorrow", distributions.size());
        distributions.forEach(distributionDTO -> distributionDTO.getUsers().forEach(userDTO -> {
            mailService.sendReminderEmail(userMapper.userDTOToUser(userDTO),
                distributionMapper.toEntity(distributionDTO),
                distributionPlaceMapper.toEntity(distributionPlaceService.findOne(distributionDTO.getPlaceId()).get()));
        }));
    }

}
