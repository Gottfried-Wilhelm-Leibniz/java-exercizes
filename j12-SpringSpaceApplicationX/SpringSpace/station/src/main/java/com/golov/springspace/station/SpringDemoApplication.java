//package com.golov.springspace.station;
//import com.golov.springspace.infra.Robot;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import java.util.List;
//
//public class SpringDemoApplication {
//    public static void main(String[] args) {
//        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
//        Station<Robot> station = new SpaceStation(ctx);
//        var hal = station.createNew("hal9000");
//        var tach = station.createNew("tachikomas");
//        var john = station.createNew("johnny5");
//        var masch = station.createNew("maschinenmensch");
//        List<Reply> replyList = List.of(hal, tach, john, masch);
//        replyList.stream().forEach(System.out::println);
//        System.out.println(station.getFleetList());
//    }
//}
