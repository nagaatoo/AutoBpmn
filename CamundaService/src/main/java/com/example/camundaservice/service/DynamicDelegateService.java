package com.example.camundaservice.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.commons.utils.cache.Cache;
import org.jvnet.hk2.annotations.Service;

import groovy.lang.GroovyClassLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicDelegateService {

    private final Map<String, JavaDelegate> delegateCache = new ConcurrentHashMap<>();
    private final GroovyClassLoader groovyClassLoader;
    private final ProcessEngine processEngine;
    /**
         * Регистрирует Java делегат в Camunda без перезагрузки приложения
         * 
         * @param delegateId Идентификатор делегата (используется в BPMN)
         * @param javaCode Исходный код делегата
         * @return true если делегат успешно зарегистрирован
         */
        public boolean registerDelegate(String delegateId, String javaCode) {
            try {
                // Компилируем и создаем экземпляр делегата из строки кода
                Class<?> delegateClass = groovyClassLoader.parseClass(javaCode);
                JavaDelegate delegate = (JavaDelegate) delegateClass.getDeclaredConstructor().newInstance();
                
                // Кэшируем делегат
                delegateCache.put(delegateId, delegate);
                
                // Регистрируем делегат в ProcessEngine
                ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
                config.getBeans().put(delegateId, delegate);
                
                // Очищаем кэш определений процессов, чтобы изменения были применены
                // к следующим экземплярам процессов без перезагрузки
                DeploymentCache deploymentCache = config.getDeploymentCache();
                Cache<String, ProcessDefinitionEntity> processDefinitionCache = deploymentCache.getProcessDefinitionCache();
                processDefinitionCache.clear();
                // processDefinitionCache.forEach((key, definition) -> {
                //     definition.clearCache();
                //     log.info("Updating process definition cache for: {}", definition.getKey());
                // });
                
                log.info("Successfully registered delegate: {}", delegateId);
                return true;
                
            } catch (Exception e) {
                log.error("Failed to register delegate: {}", delegateId, e);
                return false;
            }
        }
        
        /**
         * Получает ранее зарегистрированный делегат
         */
        public JavaDelegate getDelegate(String delegateId) {
            return delegateCache.get(delegateId);
        }
        
        /**
         * Удаляет делегат из реестра
         */
        public boolean unregisterDelegate(String delegateId) {
            try {
                delegateCache.remove(delegateId);
                ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
                config.getBeans().remove(delegateId);
                return true;
            } catch (Exception e) {
                log.error("Failed to unregister delegate: {}", delegateId, e);
                return false;
            }
        }
}
