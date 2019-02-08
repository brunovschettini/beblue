package beblue.io.helper;

import beblue.io.model.Genres;
import beblue.io.model.Weeks;
import beblue.io.model.WeeksSales;
import beblue.io.repository.WeeksRepository;
import beblue.io.repository.WeeksSaleRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;
import beblue.io.repository.GenresRepository;

@Component
public class WeeksSaleHelper {

    public GenresRepository genreRepository;
    public WeeksRepository weeksRepository;
    public WeeksSaleRepository weeksSaleRepository;

    public WeeksSaleHelper(GenresRepository genreRepository, WeeksRepository weeksRepository, WeeksSaleRepository weeksSaleRepository) {
        this.genreRepository = genreRepository;
        this.weeksRepository = weeksRepository;
        this.weeksSaleRepository = weeksSaleRepository;
    }

    public void loadWeekesSales() {
        List<WeeksSales> list = new ArrayList<>();
        // sun
        list.add(storeWeekesSales(0, "pop", new BigDecimal(25)));
        list.add(storeWeekesSales(0, "mpb", new BigDecimal(30)));
        list.add(storeWeekesSales(0, "classic", new BigDecimal(35)));
        list.add(storeWeekesSales(0, "rock", new BigDecimal(40)));
        // mon
        list.add(storeWeekesSales(1, "pop", new BigDecimal(7)));
        list.add(storeWeekesSales(1, "mpb", new BigDecimal(5)));
        list.add(storeWeekesSales(1, "classic", new BigDecimal(3)));
        list.add(storeWeekesSales(1, "rock", new BigDecimal(10)));
        // tue
        list.add(storeWeekesSales(2, "pop", new BigDecimal(6)));
        list.add(storeWeekesSales(2, "mpb", new BigDecimal(10)));
        list.add(storeWeekesSales(2, "classic", new BigDecimal(5)));
        list.add(storeWeekesSales(2, "rock", new BigDecimal(15)));
        // wed
        list.add(storeWeekesSales(3, "pop", new BigDecimal(2)));
        list.add(storeWeekesSales(3, "mpb", new BigDecimal(15)));
        list.add(storeWeekesSales(3, "classic", new BigDecimal(8)));
        list.add(storeWeekesSales(3, "rock", new BigDecimal(15)));
        // thu
        list.add(storeWeekesSales(4, "pop", new BigDecimal(10)));
        list.add(storeWeekesSales(4, "mpb", new BigDecimal(20)));
        list.add(storeWeekesSales(4, "classic", new BigDecimal(13)));
        list.add(storeWeekesSales(4, "rock", new BigDecimal(15)));
        // fri
        list.add(storeWeekesSales(5, "pop", new BigDecimal(15)));
        list.add(storeWeekesSales(5, "mpb", new BigDecimal(25)));
        list.add(storeWeekesSales(5, "classic", new BigDecimal(18)));
        list.add(storeWeekesSales(5, "rock", new BigDecimal(20)));
        // sat
        list.add(storeWeekesSales(6, "pop", new BigDecimal(20)));
        list.add(storeWeekesSales(6, "mpb", new BigDecimal(30)));
        list.add(storeWeekesSales(6, "classic", new BigDecimal(25)));
        list.add(storeWeekesSales(6, "rock", new BigDecimal(40)));

        weeksSaleRepository.saveAll(list);
    }

    public WeeksSales storeWeekesSales(Integer weekday, String genre, BigDecimal percent) {
        Weeks w = weeksRepository.findByNumber_day(weekday);
        Genres g = genreRepository.findByName(genre);
        return new WeeksSales(null, w, g, percent, new Date());
    }

}
