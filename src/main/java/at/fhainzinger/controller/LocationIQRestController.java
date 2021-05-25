package at.fhainzinger.controller;

import at.fhainzinger.data.LongitudeLatitude;
import at.fhainzinger.service.LocationIQDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/locationiq")
public class LocationIQRestController {
    @Autowired
    private LocationIQDataService locationIQDataService;

    @GetMapping("/addresses")
    @ResponseBody
    public String getAddress(@RequestParam String longitude, @RequestParam String latitude){
        return locationIQDataService.getAddress(latitude, longitude);
    }

    @GetMapping("/longlat")
    @ResponseBody
    public LongitudeLatitude getLongitudeLatitude(@RequestParam String address){
        return locationIQDataService.getLongitudeLatitudeByAddress(address);
    }
}
