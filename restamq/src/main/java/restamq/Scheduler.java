package restamq;

public interface Scheduler {
    void initialize(Runnable run);

    void stop();
}
