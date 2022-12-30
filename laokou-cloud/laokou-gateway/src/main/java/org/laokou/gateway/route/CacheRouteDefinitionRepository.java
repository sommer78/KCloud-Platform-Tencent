/**
 * Copyright (c) 2022 KCloud-Platform-Tencent Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.gateway.route;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.gateway.constant.GatewayConstant;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Kou Shenhai
 */
@Component
@Slf4j
public class CacheRouteDefinitionRepository implements RouteDefinitionRepository {

    private ReactiveRedisTemplate<String, RouteDefinition> reactiveRedisTemplate;
    private ReactiveValueOperations<String, RouteDefinition> routeDefinitionReactiveValueOperations;
    private Cache<String,RouteDefinition> caffeineCache;

    public CacheRouteDefinitionRepository(ReactiveRedisTemplate<String, RouteDefinition> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.routeDefinitionReactiveValueOperations = reactiveRedisTemplate.opsForValue();
        this.caffeineCache = Caffeine.newBuilder().initialCapacity(30)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .maximumSize(300)
                .build();
    }

    public void freshRouter(String rule) {
        List<RouteDefinition> routeDefinitionList = JacksonUtil.toList(rule, RouteDefinition.class);
        Map<String, RouteDefinition> routeDefinitionMap = routeDefinitionList.stream().collect(Collectors.toMap(k -> createKey(k.getId()), v -> v));
        caffeineCache.invalidateAll();
        routeDefinitionReactiveValueOperations.delete(this.createKey("*")).subscribe();
        routeDefinitionReactiveValueOperations.multiSet(routeDefinitionMap).subscribe();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        Collection<RouteDefinition> routeDefinitions = caffeineCache.asMap().values();
        if (CollectionUtils.isNotEmpty(routeDefinitions)) {
            return Flux.fromIterable(routeDefinitions);
        }
        return this.reactiveRedisTemplate.keys(this.createKey("*")).flatMap((key) -> this.routeDefinitionReactiveValueOperations.get(key))
                .doOnNext(definition -> caffeineCache.put(definition.getId(),definition))
                .onErrorContinue((throwable, routeDefinition) -> {
            if (log.isErrorEnabled()) {
                log.error("get routes from redis error cause : {}", throwable.toString(), throwable);
            }
        });
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap((routeDefinition) -> this.routeDefinitionReactiveValueOperations.set(this.createKey(routeDefinition.getId()), routeDefinition)
                .doOnNext(result -> caffeineCache.invalidateAll())
                .flatMap((success) -> success ? Mono.empty()
                        : Mono.defer(() -> Mono.error(new RuntimeException(String.format("Could not add route to redis repository: %s", routeDefinition))))));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap((id) -> this.routeDefinitionReactiveValueOperations.delete(this.createKey(id))
                .doOnNext(result -> caffeineCache.invalidateAll())
                .flatMap((success) -> success ? Mono.empty()
                        : Mono.defer(() -> Mono.error(new NotFoundException(String.format("Could not remove route from redis repository with id: %s", routeId))))));
    }

    private String createKey(String routeId) {
        return GatewayConstant.REDIS_DYNAMIC_ROUTER_RULE_KEY + ":" + routeId;
    }

}
