package org.ies.deti.ua.weather;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.ies.deti.ua.weather.ipma_client.IpmaCityForecast; //may need to adapt package name
import org.ies.deti.ua.weather.ipma_client.IpmaService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {
    private static final Logger logger = LogManager.getLogger(WeatherStarter.class);

    //todo: should generalize for a city passed as argument
    private static int CITY_ID_AVEIRO = 1010500;

    public static void  main(String[] args ) {

        int city_id;

        if (args.length > 0 && verifyArg(args[0])!=0) {
            int args_city_id = verifyArg(args[0]);
            city_id = args_city_id;
        }
        else {
            city_id = CITY_ID_AVEIRO;
        }

        // get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create a typed interface to use the remote API (a client)
        IpmaService service = retrofit.create(IpmaService.class);
        // prepare the call to remote endpoint
        Call<IpmaCityForecast> callSync = service.getForecastForACity(city_id);


        logger.info(String.format("Forecast for city %d", city_id ));
        System.out.println( "Forecast for city " + city_id );
        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                var weather_days = forecast.getData().listIterator();
                while (weather_days.hasNext()) {
                    var firstDay = weather_days.next();

                    logger.info(String.format("%s | %s | Min Temperature: %4.1f | Max Temperature: %4.1f | Precipitation prob: %4.1f | Wind Direction: %s",
                            forecast.getCountry(),
                            firstDay.getForecastDate(),
                            Double.parseDouble(firstDay.getTMin()),
                            Double.parseDouble(firstDay.getTMax()),
                            Double.parseDouble(firstDay.getPrecipitaProb()),
                            firstDay.getPredWindDir()
                    ));

                    System.out.printf("%s | %s | Min Temperature: %4.1f | Max Temperature: %4.1f | Precipitation prob: %4.1f | Wind Direction: %s\n",
                            forecast.getCountry(),
                            firstDay.getForecastDate(),
                            Double.parseDouble(firstDay.getTMin()),
                            Double.parseDouble(firstDay.getTMax()),
                            Double.parseDouble(firstDay.getPrecipitaProb()),
                            firstDay.getPredWindDir()
                    );

                }
            } else {
                System.out.println( "No results for this request!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static int verifyArg(String arg){
        int city_id = 0;
        try{
            city_id = Integer.parseInt(arg);
        }catch (Exception e){
            System.out.println("Please enter a valid number!");
        }
        return city_id;
    }
}