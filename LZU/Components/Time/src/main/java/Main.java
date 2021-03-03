

public class Main {

    public static void main(String[] args){
        new Thread(() -> {
            Controller.start();
            System.out.println("A");
        }).start();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Controller.stop();
    }
}
