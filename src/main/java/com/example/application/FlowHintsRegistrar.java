package com.example.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.container.JSR356AsyncSupport;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.DefaultAtmosphereResourceFactory;
import org.atmosphere.cpr.DefaultAtmosphereResourceSessionFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.atmosphere.cpr.DefaultBroadcasterFactory;
import org.atmosphere.cpr.DefaultMetaBroadcaster;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.SuspendTrackerInterceptor;
import org.atmosphere.util.SimpleBroadcaster;
import org.atmosphere.util.VoidAnnotationProcessor;
import org.atmosphere.websocket.protocol.SimpleHttpProtocol;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import com.vaadin.flow.di.LookupInitializer;
import com.vaadin.flow.router.DefaultRoutePathProvider;
import com.vaadin.flow.server.startup.DefaultApplicationConfigurationFactory;

public class FlowHintsRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DefaultApplicationConfigurationFactory.class);
        classes.add(DefaultRoutePathProvider.class);
        classes.add(LookupInitializer.class);
        try {
            classes.add(Class.forName("com.vaadin.flow.di.LookupInitializer$ResourceProviderImpl"));
            classes.add(Class.forName("com.vaadin.flow.di.LookupInitializer$LookupImpl"));
            classes.add(Class.forName("com.vaadin.flow.di.LookupInitializer$RegularOneTimeInitializerPredicate"));
            classes.add(Class.forName("com.vaadin.flow.di.LookupInitializer$StaticFileHandlerFactoryImpl"));
            classes.add(Class.forName("com.vaadin.flow.di.LookupInitializer$AppShellPredicateImpl"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        hints.resources().registerPattern("META-INF/VAADIN/*"); // Bundles, build info etc
        hints.resources().registerPattern("com/vaadin/flow/server/*");

        ReflectionHints ref = hints.reflection();
        try {
            for (Class<?> c : classes) {
                ref.registerType(c, MemberCategory.values());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
