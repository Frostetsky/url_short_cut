package ru.shortcut.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.shortcut.app.aspect.AspectGlobalJComponentSpringBeans;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
@EnableAspectJAutoProxy
public class AppConfiguration {

    @Bean
    public AspectGlobalJComponentSpringBeans aspectGlobalJComponentSpringBeans() {
        var globalAspect = new AspectGlobalJComponentSpringBeans();
        log.info("{} has bean created.", globalAspect.getClass().getSimpleName());
        return globalAspect;
    }
}
