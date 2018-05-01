package me.rayzr522.flash.test.calculator;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.gui.display.component.Button;
import me.rayzr522.flash.gui.display.component.Label;
import me.rayzr522.flash.gui.display.panes.AnchorPane;
import me.rayzr522.flash.gui.display.panes.FlowPane;
import me.rayzr522.flash.gui.flow.FlowInventoryGui;
import me.rayzr522.flash.gui.properties.ObservableProperty;
import me.rayzr522.flash.gui.properties.StringExpression;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.IntBinaryOperator;

public class Calculator {

    private ObservableProperty<Integer> result = new ObservableProperty<>(0);
    private Gui gui;
    private Label output;

    private ObservableProperty<String> left = new ObservableProperty<>("");
    private ObservableProperty<String> right = new ObservableProperty<>("");
    private ObservableProperty<Operator> operator = new ObservableProperty<>(null);

    public Calculator(Player player) {
        Pane numpad = buildNumPad();
        Pane operators = buildOperators();
        output = new Label(9, 1, Material.PAPER);

        gui = FlowInventoryGui.forPlayer(player)
                .title(ChatColor.RED + "" + ChatColor.BOLD + "Calculator")
                .setRootPane(new AnchorPane(9, 6))
                .editRootPane(flowPane -> flowPane.addChild(numpad, 2, 0))
                .editRootPane(flowPane -> flowPane.addChild(operators, 6, 0))
                .editRootPane(flowPane -> flowPane.addChild(output, 0, 4))
                .build();

        StringExpression concat = new StringExpression(ChatColor.GREEN.toString())
                .concat(left, "%s%-10s")
                .concat(" " + ChatColor.RED)
                .concatWithDefault(operator, "|-|")
                .concat(" " + ChatColor.GREEN)
                .concat(right, "%s%10s")
                .concat("   = " + ChatColor.AQUA + ChatColor.BOLD)
                .concat(result);
        output.setLabel(concat);
    }

    private Pane buildNumPad() {
        AnchorPane wrapper = new AnchorPane(3, 4);

        FlowPane numpad = new FlowPane(3, 3);
        for (int i = 1; i <= 9; i++) {
            numpad.addChild(buildNumberButton(i));
        }

        wrapper.addChild(numpad, 0, 0);
        wrapper.addChild(buildNumberButton(0), 1, 3);

        return wrapper;
    }

    private Button buildNumberButton(int number) {
        return new Button(1, 1, Material.GLASS, Integer.toString(number))
                .setOnClick(clickEvent -> onReceiveNumber(number));
    }

    private FlowPane buildOperators() {
        FlowPane operators = new FlowPane(2, 4);

        for (Operator op : Operator.values()) {
            operators.addChild(
                    new Button(1, 1, Material.GLASS_BOTTLE, op.name())
                            .setOnClick(clickEvent -> onReceiveOperator(op))
            );
        }

        operators.addChild(
                new Button(2, 1, Material.BOOK_AND_QUILL, ChatColor.RED + "Compute")
                        .setOnClick(clickEvent -> onCalculate())
        );
        operators.addChild(
                new Button(2, 1, Material.BARRIER, ChatColor.RED + "" + ChatColor.BOLD + "Reset")
                        .setOnClick(clickEvent -> reset())
        );

        return operators;
    }

    private void onReceiveNumber(int number) {
        if (operator.getValue() == null) {
            left.setValue(left.getValue() + number);
        } else {
            right.setValue(right.getValue() + number);
        }
    }

    private void onReceiveOperator(Operator operator) {
        this.operator.setValue(operator);
    }

    private void onCalculate() {
        result.setValue(
                operator.getValue().apply(
                        Integer.parseInt(left.getValue()),
                        Integer.parseInt(right.getValue())
                )
        );
    }

    private void reset() {
        left.setValue("");
        right.setValue("");
        operator.setValue(null);
        result.setValue(0);
    }

    public void show() {
        gui.show();
    }

    private enum Operator {
        ADD(Math::addExact),
        SUBTRACT(Math::subtractExact),
        DIVIDE(Math::floorDiv),
        MULTIPLY((left, right) -> left / right);

        private IntBinaryOperator operator;

        Operator(IntBinaryOperator operator) {
            this.operator = operator;
        }

        public int apply(int left, int right) {
            return operator.applyAsInt(left, right);
        }
    }
}
