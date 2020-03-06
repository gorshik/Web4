package ru.itmo.gorshkov.web4_2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.gorshkov.web4_2.data.MyUser;
import ru.itmo.gorshkov.web4_2.data.Point;
import ru.itmo.gorshkov.web4_2.data.PointDTO;
import ru.itmo.gorshkov.web4_2.data.UserRepository;
import ru.itmo.gorshkov.web4_2.util.BigDecimalToDouble;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/points")
public class PointsController {
    private final UserRepository repository;

    @Autowired
    public PointsController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Point> getPoints(Principal principal) {
        MyUser user = repository.findByUsername(principal.getName());
        return user.getPoints();
    }

    @PostMapping
    public PointDTO addPoint(@RequestBody PointDTO pointDTO, Principal principal) {
        @Valid
        Point point = pointFromDTO(pointDTO);
        MyUser user = repository.findByUsername(principal.getName());
        int result = checkArea(point);
        point.setResult(result);
        pointDTO.setResult(result);
        user.addPoint(point);
        repository.save(user);
        log.info("POST Point: " + point);
        return pointDTO;
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public static int checkArea(Point point) {
        double x = BigDecimalToDouble.convert(point.getX());
        double y = BigDecimalToDouble.convert(point.getY());
        double r = BigDecimalToDouble.convert(point.getR());
        if (x < 0) {
            if (y > 0)
                return 0;
            else {
                if (y < -0.5 * x - r / 2)
                    return 0;
                else
                    return 1;
            }
        } else {
            if (y > 0) {
                if (Math.pow(x, 2) + Math.pow(y, 2) > Math.pow(r / 2, 2))
                    return 0;
                else
                    return 1;
            } else {
                if (x > r / 2 && y < -r)
                    return 0;
                else
                    return 1;
            }
        }
    }

    public static Point pointFromDTO(PointDTO pointDTO) {
        Point point = new Point();
        point.setX(new BigDecimal(pointDTO.getX()));
        point.setY(new BigDecimal(pointDTO.getY()));
        point.setR(new BigDecimal(pointDTO.getR()));
        return point;
    }
}
