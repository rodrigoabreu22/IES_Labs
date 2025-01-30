package org.ies.deti.ua;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CityForecast {
    private static Logger logger = LogManager.getLogger(CityForecast.class);
    private static int[] ids = {
            1010500, 1020500, 1030300, 1030800, 1040200,
            1050200, 1060300, 1070500, 1080500, 1080800,
            1081100, 1081505, 1090700, 1090821, 1100900,
            1110600, 1121400, 1131200, 1141600, 1151200,
            1151300, 1160900, 1171400, 1182300, 2310300,
            2320100, 3410100, 3420300, 3430100, 3440100,
            3450200, 3460200, 3470100, 3480200, 3490100
    };

    Toolkit toolkit;//j  av  a2  s .  c o m

    Timer timer;

    public CityForecast() {
        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        timer.scheduleAtFixedRate(new PeriodicalLogger(), 0, //initial delay
                20 * 1000); //subsequent rate
    }

    class PeriodicalLogger extends TimerTask {
        public void run() {
            int randId = (int) (Math.random() * ids.length);
            logger.info(WeatherStarter.ipma(ids[randId]));
        }
    }

    public static void main(String args[]) {
        new CityForecast();
    }
}
