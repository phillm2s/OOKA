

public class Main {

    public static void main(String[] args){

        Controller.postConstruct("testID");
        Controller.start();
        try {
            Thread.sleep(6000);
            Controller.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
