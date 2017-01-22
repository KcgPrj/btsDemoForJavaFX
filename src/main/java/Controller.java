import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField textDisplay;
    private double leftNumber; //演算子の左側
    private double rightNumber; //演算子の右側
    private OptState state = null; //演算子

    private void onNumberPressed(int newNumber) {
        if (state == null) {
            //値が十分に大きければ更新しない
            if (leftNumber >= 10000) {
                return;
            }
            leftNumber *= 10;
            leftNumber += newNumber;
        } else {
            //値が十分に大きければ更新しない
            if (rightNumber >= 10000) {
                return;
            }
            rightNumber *= 10;
            rightNumber += newNumber;
        }
    }

    //画面更新
    private void updateDisplayText() {
        String str = String.valueOf(leftNumber);
        if (state != null) {
            str += " " + state.getOperatorText() + " " + rightNumber;

        }
        textDisplay.setText(str);
    }

    @FXML
    private void handleOnAnyButtonClicked(ActionEvent evt) {

        Button source = (Button) evt.getSource();
        if (source.getText().matches("^\\d$")) {
            onNumberPressed(Integer.valueOf(source.getText()));
            updateDisplayText();
        }

    }

    @FXML
    private void changeZero(ActionEvent evt_valuezero) {
        Button source = (Button) evt_valuezero.getSource();
        if (source.getText().equals("AC")) {
            leftNumber = 0;
            rightNumber = 0;
            state = null;
            updateDisplayText();
        }
    }

    //演算子をおした時
    public void pressOperator(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        switch (source.getText()) {
            case "+":
                calc();
                state = OptState.ADD;
                break;
            case "-":
                calc();
                state = OptState.SUB;
                break;
            case "×":
                calc();
                state = OptState.MULTI;
                break;
            case "÷":
                calc();
                state = OptState.DIV;
                break;
        }
    }

    private void calc() {
            leftNumber = state.operate(leftNumber, rightNumber);
            rightNumber = 0;
            state = null;
            updateDisplayText();
    }

    interface Opr {
        double operate(double leftNumber, double rightNumber);
    }

    enum OptState implements Opr {
        ADD("+") {
            @Override
            public double operate(double x, double y) {
                return x + y;
            }
        },
        SUB("-") {
            @Override
            public double operate(double x, double y) {
                return x - y;
            }
        },
        MULTI("*") {
            @Override
            public double operate(double x, double y) {
                return x * y;
            }
        },
        DIV("/") {
            @Override
            public double operate(double x, double y) {
                return x / y;
            }
        };

        private final String operatorText;

        OptState(String operatorText) {
            this.operatorText = operatorText;
        }

        public String getOperatorText() {
            return operatorText;
        }
    }
}
