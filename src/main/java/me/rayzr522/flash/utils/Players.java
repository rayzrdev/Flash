package me.rayzr522.flash.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Players {
    /**
     * Gets an online player with the given name.
     *
     * @param name The name of the player to get.
     * @return An {@link Optional} with the requested player.
     */
    @SuppressWarnings("deprecated")
    public static Optional<Player> get(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    /**
     * Attempts to match an online player with the given name.
     *
     * @param name The name to match.
     * @return An {@link Optional} with the closest match to the requested player.
     */
    @SuppressWarnings("deprecated")
    public static Optional<Player> fuzzyGet(String name) {
        List<Player> players = Bukkit.matchPlayer(name);
        if (players.size() < 1) {
            return Optional.empty();
        }
        return Optional.of(players.get(0));
    }

    /**
     * Gets an online player with the given UUID.
     *
     * @param id The UUID of the player to get.
     * @return An {@link Optional} with the requested player.
     */
    public static Optional<Player> get(UUID id) {
        return Optional.ofNullable(Bukkit.getPlayer(id));
    }
}
