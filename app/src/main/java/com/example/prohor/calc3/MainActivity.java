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

    TextView textView1;
    TextView textView2;
    TextView textView3;
    ScrollView scrollView;

    String primer1 = "";

    Boolean negativeStatus = false;
    Boolean resultUsed = true;
    Boolean dotIsSet = false;
    Boolean firstArg = true;
    Boolean signSet = false;

    int buttonPressedCounter = 0;
    int buttonZeroCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        final List<Integer> buttons = Arrays.asList(R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9 /*,  R.id.button15,*/ /* R.id.button16*/);
        for (int i = 0; i < buttons.size(); i++) {
            final Button button = (Button) findViewById(buttons.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(resultUsed){
                        textView1.setText("");                                                          // только для цифр! если вводишь знаки для вычисления, операции будут
                        resultUsed = false;                                                             // выполнятся над результатом предыдущего вычисления !!!!!
                    }                                                                                   // поделить клавиши на группы. группа цифр и другие
                    if (signSet){
                        textView1.setText("");
                    }

                    if (textView1.getText().toString().equals("0")) {
                        textView1.setText(button.getText());
                        buttonPressedCounter++;
                        signSet = false;
                    }
                    else {
                        String res = String.valueOf(textView1.getText());
                        textView1.setText(res + button.getText());
                        buttonPressedCounter++;
                        signSet = false;
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

                    if (!signSet && (textView1.getText().length() != 0 || resultUsed)) {
                        String res = String.valueOf(textView1.getText());

                        if (negativeStatus && !firstArg) {
                            res = "(" + res + ")";
                        }
                        textView3.setText(textView3.getText() + res + button.getText());
                        textView1.setText("");
                        textView1.setText(button.getText());                                      //выводить знак который будет активный и который потом стирается при вводе нового значения
                        negativeStatus = false;
                        //dotIsSet = false;
                        resultUsed = false;
                        buttonPressedCounter = 0;
                        buttonZeroCounter = 0;
                        firstArg = false;
                        signSet = true;
                    }
                    if(signSet){
                        textView3.setText(removeLastChar(String.valueOf(textView3.getText())));
                        textView3.setText(textView3.getText().toString() + button.getText());
                        textView1.setText(button.getText());
                    }

                }
            });
        }


        Button buttonSqrt = (Button) findViewById(R.id.button36);
        buttonSqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = String.valueOf( textView1.getText());
                if (!negativeStatus && !res.isEmpty() && !signSet){
                    textView3.setText(textView3.getText() + "sqrt(" + res + ")");
                    textView1.setText("");
                    negativeStatus = false;
                    //dotIsSet = false;
                    resultUsed = true;      // true для того чтобы можно было вводить знак после вычисления корня
                    buttonPressedCounter = 0;
                    buttonZeroCounter = 0;
                    firstArg = false;
                }
                else {
                    String notif = "нельзя извлечь корень";
                    showToast(notif);
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
                    System.out.println("больше нулей не надо");
                }
                else {
                    String res = String.valueOf( textView1.getText());
                    textView1.setText(res + buttonZeroNumber.getText());
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
                    resultUsed = false;
                    negativeStatus = false;
                    signSet = false;
                    buttonPressedCounter = 0;
                    buttonZeroCounter = 0;
                }
            }
        });

        buttonClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textView1.setText("");
                textView2.setText("");
                textView3.setText("");
                //dotIsSet = false;
                resultUsed = false;
                negativeStatus = false;
                signSet = false;
                buttonPressedCounter = 0;
                buttonZeroCounter = 0;
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


        /*if (!textView1.getText().toString().contains(".")) {
            if (textView1.getText().length() == 0) {
                textView1.setText("0.");
            } else {
                textView1.setText(textView1.getText() + ".");
            }
        }*/


        Button buttonEqual = (Button) findViewById(R.id.button14);
        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signSet){
                    textView3.setText(removeLastChar(String.valueOf(textView3.getText())));
                    textView1.setText("");
                    signSet = false;
                }
                String s = String.valueOf(textView1.getText());

                if (negativeStatus && !firstArg){
                    s = "(" + s + ")";
                }

                if (textView3.getText().length() != 0 || textView1.getText().length() != 0) {
                    String a = String.valueOf(textView3.getText());

                    String example = a + s;

                    Expression one = new Expression(example);

                    String answer = "";
                    try {
                        answer = String.valueOf(new BigDecimal("" + one.calculate()).setScale(10, BigDecimal.ROUND_HALF_UP));
                        textView1.setText(String.valueOf(answer));
                        clearValue();
                    } catch (Exception e) {
                        textView1.setText("ERROR");
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
                    //dotIsSet = false;
                    negativeStatus = false;
                    signSet = false;
                    buttonPressedCounter = 0;
                    buttonZeroCounter = 0;
                }
                else {
                    showToast("нечего вычислять");
                }
            }
        });

        Button buttonNegative = (Button) findViewById(R.id.button19);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView1.getText().length() != 0 && !textView1.getText().toString().equals("0") && !signSet) {
                    if (negativeStatus) {
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


        if(getResources().getConfiguration().orientation==ORIENTATION_LANDSCAPE){                   // если land ориентация, найти остальные кнопки по id
            Button cos = (Button) findViewById(R.id.button20);
            cos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView2.setText(textView2.getText() + "COS()" + "\n");
                }
            });
        }

    }

    //Удаление последнего символа в нижнем поле
    public void deleteSymbol(TextView tV) {
        StringBuffer stringBuffer = new StringBuffer(tV.getText().toString());
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

}
