package com.example.prohor.calc3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.mariuszgromada.math.mxparser.*;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;


public class MainActivity extends AppCompatActivity {

    public static final String TEXT_VIEW_1 = "textView1";
    public static final String TEXT_VIEW_2 = "textView2";
    public static final String TEXT_VIEW_3 = "textView3";
    public static final String NEGATIVE_STATUS = "negativeStatus";
    public static final String RESULT_USED = "resultUsed";
    public static final String SIGN_SET = "signSet";
    public static final String FUNC_USED = "funcUsed";
    public static final String ERROR = "error";

    TextView textView1;
    TextView textView2;
    TextView textView3;
    ScrollView scrollView;

    Boolean negativeStatus = false;
    Boolean resultUsed = true; // т.к. изначально в tv1 0
    Boolean signSet = false;
    Boolean funcUsed = false;
    Boolean error = false;

    int buttonPressedCounter = 0;
    int buttonZeroCounter = 0;
    int bracketOpenCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        final List<Integer> buttons = Arrays.asList(R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9);
        for (int i = 0; i < buttons.size(); i++) {
            final Button button = (Button) findViewById(buttons.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(resultUsed || error){
                        textView1.setText("");
                        resultUsed = false;
                        error = false;
                    }

                    if (signSet){
                        textView1.setText("");
                    }

                    if (textView1.getText().toString().equals("0")) {
                        textView1.setText(button.getText());
                        buttonPressedCounter = 1;
                        signSet = false;
                    }
                    else {
                        if (buttonPressedCounter < 12) {
                            String res = String.valueOf(textView1.getText());
                            textView1.setText(res + button.getText());
                            buttonPressedCounter++;
                            signSet = false;
                        }
                        else {
                            showToast(getString(R.string.limit_of_chars));
                        }
                    }

                }
            });
        }

        final List<Integer> signs = Arrays.asList(R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button15);
        for (int i = 0; i < signs.size(); i++){
            final Button button = (Button) findViewById(signs.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (error){
                        textView1.setText("");
                        error = false;
                        resultUsed = false;
                    }

                    if (!signSet && (textView1.getText().length() != 0 || resultUsed)) {
                        clearValue();
                        String res = String.valueOf(textView1.getText());

                        if (negativeStatus) {
                            res = "(" + res + ")";
                        }
                        textView3.setText(textView3.getText() + res + button.getText());
                        textView1.setText("");
                        textView1.setText(button.getText());                                        //выводить знак который будет активный и который потом стирается при вводе нового значения
                        negativeStatus = false;
                        resultUsed = false;
                        buttonPressedCounter = 0;
                        buttonZeroCounter = 0;
                        signSet = true;
                    }
                    if(signSet){
                        textView3.setText(removeLastChar(String.valueOf(textView3.getText())));     // если знак установлен, стирает его из 3 и устанавливает новый.
                        textView3.setText(textView3.getText().toString() + button.getText());
                        textView1.setText(button.getText());
                    }

                }
            });
        }

        Button buttonSqrt = (Button) findViewById(R.id.button16);
        buttonSqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView1.getText().length() != 0) {
                    String res = String.valueOf(textView1.getText());
                    if (!textView1.getText().toString().substring(0, 1).equals("-") && !signSet) {
                        textView3.setText(textView3.getText() + "sqrt(" + res + ")");
                        textView1.setText("");
                        negativeStatus = false;
                        resultUsed = true;      // true для того чтобы можно было вводить знак после вычисления корня
                        buttonPressedCounter = 0;
                        buttonZeroCounter = 0;
                    } else {
                        showToast(getString(R.string.Notification_sqrt));
                    }
                }
            }
        });

        final Button buttonZeroNumber = (Button) findViewById(R.id.button0);
        buttonZeroNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultUsed){
                    textView1.setText("");
                    resultUsed = false;
                }

                if (signSet){
                    textView1.setText("");
                }

                if (buttonPressedCounter == 0 && buttonZeroCounter > 0 ){
                    System.out.println("");
                }
                else {
                    textView1.setText(textView1.getText().toString() + buttonZeroNumber.getText());
                    buttonZeroCounter++;
                    signSet = false;
                    //buttonPressedCounter++;
                }

            }
        });

        Button buttonClear = (Button) findViewById(R.id.button17);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textView1.length() == 0 && textView3.length() != 0){
                    textView1.setText(removeLastChar(textView3.getText().toString()));
                    textView3.setText("");
                }
                else {
                    textView1.setText(removeLastChar(String.valueOf(textView1.getText())));
                    dropAllFlags();
                    buttonPressedCounter = 0;
                    buttonZeroCounter = 0;
                    bracketOpenCounter = 0;
                }
            }
        });

        buttonClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textView1.setText("");
                textView2.setText("");
                textView3.setText("");
                dropAllFlags();
                buttonPressedCounter = 0;
                buttonZeroCounter = 0;
                bracketOpenCounter = 0;
                return true;

            }
        });

        Button buttonDot = (Button) findViewById(R.id.button18);
        buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textView1.getText().toString().contains(".") && textView1.getText().length() != 0) {
                        textView1.setText(textView1.getText() + ".");
                        buttonPressedCounter++;
                }
            }
        });

        Button buttonEqual = (Button) findViewById(R.id.button14);
        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (error){
                    textView1.setText("");
                    error = false;
                    resultUsed = false;
                }

                clearValue();

                if (negativeStatus && !funcUsed){
                    textView1.setText("(" + textView1.getText() + ")");
                }

                if (textView3.getText().length() != 0 || textView1.getText().length() != 0) {

                    if (bracketOpenCounter > 0){
                        while (bracketOpenCounter > 0) {
                            textView3.setText(textView3.getText() + ")");
                            bracketOpenCounter--;
                        }
                    }

                    textView3.setText(textView3.getText().toString().replace("()","(0)"));
                    textView3.setText(textView3.getText().toString().replace(")(",")*("));

                    String example = textView3.getText().toString() + textView1.getText().toString();

                    while (lastCharIsSign(example)){
                        example = removeLastChar(example);
                    }

                    Expression one = new Expression(example);

                    try {
                        String answer = String.valueOf(new BigDecimal("" + one.calculate()).setScale(9, BigDecimal.ROUND_HALF_UP));
                        textView1.setText(answer);
                        clearValue();
                    } catch (Exception e) {
                        textView1.setText(R.string.error);
                        error = true;
                    }

                    String temp = String.valueOf(textView2.getText());
                    textView2.setText(temp + "\n" + example + " = " + textView1.getText());

                    textView3.setText("");
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 100L);
                    resultUsed = true;
                    negativeStatus = false;
                    signSet = false;
                    buttonPressedCounter = 0;
                    buttonZeroCounter = 0;
                }
                else {
                    showToast(getString(R.string.notification_nothing_to_show));
                }
            }
        });

        Button buttonNegative = (Button) findViewById(R.id.button19);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView1.getText().length() != 0 && !textView1.getText().toString().equals("0") && !signSet) {
                    if (textView1.getText().toString().substring(0, 1).equals("-")) {
                        String box = String.valueOf(textView1.getText());
                        textView1.setText(box.substring(1));
                        negativeStatus = false;
                    } else {
                        String box = String.valueOf(textView1.getText());                               // если firstArg = false ставить скобки вокруг аргумена
                        textView1.setText("-" + box);
                        negativeStatus = true;
                    }
                }
            }
        });

        if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){                   // если land ориентация, найти остальные кнопки по id

            final Button buttonBracketOpen = (Button) findViewById(R.id.button36);
            buttonBracketOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView1.getText().length() != 0) {
                        if (signSet) {
                            textView1.setText("");
                        }
                        textView3.setText(textView3.getText() + textView1.getText().toString());
                        textView1.setText("");

                        if (!signSet && !textView3.getText().toString().substring(textView3.getText().length() - 1, textView3.getText().length()).equals("(")) {
                            textView3.setText(textView3.getText() + "*");
                        }
                    }
                    if (bracketOpenCounter < 6) {
                        textView3.setText(textView3.getText() + buttonBracketOpen.getText().toString());
                        bracketOpenCounter++;
                    }

                }
            });

            final Button buttonBracketClose = (Button) findViewById(R.id.button35);
            buttonBracketClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bracketOpenCounter != 0 && !signSet) {
                        textView3.setText(textView3.getText() + textView1.getText().toString() + buttonBracketClose.getText().toString());
                        textView1.setText("");
                        resultUsed = true;
                        bracketOpenCounter--;
                    }
                }
            });

            final List<Integer> funcButtons = Arrays.asList(R.id.button21, R.id.button23, R.id.button24, R.id.button25);
            for (int i = 0; i < funcButtons.size(); i++) {
                final Button buttonFunc = (Button) findViewById(funcButtons.get(i));
                buttonFunc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textView1.length() != 0 && !signSet){
                            textView3.setText(textView3.getText() + buttonFunc.getText().toString() + "(" + textView1.getText() + ")");
                            textView1.setText("");
                            funcUsed = true;
                            resultUsed = true;
                        }
                    }
                });
            }

            final Button buttonLog = (Button) findViewById(R.id.button20);
            buttonLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView1.length() != 0 && !signSet){
                        textView3.setText(textView3.getText() + buttonLog.getText().toString() + "2(" + textView1.getText() + ")");
                        textView1.setText("");
                        funcUsed = true;
                        resultUsed = true;
                    }
                }
            });

            Button buttonPi = (Button) findViewById(R.id.button22);
            buttonPi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double Pi = 3.1415926535;
                    textView1.setText(String.valueOf(Pi));
                }
            });

            Button buttonFact = (Button) findViewById(R.id.button26);
            buttonFact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView1.length() != 0){
                        textView3.setText(textView3.getText().toString() + textView1.getText().toString() + "!");
                        textView1.setText("");
                        funcUsed = true;
                    }
                }
            });

        }
    }

    //Удаление последнего символа в нижнем поле
    public void deleteSymbol(TextView tV) {
        StringBuilder stringBuffer = new StringBuilder(tV.getText().toString());
        if (stringBuffer.length() != 0) {
            stringBuffer.delete(tV.getText().length() - 1, tV.getText().length());
            tV.setText(stringBuffer.toString());

        }
    }

    // Очистка нижнего поля от лишних нулей и точек
    public void clearValue (){
        if (textView1.getText().toString().contains(".")) {
            while (true) {
                int i = textView1.getText().length();
                String s = textView1.getText().toString().substring(i-1, i);                        // проверяет последний символ вместо i длинну textview
                if (s.equals("0")) {
                    deleteSymbol(textView1);
                } else if (s.equals(".")) {
                    deleteSymbol(textView1);
                    break;
                } else break;
            }
        }
    }

    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    public void showToast(String note) {
        Toast toast = Toast.makeText(getApplicationContext(), note, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,150);
        toast.show();
    }

    public boolean checkLastChar (String a){
        return textView1.getText().toString().substring(textView1.getText().length()-1, textView1.getText().length()).equals(a);
    }

    public void dropAllFlags (){
        resultUsed = false;
        negativeStatus = false;
        signSet = false;
        funcUsed = false;
        error = false;
    }

    public boolean lastCharIsSign (String text){
        return text.substring(text.length() - 1, text.length()).equals("+") || text.substring(text.length() - 1, text.length()).equals("-") ||
                text.substring(text.length() - 1, text.length()).equals("*") || text.substring(text.length() - 1, text.length()).equals("/") ||
                text.substring(text.length() - 1, text.length()).equals("^");
    }

    protected void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_VIEW_1, textView1.getText().toString());
        outState.putString(TEXT_VIEW_2, textView2.getText().toString());
        outState.putString(TEXT_VIEW_3, textView3.getText().toString());
        outState.putBoolean(NEGATIVE_STATUS, negativeStatus);
        outState.putBoolean(RESULT_USED, resultUsed);
        outState.putBoolean(SIGN_SET, signSet);
        outState.putBoolean(FUNC_USED, funcUsed);
        outState.putBoolean(ERROR, error);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textView1.setText(savedInstanceState.getString(TEXT_VIEW_1));
        textView2.setText(savedInstanceState.getString(TEXT_VIEW_2));
        textView3.setText(savedInstanceState.getString(TEXT_VIEW_3));
        negativeStatus = savedInstanceState.getBoolean(NEGATIVE_STATUS);
        resultUsed = savedInstanceState.getBoolean(RESULT_USED);
        signSet = savedInstanceState.getBoolean(SIGN_SET);
        funcUsed = savedInstanceState.getBoolean(FUNC_USED);
        error = savedInstanceState.getBoolean(ERROR);
    }
}
