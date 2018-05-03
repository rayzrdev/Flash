package me.rayzr522.flash.prompt;

import me.rayzr522.flash.Flash;
import me.rayzr522.flash.utils.Promise;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.function.Predicate;

public class ChatPrompts {
    private static <T> Promise<T> get(Player player, String prompt, Predicate<String> validator, Function<String, String> errorMessage, Function<String, T> mapper) {
        Promise<T> promise = new Promise<>();

        new ConversationFactory(Flash.getInstance())
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext conversationContext) {
                        return prompt;
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext conversationContext, String query) {
                        if (!validator.test(query)) {
                            player.sendMessage(errorMessage.apply(query));
                            return this;
                        }

                        promise.resolve(mapper.apply(query));
                        return null;
                    }
                })
                .withLocalEcho(true)
                .withModality(false)
                .buildConversation(player)
                .begin();

        return promise;
    }

    public static Promise<String> getString(Player player, String prompt) {
        return get(
                player,
                prompt,
                query -> true,
                query -> null,
                query -> query
        );
    }

    public static Promise<Double> getDouble(Player player, String prompt) {
        return get(
                player,
                prompt,
                query -> {
                    try {
                        double v = Double.parseDouble(query);
                        return v == v; // essentially return true, but this way IntelliJ doesn't kill me
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                query -> Flash.getInstance().getLanguage().tr("command.fail.not-decimal", query),
                Double::parseDouble
        );
    }
}
