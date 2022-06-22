package com.hamming.storim.server.common.factories;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.model.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationFactory {
    private static LocationFactory instance;
    private static Map<Long, Location> locations;

    private LocationFactory() {
        initializeLocations();
    };

    private void initializeLocations() {
        locations = new HashMap<>();
        List<Location> locationsFromDb = Database.getInstance().getAll(Location.class);
        for (Location l : locationsFromDb) {
            locations.put(l.getObjectId(), l);
        }
    }

    public static LocationFactory getInstance() {
        if ( instance == null ) {
            instance = new LocationFactory();
        }
        return instance;
    }
    public Location getLocationForObject(Long objectId ) {
        return locations.get(objectId);
    }

    public void setLocation (Long objectId, Location l) {
        locations.put(objectId,l);
    }

    public void removeLocation (Long objectId) {
        locations.remove(objectId);
    }


    public Location createLocation(Long creatorId, LocationDto dto) {
        Long id = Database.getInstance().getNextID();
        Location location = new Location(dto.getObjectId(), dto.getServerId(),dto.getRoomId(), dto.getX(), dto.getY());
        location.setId(id);
        location.setCreatorId(creatorId);
        location.setOwnerId(creatorId);

        Database.getInstance().addBasicObject(location);
        return location;
    }


}
