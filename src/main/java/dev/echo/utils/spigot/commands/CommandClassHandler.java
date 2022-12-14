package dev.echo.utils.spigot.commands;

import dev.echo.utils.general.Color;
import dev.echo.utils.spigot.commands.annotations.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static dev.echo.utils.spigot.reflection.PacketUtil.getCraftBukkitClass;


public class CommandClassHandler {


    private static final HashMap<String, Object[]> commandMap = new HashMap<>();

    @Getter
    @Setter
    private static Class<?> tabHandler;

    @SneakyThrows
    @SuppressWarnings("deprecation")
    public static void getCommands(String fall, String pack) {

        for (Class<?> a : new Reflections(pack, new SubTypesScanner(false)).getSubTypesOf(Object.class)) {
            System.out.println(a.getSimpleName());
            if (a.getDeclaredAnnotation(Command.class) == null){
                continue;
            }
            Command command = a.getDeclaredAnnotation(Command.class);
            if(a.getDeclaredMethod("command") == null){
                continue;
            }
            commandMap.put(command.aliases()[0], new Object[]{command, a.getDeclaredMethod("command")});
        }
        registerCommands(fall);
    }

    //This doesn't need to be modified.
    private static void registerCommands(String fall) {
        commandMap.forEach((s, adventureCommand) ->
        {
            try {
                ((CommandMap) getCraftBukkitClass("CraftServer")
                        .cast(Bukkit.getServer()).getClass().getMethod("getCommandMap").invoke(Bukkit.getServer())).register(fall,
                        new CommandClass((Command) adventureCommand[0], (Method) adventureCommand[1]));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });


        commandMap.clear();
    }


    private static final class CommandClass extends org.bukkit.command.Command {


        private final Command commandAnn;
        private final Method method;

        private final HashMap<String, Method> methodCache = new HashMap<>();
        private final HashMap<String, Object> cacheClass = new HashMap<>();

        @SneakyThrows
        private CommandClass(Command commandAnn, Method method) {


            super(commandAnn.aliases()[0], commandAnn.desc(),
                    "/" + commandAnn.aliases()[0], Arrays.asList(commandAnn.aliases()));

            this.commandAnn = commandAnn;

            methodCache.put(commandAnn.aliases()[0], method);
            cacheClass.put(commandAnn.aliases()[0], method.getDeclaringClass().getConstructor().newInstance());
            this.method = methodCache.get(commandAnn.aliases()[0]);
        }


        @Override
        public boolean execute(CommandSender sender, String s, String[] strings) {


            if (!commandAnn.console() && !(sender instanceof Player)) {
                sender.sendMessage(Color.c("&cYou must be a player to perform this command!"));
                return true;
            }
            CommandContext context = new CommandContext(sender, strings);
            context.setMax(commandAnn.max());
            context.setMin(commandAnn.min());
            command(context);
            return false;
        }

        @SneakyThrows
        private void command(CommandContext context) {
            method.setAccessible(true);
            handle(context);
            method.setAccessible(false);
        }


        @SneakyThrows
        private List<String> tab(CommandContext context) {
            Class<?> baseClass = method.getDeclaringClass();
            for (Method tabMethods : tabHandler.getMethods()) {
                tabMethods.setAccessible(true);
                String tabName = tabMethods.getName();
                for (Method methods : baseClass.getDeclaredMethods()) {
                    methods.setAccessible(true);
                    String name = methods.getName();
                    if (tabName.equals(name))
                        return (List<String>) tabMethods.invoke(tabHandler.getConstructor().newInstance(), context);
                    methods.setAccessible(false);
                }
                tabMethods.setAccessible(false);
            }
            return new ArrayList<>();
        }


        @Override
        public @NotNull List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            if (commandAnn.tab()) return tab(new CommandContext(sender, args));
            return super.tabComplete(sender, alias, args);
        }


        @SneakyThrows
        private void handle(CommandContext context) {
            Method method = methodCache.get(commandAnn.aliases()[0]);
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 1)
                for (Parameter parameter : parameters) {
                    if (parameter.getType().isAssignableFrom(CommandContext.class))
                        method.invoke(cacheClass.get(commandAnn.aliases()[0]), context);

                }
        }
    }

}
