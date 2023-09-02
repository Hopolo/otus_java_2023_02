package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("unchecked")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Can't get config class instance", e);
        }
        Method[] methodsConfigClass = configClass.getDeclaredMethods();
        for (int i = 0; i < methodsConfigClass.length; i++) {
            for (Method method : methodsConfigClass) {
                Optional<AppComponent>
                    componentAnnotation =
                    Optional.ofNullable(method.getDeclaredAnnotationsByType(AppComponent.class)[0]);
                int curOrder = i;
                componentAnnotation.filter(appComponent -> appComponent.order() == curOrder).ifPresent(appComponent -> {
                    if (appComponentsByName.keySet().stream().anyMatch(compName -> compName.equals(appComponent.name().toLowerCase()))) {
                        throw new RuntimeException("Duplicated component name");
                    }
                });
                componentAnnotation.filter(appComponent -> appComponent.order() == curOrder)
                    .ifPresent(appComponent -> addToContainer(
                        appComponent.name(),
                        configInstance,
                        method
                    ));
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        long countComponentByClass = appComponents.stream().filter(o -> componentClass.isAssignableFrom(o.getClass())).count();
        if (countComponentByClass == 0) {
            throw new RuntimeException("Can't get app component");
        }
        if (countComponentByClass > 1) {
            throw new RuntimeException("Duplicated class component");
        }
        return (C) appComponents.stream().filter(o -> componentClass.isAssignableFrom(o.getClass())).findAny().get();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        List<String>
            listOfComponentNames =
            appComponentsByName.keySet().stream().filter(key -> key.equals(componentName.toLowerCase())).toList();
        if (listOfComponentNames.isEmpty()) {
            throw new RuntimeException("App component doesn't exist");
        }
        return (C) appComponentsByName.get(listOfComponentNames.get(0));
    }

    private void addToContainer(
        String nameComponent,
        Object configInstance,
        Method method
    ) {
        List<Object> args = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            Class<?> paramType = parameter.getType();
            Object argComponent = Optional.ofNullable(getAppComponent(paramType)).orElseThrow(() -> new RuntimeException("Can't get arg "
                                                                                                                             + "component"));
            args.add(argComponent);
        }
        try {
            Object appComponent = method.invoke(configInstance, args.toArray(new Object[0]));
            appComponents.add(appComponent);
            appComponentsByName.put(nameComponent.toLowerCase(), appComponent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't add app component", e);
        }
    }

}
