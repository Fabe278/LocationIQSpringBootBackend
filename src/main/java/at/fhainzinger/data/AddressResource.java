package at.fhainzinger.data;

import lombok.Data;

import java.util.List;

@Data
public class AddressResource {
    private String place_id;
    private String licence;
    private String osm_type;
    private String osm_id;
    private String lat;
    private String lon;
    private String display_name;
    private Address address;

}
