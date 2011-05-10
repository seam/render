package org.jboss.seam.render.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * Warning: ***DO NOT USE THIS CLASS***
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class BeanManagerUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getContextualInstance(final BeanManager manager, final Class<T> type) {
        T result = null;
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                result = (T) manager.getReference(bean, type, context);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void injectNonContextualInstance(final BeanManager manager, final Object instance) {
        if (instance != null) {
            CreationalContext<Object> creationalContext = manager.createCreationalContext(null);
            InjectionTarget<Object> injectionTarget = (InjectionTarget<Object>) manager
                    .createInjectionTarget(getAnnotatedType(manager, instance));
            injectionTarget.inject(instance, creationalContext);
        }
    }

    private static AnnotatedType<? extends Object> getAnnotatedType(final BeanManager manager, final Object instance) {
        AnnotatedType<?> result = null;
        result = manager.createAnnotatedType(instance.getClass());
        return result;
    }

}
