package com.ssafy.trip.common.wordsearch;

import com.ssafy.trip.domain.attraction.Attraction;
import com.ssafy.trip.domain.attraction.AttractionDao;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrieInitializer {
    private final AttractionDao attractionDao;

//    @PostConstruct
    public void init() {
        execute();
    }

    public void execute() {
        List<Attraction> attractions = attractionDao.selectAllName();
        for (Attraction attraction : attractions) {
            String name = attraction.getTitle();
            Long id = attraction.getId();
            SearchEngine.insert(name, id);
        }
    }
}
