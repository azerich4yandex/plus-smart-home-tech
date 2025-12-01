package ru.yandex.practicum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;

public interface ConditionRepository extends JpaRepository<Condition, Long> {

    List<Condition> findAllByScenario(Scenario scenario);

    void deleteByScenario(Scenario scenario);
}
