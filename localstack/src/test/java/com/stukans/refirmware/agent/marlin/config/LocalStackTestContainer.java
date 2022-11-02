package com.stukans.refirmware.agent.marlin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

public class LocalStackTestContainer implements InitializingBean, DisposableBean {

    private LocalStackContainer localStackContainer;
    private static final Logger log = LoggerFactory.getLogger(LocalStackTestContainer.class);

    @Override
    public void destroy() {
        if (null != localStackContainer) {
            localStackContainer.close();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (null == localStackContainer) {
            localStackContainer =
                new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.2.0"))
                    .withServices(LocalStackContainer.Service.S3)
                    .withLogConsumer(new Slf4jLogConsumer(log))
                    .withReuse(true)
            ;
        }
        if (!localStackContainer.isRunning()) {
            localStackContainer.start();
        }
    }

    public LocalStackContainer getLocalStackContainer() {
        return localStackContainer;
    }
}
