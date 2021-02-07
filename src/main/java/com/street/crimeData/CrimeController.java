package com.street.crimeData;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/")
public class CrimeController {
    private static Logger logger = LoggerFactory.getLogger(CrimeController.class);

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(path = "/crime/categories", produces = "application/json")
    public String getCategories() {

        //get the crime-categories from police data
        final String categoryUri = "https://data.police.uk/api/crime-categories";

        //return the categories
        String categories = restTemplate.getForObject(categoryUri, String.class);
        logger.info(categories);
        return categories;
    }

    /*get the crime details by latitude,longitude and postcode*/
    @RequestMapping(path = "/crimes")
    public String getLatLongByPostCode(@RequestParam String postcode, @RequestParam String date) throws JsonParseException {

        //get the location details by postcode where postcode is our input.
        final String uri = "https://api.postcodes.io/postcodes/" + postcode;

        String postCodeResult = restTemplate.getForObject(uri, String.class);

        //parse the result into json object
        JsonObject jsonObject = new JsonParser().parse(postCodeResult).getAsJsonObject();

        //extract the "result" to get the latitude and logitude
        JsonObject locResult = jsonObject.getAsJsonObject("result");

        //extract the latitude
        String longitude = locResult.get("longitude").getAsString();

        //extract the longitude
        String latitude = locResult.get("latitude").getAsString();

        //url for crimes at a given latitude,longitude and date
        final String uriloc = "https://data.police.uk/api/crimes-at-location?lng=longitude&lat=latitude&date=date";


        // invoke the url
        String crimeByPostcode = restTemplate.getForObject(uriloc, String.class);
        logger.info(crimeByPostcode);
        //return the crime Info
        return crimeByPostcode;
    }

}
