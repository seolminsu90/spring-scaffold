package com.admin.tool;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringApplication {
    /*  각종 설정 값(default) 목록
         https://www.atomikos.com/Documentation/JtaProperties

         com.atomikos.icatch.oltp_max_retries = 5
         com.atomikos.icatch.log_base_dir = ./
         com.atomikos.icatch.tm_unique_name = 192.168.219.110.tm
         com.atomikos.icatch.default_jta_timeout = 10000
         com.atomikos.icatch.serial_jta_transactions = true
         com.atomikos.icatch.allow_subtransactions = true
         com.atomikos.icatch.automatic_resource_registration = true
         com.atomikos.icatch.log_base_name = tmlog
         com.atomikos.icatch.oltp_retry_interval = 10000
         com.atomikos.icatch.checkpoint_interval = 500
         com.atomikos.icatch.default_max_wait_time_on_shutdown = 9223372036854775807
         com.atomikos.icatch.client_demarcation = false
         com.atomikos.icatch.forget_orphaned_log_entries_delay = 86400000
         com.atomikos.icatch.trust_client_tm = false
         com.atomikos.icatch.force_shutdown_on_vm_exit = false
         com.atomikos.icatch.rmi_export_class = none
         com.atomikos.icatch.enable_logging = true
         com.atomikos.icatch.max_timeout = 300000
         com.atomikos.icatch.threaded_2pc = false
         com.atomikos.icatch.recovery_delay = 10000
         com.atomikos.icatch.max_actives = 50
     */
    public static void main(String[] args) {
        System.setProperty("com.atomikos.icatch.max_actives", "-1");
        System.setProperty("com.atomikos.icatch.enable_logging", "false");

        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }

}
