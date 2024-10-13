package com.study.dynamo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@Configuration
public class AwsConfig {


    @Bean
    //@Profile("!default")
    public AwsCredentialsProvider awsCredentialsProviderV2() {
        return DefaultCredentialsProvider.create();
    }

//    @Bean
//    @Profile("default")
//    public StsClient stsClient() {
//        return StsClient
//                .builder()
//                .region(Region.US_EAST_1)
//                .build();
//    }
//
//    @Bean
//    @Profile("default")
//    public AwsCredentialsProvider getSessionCredentialsAssumingRoleV2(StsClient stsClient) {
//        return StsGetSessionTokenCredentialsProvider.builder()
//                .stsClient(stsClient)
//                .build();
//    }
}
