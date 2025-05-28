package com.ssafy.trip.service.attraction;

import com.ssafy.trip.domain.attraction.region.Gugun;
import com.ssafy.trip.domain.attraction.region.GugunDao;
import com.ssafy.trip.domain.attraction.region.Sido;
import com.ssafy.trip.domain.attraction.region.SidoDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final SidoDao sidoDao;
    private final GugunDao gugunDao;

    public List<Sido> getAll() {
        return sidoDao.selectAll();
    }

    public List<Gugun> getAllGugun(int sidoId) {
        return gugunDao.selectAllBySido(sidoId);
    }

    public List<Gugun> getAllGugunByCode(Long sidoCode) {
        return gugunDao.selectAllBySidoCode(sidoCode);
    }
}
