package me.rayzr522.flash.command;

import me.rayzr522.flash.command.exception.NoSuchPlayerException;
import me.rayzr522.flash.utils.Players;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class ArgTypes {
    public static final Function<String, Integer> INT = Integer::parseInt;
    public static final Function<String, Double> DOUBLE = Double::parseDouble;
    public static final Function<String, Float> FLOAT = Float::parseFloat;
    public static final Function<String, Long> LONG = Long::parseLong;

    public static final Function<String, Boolean> BOOLEAN = arg -> {
        switch (arg.toLowerCase()) {
            case "y":
            case "yes":
            case "true":
                return true;
            case "n":
            case "no":
            case "false":
                return false;
            default:
                throw new IllegalArgumentException();
        }
    };

    public static final Function<String, Player> PLAYER = arg -> Players.get(arg).orElseThrow(() -> new NoSuchPlayerException(arg));
    public static final Function<String, Material> MATERIAL = enumConstant(Material.class);
    public static final Function<String, EntityType> ENTITY_TYPE = enumConstant(EntityType.class);

    public static <T extends Enum<T>> Function<String, T> enumConstant(Class<T> enumClass) {
        return arg -> Enum.valueOf(enumClass, arg.toUpperCase().replaceAll("[^A-Z0-9_]", "_"));
    }
}
