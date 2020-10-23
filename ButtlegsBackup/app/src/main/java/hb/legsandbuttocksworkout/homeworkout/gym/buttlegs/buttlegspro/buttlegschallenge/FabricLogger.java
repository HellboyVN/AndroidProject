package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge;

public class FabricLogger {
    private static FabricLogger _instance;

    private FabricLogger() {
    }

    public static FabricLogger getInstance() {
        if (_instance == null) {
            _instance = new FabricLogger();
        }
        return _instance;
    }

    public void logPurchase(boolean isSuccess) {
    }
}
