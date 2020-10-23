package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

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

}
