package in.co.idbibank.etreasury.idbiforex.repository;

import in.co.idbibank.etreasury.idbiforex.model.IdbiForexDeal;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetProvider;
import java.sql.SQLException;
import java.sql.Types;

import static org.assertj.core.api.Assertions.assertThat;

class IdbiForexDealRepositoryTest {

    @Test
    void recognizesOracleNoDataFoundByVendorErrorCode() {
        SQLException oracleFailure = new SQLException("no data found", "02000", 1403);
        DataIntegrityViolationException springFailure =
                new DataIntegrityViolationException("Procedure failed", oracleFailure);

        assertThat(IdbiForexDealRepository.isOracleNoDataFound(springFailure)).isTrue();
    }

    @Test
    void doesNotConvertOtherOracleErrorsIntoEmptyResults() {
        SQLException oracleFailure = new SQLException("invalid identifier", "42000", 904);

        assertThat(IdbiForexDealRepository.isOracleNoDataFound(oracleFailure)).isFalse();
    }

    @Test
    void mapsActualCursorRateAndIgnoresAbsentLegacyEmailColumns() throws Exception {
        var cursor = RowSetProvider.newFactory().createCachedRowSet();
        var metadata = new RowSetMetaDataImpl();
        metadata.setColumnCount(5);
        defineColumn(metadata, 1, "DEAL_NO", Types.VARCHAR);
        defineColumn(metadata, 2, "RATE", Types.VARCHAR);
        defineColumn(metadata, 3, "CARD_RATES", Types.VARCHAR);
        defineColumn(metadata, 4, "STATUS", Types.VARCHAR);
        defineColumn(metadata, 5, "AMOUNT", Types.NUMERIC);
        cursor.setMetaData(metadata);
        cursor.moveToInsertRow();
        cursor.updateString("DEAL_NO", "FX0508260007");
        cursor.updateString("RATE", "109.66");
        cursor.updateString("CARD_RATES", "No");
        cursor.updateString("STATUS", "VERIFIED");
        cursor.updateBigDecimal("AMOUNT", new java.math.BigDecimal("700"));
        cursor.insertRow();
        cursor.moveToCurrentRow();
        cursor.beforeFirst();
        cursor.next();

        IdbiForexDeal deal = IdbiForexDealRepository.mapDeal(cursor, 0);

        assertThat(deal.dealNumber()).isEqualTo("FX0508260007");
        assertThat(deal.rate()).isEqualTo("109.66");
        assertThat(deal.cardRates()).isEqualTo("No");
        assertThat(deal.netRate()).isNull();
    }

    private void defineColumn(
            RowSetMetaDataImpl metadata,
            int index,
            String name,
            int sqlType) throws SQLException {
        metadata.setColumnName(index, name);
        metadata.setColumnLabel(index, name);
        metadata.setColumnType(index, sqlType);
    }
}
