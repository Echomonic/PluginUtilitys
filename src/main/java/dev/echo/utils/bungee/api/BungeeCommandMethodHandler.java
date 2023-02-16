package dev.echo.utils.bungee.api;

import dev.echo.utils.general.PluginInstance;
import dev.echo.utils.general.Color;
import dev.echo.utils.spigot.commands.CommandContext;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bukkit.entity.Player;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BungeeCommandMethodHandler {


    private static final HashMap<String, Object[]> commandMap = new HashMap<>();

    @Getter
    @Setter
    private static Class<?> tabHandler;

    @SneakyThrows
    @SuppressWarnings("deprecation")
    public static void getCommands(String fall, String pack) {
        System.out.println("This method was called");

        for (Class<?> a : new Reflections(pack, new SubTypesScanner(false)).getSubTypesOf(Object.class)) {
            System.out.println(a.getSimpleName());
            for (Method method : a.getDeclaredMethods()) {
                if (method.getDeclaredAnnotation(BungeeCommand.class) == null) {
                    continue;
                }
                BungeeCommand command = method.getDeclaredAnnotation(BungeeCommand.class);
                commandMap.put(command.aliases()[0], new Object[]{command, method});
            }
        }
        registerCommands(fall);
    }

    private static void registerCommands(String fall) {
        commandMap.forEach((s, adventureCommand) ->
        {
            PluginInstance.getBungeeClass()
                    .getProxy().getPluginManager().
                    registerCommand(PluginInstance.getBungeeClass(),new CommandClass((BungeeCommand) adventureCommand[0], (Method) adventureCommand[1]));
        });


        commandMap.clear();
    }
    public static void unregisterCommands() {
        PluginInstance.getBungeeClass().getProxy().getPluginManager().unregisterListeners(PluginInstance.getBungeeClass());
    }

    private static final class CommandClass extends Command implements TabExecutor {


        private final BungeeCommand commandAnn;
        private final Method method;

        private final HashMap<String, Method> methodCache = new HashMap<>();
        private final HashMap<String, Object> cacheClass = new HashMap<>();

        @SneakyThrows
        private CommandClass(BungeeCommand commandAnn, Method method) {


            super(commandAnn.aliases()[0], null, commandAnn.aliases());


            this.commandAnn = commandAnn;
            this.method = method;

            methodCache.put(commandAnn.aliases()[0], method);
            cacheClass.put(commandAnn.aliases()[0], method.getDeclaringClass().getConstructor().newInstance());
        }

        @Override
        public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {

            if (!commandAnn.console() && !(sender instanceof Player)) {
                sender.sendMessage(Color.c("&cYou must be a player to perform this command!"));
                return;
            }
            BungeeContext context = new BungeeContext(sender, args);
            context.setMax(commandAnn.max());
            context.setMin(commandAnn.min());
            command(context);
        }



        @SneakyThrows
        private void command(BungeeContext context) {
            method.setAccessible(true);
            handle(context);
            method.setAccessible(false);
        }


        @SneakyThrows
        private List<String> tab(BungeeContext context) {
            Method commandMethod = methodCache.get(commandAnn.aliases()[0]);
            if(tabHandler.getDeclaredMethod(commandMethod.getName(),BungeeContext.class) != null){
                Method tabMethod = tabHandler.getDeclaredMethod(commandMethod.getName(), BungeeContext.class);
                return (List<String>) tabMethod.invoke(tabHandler.getConstructor().newInstance(),context);
            }
            return new ArrayList<>();
        }


        @SneakyThrows
        private void handle(BungeeContext context) {
            Method method = methodCache.get(commandAnn.aliases()[0]);
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 1)
                for (Parameter parameter : parameters) {
                    if (parameter.getType().isAssignableFrom(BungeeContext.class))
                        method.invoke(cacheClass.get(commandAnn.aliases()[0]), context);

                }
        }


        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            BungeeContext context = new BungeeContext(sender, args);
            context.setMax(commandAnn.max());
            context.setMin(commandAnn.min());

            return this.commandAnn.tab() ? tab(context) : () -> null;
        }
    }

}
