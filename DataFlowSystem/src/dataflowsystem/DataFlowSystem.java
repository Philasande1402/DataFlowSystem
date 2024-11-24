package dataflowsystem;

class DataFlowSystem {

    // Shared resources
    private static boolean isDataCollected = false;
    private static boolean isDataProcessed = false;

    public static void main(String[] args) {
        // Creating and starting threads
        Thread dataCollectionThread = new DataCollectionThread();
        Thread dataProcessingThread = new DataProcessingThread();
        Thread dataStorageThread = new DataStorageThread();

        dataCollectionThread.setPriority(Thread.MAX_PRIORITY);
        dataProcessingThread.setPriority(Thread.NORM_PRIORITY);
        dataStorageThread.setPriority(Thread.MIN_PRIORITY);

        dataCollectionThread.start();
        dataProcessingThread.start();
        dataStorageThread.start();
    }

    static class DataCollectionThread extends Thread {
        @Override
        public void run() {
            synchronized (DataFlowSystem.class) {
                System.out.println("Data collection started.");
                try {
                    Thread.sleep(1000); // Simulate time taken for data collection
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isDataCollected = true;
                System.out.println("Data collection completed.");
                DataFlowSystem.class.notifyAll(); // Notify waiting threads
            }
        }
    }

    // Implement the DataProcessingThread class here
    static class DataProcessingThread extends Thread {
        // 
        @Override
        public void run(){
            synchronized (DataFlowSystem.class) {
                try{
                   while(!isDataCollected)
                   {
                    System.out.println("Waiting data collection....");
                    DataFlowSystem.class.wait();
                   }
                    System.out.println("Data processing started");
                    Thread.sleep(1000);
                    isDataProcessed=true;
                    System.out.println("Data processing completed.");
                    DataFlowSystem.class.notifyAll();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                
            }
        }
    }

    static class DataStorageThread extends Thread {
        @Override
        public void run() {
            synchronized (DataFlowSystem.class) {
                try {
                    while (!isDataProcessed) {
                        System.out.println("Waiting for data processing...");
                        DataFlowSystem.class.wait(); // Wait for data to be processed
                    }
                    System.out.println("Data storage started.");
                    Thread.sleep(1000); // Simulate time taken for data storage
                    System.out.println("Data storage completed.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
