public class Calculator {

    private boolean enabled=false;

    public double sum(double a, double b){
        return a+b;
    }

    public double sub(double a, double b){
        return a-b;
    }

    public double mul(double a, double b){
        return a*b;
    }

    public double div(double a, double b){
        return a/b;
    }

    public boolean isRunning(){
        return enabled;
    }

    public void enable(){
        enabled=true;
    }

    public void disable(){
        enabled=false;
    }
}
