package com.innervix.model3d.common.mybatis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innervix.model3d.common.model.Vector3;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Vector3JsonTypeHandler extends BaseTypeHandler<Vector3> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Vector3 parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, OBJECT_MAPPER.writeValueAsString(List.of(parameter.x(), parameter.y(), parameter.z())));
        } catch (Exception ex) {
            throw new SQLException("Could not write vector JSON", ex);
        }
    }

    @Override
    public Vector3 getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return read(rs.getString(columnName));
    }

    @Override
    public Vector3 getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return read(rs.getString(columnIndex));
    }

    @Override
    public Vector3 getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return read(cs.getString(columnIndex));
    }

    private Vector3 read(String value) throws SQLException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Double[] values = OBJECT_MAPPER.readValue(value, Double[].class);
            if (values.length != 3) {
                return null;
            }
            return new Vector3(values[0], values[1], values[2]);
        } catch (Exception ex) {
            throw new SQLException("Could not read vector JSON", ex);
        }
    }
}
