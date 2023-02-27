package com.golov.springspace.simplecontroller;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.exceptions.RobotNotExistInFleetExceptopn;
import com.golov.springspace.station.fleet.Fleet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/fleet")
public class FleetController {
    private final Fleet<Robot> fleet;

    public FleetController(Fleet<Robot> fleet) {
        this.fleet = fleet;
    }
    @GetMapping("/get/${callSign}")
    public ResponseEntity<Robot>get(@PathVariable String callSign) {
        Robot r;
        try {
            r = fleet.get(callSign);
        } catch (RobotNotExistInFleetExceptopn e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error", e.getMessage()).build();
        }
        return ResponseEntity.ok(r);
    }
    @GetMapping("")
    public ResponseEntity<List<Robot>> getFleet() {
        return ResponseEntity.ok(fleet.listRobots());
    }
    @GetMapping("/available")
    public ResponseEntity<List<Robot>> getAvailable() {
        return ResponseEntity.ok(fleet.listAvailableRobots());
    }

    @DeleteMapping("/remove/${callSign}")
    public ResponseEntity<String> remove(@PathVariable String callSign) {
        try {
            fleet.remove(fleet.get(callSign));
        } catch (RobotNotExistInFleetExceptopn e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error", e.getMessage()).build();
        }
        return ResponseEntity.ok().header("Succseed", "The robot was removed from fleet").build();
    }
}
