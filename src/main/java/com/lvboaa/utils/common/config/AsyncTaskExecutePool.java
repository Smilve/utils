package com.lvboaa.utils.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncTaskExecutePool implements AsyncConfigurer {
    private Logger logger = LoggerFactory.getLogger(AsyncTaskExecutePool.class);

    @Autowired
    private AsyncThreadPoolConfig config;

    @Bean("mainExecutor")
    @Override  
    public Executor getAsyncExecutor() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(config.getCorePoolSize());    
        executor.setMaxPoolSize(config.getMaxPoolSize());    
        executor.setQueueCapacity(config.getQueueCapacity());    
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());    
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        //队列满时主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  
        executor.initialize();    
        return executor;    
    }

    /* (非 Javadoc) 
    * Title: getAsyncUncaughtExceptionHandler 
    * Description:  对于返回值是void，异常会被AsyncUncaughtExceptionHandler处理掉;
    * 其它返回对象，不会被AsyncUncaughtExceptionHandler处理，需要我们在方法中捕获异常并处理
    * @return 
    * @see org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncUncaughtExceptionHandler() 
    */
    @Override  
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {// 异步任务中异常处理  
        return new AsyncUncaughtExceptionHandler() {  
            @Override  
            public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {  
            	logger.info("Exception message - " + throwable.getMessage());  
            	logger.info("Method name - " + method.getName());  
                for (Object param : obj) {  
                	logger.info("Parameter value - " + param);  
                } 
            }
        };  
    }
}
