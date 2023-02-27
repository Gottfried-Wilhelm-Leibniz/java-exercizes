package com.golov.springspace.simplecontroller;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/robot")
public class RobotsController {
    private final Station<Robot> station;
    @Autowired
    private List<Robot> modelsList;
    public RobotsController(Station<Robot> station) {
        this.station = station;
    }
    @GetMapping("/dispatch/${callSign}")
    public ResponseEntity<String> dispatch(@PathVariable String callSign) {
        return robotAct(callSign, "Dispatching");
    }

    @GetMapping("/reboot/${callSign}")
    public ResponseEntity<String> reboot(@PathVariable String callSign) {
        return robotAct(callSign, "Rebooting");
    }

    @GetMapping("/selfdiagnostic/${callSign}")
    public ResponseEntity<String> selfDiagnostic(@PathVariable String callSign) {
        return robotAct(callSign, "SelfDiagnostic");
    }

    private ResponseEntity<String> robotAct(String callSign, String Dispatching) {
        var r = station.commandRobot(callSign, Dispatching);
        if(r.isSucceed()) {
            return ResponseEntity.ok(r.reason());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Error", r.reason()).build();
    }

    @GetMapping("/prohibition/hal9000/${name}/${callSign}")
    public ResponseEntity<String> newHal9000(@PathVariable String name, @PathVariable String callSign) {
        return prohibitNewRobot("Hal9000", name, callSign);
    }
    @GetMapping("/prohibition/johnny5/${name}/${callSign}")
    public ResponseEntity<String> newJohnny5(@PathVariable String name, @PathVariable String callSign) {
        return prohibitNewRobot("Johnny5", name, callSign);
    }
    @GetMapping("/prohibition/maschinemensch/${name}/${callSign}")
    public ResponseEntity<String> newMaschinemensch(@PathVariable String name, @PathVariable String callSign) {
        return prohibitNewRobot("Maschinemensch", name, callSign);
    }
    @GetMapping("/prohibition/tachikomas/${name}/${callSign}")
    public ResponseEntity<String> newTachikomas(@PathVariable String name, @PathVariable String callSign) {
        return prohibitNewRobot("Tachikomas", name, callSign);
    }

    private ResponseEntity<String> prohibitNewRobot(String model, String name, String callSign) {
        var r = station.createNew("Hal9000", name, callSign);
        if(r.isSucceed()) {
            return ResponseEntity.ok(r.reason());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Error", r.reason()).build();
    }

    @GetMapping("/availablemodels")
    public ResponseEntity<List<Robot>> availableModels() {
        if(modelsList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error", "No available models").build();
        }
        return ResponseEntity.ok(modelsList);
    }

}
