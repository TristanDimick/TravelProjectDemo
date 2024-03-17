package travel.demo.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
// imports for rapidapi
import java.net.http.HttpRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


    @RestController
    public class New {

        @GetMapping("/")
            public ModelAndView FormSubmission(ModelAndView model) {
            model.setViewName("index");
            model.addObject("form", new FormSubmission());
            return model;
        }
        
        @PostMapping("/")
        public ModelAndView postMethod(@ModelAttribute FormSubmission form, ModelAndView model) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://airports15.p.rapidapi.com/airports?"+ form.getSearchType() +"="+ form.getSearchContent()))
                .header("X-RapidAPI-Host", "airports15.p.rapidapi.com")
                .header("X-RapidAPI-Key", "4bd7c169a3msh084e2975807b31ep1b3813jsn39d77b2f06a9")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = null;
                try {
                    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Airport airport = new Airport();
                System.out.println(response.body().length());
                if(response.body().length() > 2){
                    HashMap<String, String> fhm = formatList(response.body().replace("[{", "").replace("}]", "").split(","));
                    fhm.get("name");
                    airport.setName(fhm.get("name"));
                    airport.setIataCode(fhm.get("iataCode"));
                    airport.setIcaoCode(fhm.get("icaoCode"));
                    airport.setCountryCode(fhm.get("countryCode"));
                    airport.setCity(fhm.get("city"));
                    airport.setElevation(fhm.get("elevation"));
                    airport.setTimezone(fhm.get("timezone"));
                    airport.setLocalTime(fhm.get("localTime"));
                    airport.setLat(fhm.get("lat"));
                    airport.setLon(fhm.get("lon"));
                }
                
            model.addObject("airport", airport);
            if(airport.getName() == null){
                model.addObject("found", "No");
            }else {
                model.addObject("found", "");
            }
            model.setViewName("result");
            return model;
        }

        public HashMap<String, String> formatList(String[] s){
            HashMap<String, String> formattedHashMap = new HashMap<String, String>();
            for(int i = 0; i < s.length; i++){
                    String[] temp = s[i].split(":");
                    System.out.println(temp[0].replace("\"", "")+", "+temp[1].replace("\"", ""));
                    formattedHashMap.put(temp[0].replace("\"", ""),temp[1].replace("\"", ""));
            }
            return formattedHashMap;
        }
        
    }