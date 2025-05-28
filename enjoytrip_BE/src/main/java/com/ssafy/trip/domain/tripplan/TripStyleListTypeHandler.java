package com.ssafy.trip.domain.tripplan;

import com.ssafy.trip.domain.tripplan.enums.TripStyle;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class TripStyleListTypeHandler extends BaseTypeHandler<List<TripStyle>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<TripStyle> parameter, JdbcType jdbcType)
            throws SQLException {
        String joined = parameter.stream()
                .map(TripStyle::name)
                .collect(Collectors.joining(","));
        ps.setString(i, joined);
    }

    @Override
    public List<TripStyle> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String column = rs.getString(columnName);
        return parseStyles(column);
    }

    @Override
    public List<TripStyle> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String column = rs.getString(columnIndex);
        return parseStyles(column);
    }

    @Override
    public List<TripStyle> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String column = cs.getString(columnIndex);
        return parseStyles(column);
    }

    private List<TripStyle> parseStyles(String str) {
        if (str == null || str.isEmpty()) return Collections.emptyList();
        return Arrays.stream(str.split(","))
                .map(TripStyle::valueOf)
                .collect(Collectors.toList());
    }
}
