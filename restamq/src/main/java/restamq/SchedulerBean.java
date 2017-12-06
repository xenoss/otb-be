package restamq;

import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.ArrayList;

@Singleton
public class SchedulerBean implements Scheduler {
    private static final Logger log = Logger.getLogger(SchedulerBean.class.toString());

    private final ArrayList<Runnable> runnable = new ArrayList<>();
    @Resource
    private TimerService timerService;

    private Timer timer;

    @Timeout
    public synchronized void scheduler(Timer timer) {
        log.info("Rest Timer: Info=" + timer.getInfo());

        runnable.forEach(Runnable::run);
    }

    @Override
    public void initialize(Runnable run) {
        runnable.add(run);

        if (timer == null) {
            ScheduleExpression sexpr = new ScheduleExpression();
            // set schedule to every 10 seconds for demonstration
            sexpr.hour("*").minute("*").second("0/20");
            // persistent must be false because the timer is started by the HASingleton service
            timer = timerService.createCalendarTimer(sexpr, new TimerConfig("rest time", false));
        }
    }

    @Override
    public void stop() {
        log.info("Stop rest timer");

        if (timer != null) timer.cancel();
    }
}
