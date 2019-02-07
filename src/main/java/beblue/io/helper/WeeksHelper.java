package beblue.io.helper;

import beblue.io.model.Weeks;
import beblue.io.repository.WeeksRepository;

public class WeeksHelper {

    private final WeeksRepository weeksRepository;

    public WeeksHelper(WeeksRepository weeksRepository) {
        this.weeksRepository = weeksRepository;
    }

    public void load() {
        weeksRepository.save(new Weeks(null, "Domingo", 0));
        weeksRepository.save(new Weeks(null, "Segunda-Feira", 1));
        weeksRepository.save(new Weeks(null, "Terça-Feira", 2));
        weeksRepository.save(new Weeks(null, "Quarta-Feira", 3));
        weeksRepository.save(new Weeks(null, "Quinta-Feira", 4));
        weeksRepository.save(new Weeks(null, "Sexta", 5));
        weeksRepository.save(new Weeks(null, "Sábado", 6));
    }

}
