package ru.yandex.practicum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Scenario;

public interface ActionRepository extends JpaRepository<Action, Long> {

    List<Action> findAllByScenario(Scenario scenario);

    void deleteByScenario(Scenario scenario);
}
