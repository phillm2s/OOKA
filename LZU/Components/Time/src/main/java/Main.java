

public class Main {

    public static void main(String[] args){

        Controller.instantiate("testID");
        Controller.start();
        try {
            Thread.sleep(6000);
            Controller.stop();
            //Controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
