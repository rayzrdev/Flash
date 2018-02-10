package me.rayzr522.flash.command;

import me.rayzr522.flash.command.exception.GenericCommandException;
import me.rayzr522.flash.command.exception.NoSuchPlayerException;
import me.rayzr522.flash.utils.ArrayUtils;
import me.rayzr522.flash.utils.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommandContext {
    private CommandHandler command;
    private CommandSender sender;
    private List<String> args;
    private int currentArg = 0;

    public CommandContext(CommandHandler command, CommandSender sender, String[] args) {
        this.command = command;
        this.sender = sender;
        this.args = Arrays.asList(args);
    }

    /**
     * @return The command that was executed.
     */
    public CommandHandler getCommand() {
        return command;
    }

    /**
     * @return The sender of the command.
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * <i>Warning: this blindly casts <code>sender</code> to {@link Player}, so be careful using this!</i>
     *
     * @return The player who sent the command.
     */
    public Player getPlayer() {
        return (Player) sender;
    }

    /**
     * @return The first argument to the command, or <code>null</code>.
     */
    public String first() {
        return currentArg >= args.size() ? null : args.get(currentArg);
    }

    /**
     * @return The arguments that were passed to the command.
     */
    public List<String> getArgs() {
        return args.subList(currentArg, args.size());
    }

    /**
     * Checks if there are a certain number of arguments available.
     *
     * @param amount The amount of arguments to check.
     * @return Whether or not that many arguments are available.
     */
    public boolean hasArgs(int amount) {
        return getArgs().size() >= amount;
    }


    /**
     * Checks if there is at least 1 argument available. Calls {@link #hasArgs(int)} with an amount of 1.
     *
     * @return Whether or not there is at least 1 argument available.
     */
    public boolean hasArgs() {
        return hasArgs(1);
    }

    /**
     * Concatenates the remaining arguments with the given joiner.
     *
     * @param joiner The joiner to concatenate the arguments with.
     * @return The concatenated string.
     */
    public String remainder(String joiner) {
        return ArrayUtils.join(getArgs(), joiner);
    }


    /**
     * Concatenates the remaining arguments with a space. Calls {@link #remainder(String)} with a joiner of <code>" "</code>.
     *
     * @return The concatenated string.
     */
    public String remainder() {
        return remainder(" ");
    }

    public <T> T shift(Function<String, T> transformer) {
        Objects.requireNonNull(transformer, "transformer cannot be null!");

        String next = first();
        if (next != null) {
            currentArg++;
        }

        return transformer.apply(next);
    }

    public String shift() {
        return shift(arg -> arg);
    }

    public Player shiftPlayer() {
        String name = shift();
        return shift(arg -> Players.get(arg).orElseThrow(() -> new NoSuchPlayerException(arg)));
    }

    public void tell(String key, Object... formatterArgs) {
        tell(sender, key, formatterArgs);
    }

    public void tell(CommandSender receiver, String key, Object... formatterArgs) {
        receiver.sendMessage(command.getPlugin().getLanguage().tr(key, formatterArgs));
    }

    public Supplier<GenericCommandException> fail(String key, Object... formatterArgs) {
        return () -> new GenericCommandException(command.getPlugin().getLanguage().tr(key, formatterArgs));
    }

    public void assertPermission(String permission) {
        if (!command.getPlugin().checkPermission(sender, permission, true)) {
            throw new GenericCommandException(null);
        }
    }
}
