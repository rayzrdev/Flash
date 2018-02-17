package me.rayzr522.flash.gui;

import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.gui.display.component.Button;
import me.rayzr522.flash.gui.display.component.Label;
import me.rayzr522.flash.gui.display.panes.AnchorPane;
import me.rayzr522.flash.gui.display.panes.FlowPane;
import me.rayzr522.flash.gui.flow.FlowInventoryGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.IntBinaryOperator;

public class Calculator {

    private int result;
    private Gui gui;
    private Label output;

    private String left = "";
    private String right = "";
    private Operator operator;

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
        if (operator == null) {
            left += number;
        } else {
            right += number;
        }
        updateOutput();
    }

    private void onReceiveOperator(Operator operator) {
        this.operator = operator;
        updateOutput();
    }

    private void onCalculate() {
        result = operator.apply(Integer.parseInt(left), Integer.parseInt(right));
        updateOutput();
    }

    private void updateOutput() {
        output.setLabel(String.format(
                "%s%-10s %s%s %s%10s    = %s%d",
                ChatColor.GREEN.toString(),
                left,
                ChatColor.RED.toString(),
                operator == null ? "|-|" : operator.name(),
                ChatColor.GREEN.toString(),
                right,
                ChatColor.AQUA.toString() + ChatColor.BOLD,
                result
        ));
    }

    private void reset() {
        left = "";
        right = "";
        operator = null;
        result = 0;
        updateOutput();
    }

    public void show() {
        gui.show();
    }

    public int getResult() {
        return result;
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
