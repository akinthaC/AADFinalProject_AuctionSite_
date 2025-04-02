package lk.ijse.aadfinalproject_auctionsite_;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AadFinalProjectAuctionSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(AadFinalProjectAuctionSiteApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
