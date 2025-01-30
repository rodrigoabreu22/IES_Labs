package org.ies.deti.ua;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.ies.deti.ua.ipma_client.IpmaCityForecast; //may need to adapt package name
import org.ies.deti.ua.ipma_client.IpmaService;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    public static String ipma(int cityId) {

        int city_id;

        // get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create a typed interface to use the remote API (a client)
        IpmaService service = retrofit.create(IpmaService.class);
        // prepare the call to remote endpoint
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityId);

        String output = String.format("Forecast for city %d\n", cityId );

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                var weather_days = forecast.getData().listIterator();
                while (weather_days.hasNext()) {
                    var firstDay = weather_days.next();

                    output += String.format("%s | %s | Min Temperature: %4.1f | Max Temperature: %4.1f | Precipitation prob: %4.1f | Wind Direction: %s\n",
                            forecast.getCountry(),
                            firstDay.getForecastDate(),
                            Double.parseDouble(firstDay.getTMin()),
                            Double.parseDouble(firstDay.getTMax()),
                            Double.parseDouble(firstDay.getPrecipitaProb()),
                            firstDay.getPredWindDir()
                    );

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return output;
    }

}