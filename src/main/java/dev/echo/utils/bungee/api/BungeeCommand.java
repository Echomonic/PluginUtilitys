package dev.echo.utils.bungee.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface BungeeCommand {


    @Deprecated
    String name() default "";


    String[] aliases();
    String desc();


    
    int min() default 1;

    
    int max() default 1;

    /**
     * <p>
     *     Use isPermission from CommandContext Class.
     * </p>
     *
     * @return perm
     */
    
    String perm() default "";

    
    boolean console() default false;

    
    boolean tab() default false;

}
