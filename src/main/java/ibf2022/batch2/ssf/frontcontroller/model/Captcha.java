package ibf2022.batch2.ssf.frontcontroller.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Captcha {
    private int firstNum;
    private int secondNum;
    private String operator;
    private float answer;
    private boolean visible = false;

    private List<String> operatorList = Arrays.asList("+", "-" , "/" , "*");

    public int getFirstNum() {
        return firstNum;
    }
    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }
    public int getSecondNum() {
        return secondNum;
    }
    public void setSecondNum(int secondNum) {
        this.secondNum = secondNum;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public float getAnswer() {
        return answer;
    }
    public void setAnswer(float answer) {
        this.answer = answer;
    }
    public void generateAnswer(){
        if (this.operator.equals("-")){
            this.answer = (float)this.firstNum - (float)this.secondNum;
        }else if (this.operator.equals("+")){
            this.answer = (float)this.firstNum + (float)this.secondNum;
        }else if (this.operator.equals("*")){
            this.answer = (float)this.firstNum * (float)this.secondNum;
        }else{
            this.answer = (float)this.firstNum / (float)this.secondNum;
        }
    }

    public Captcha() {
        Random rand = new Random();
        this.firstNum = rand.nextInt(50)+1;
        this.secondNum = rand.nextInt(50)+1;
        this.operator = operatorList.get(rand.nextInt(4));
        //this.generateAnswer();
    }
}
