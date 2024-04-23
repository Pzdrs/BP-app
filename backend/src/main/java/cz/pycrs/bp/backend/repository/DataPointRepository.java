package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DataPointRepository extends MongoRepository<DataPoint, String> {
    List<DataPoint> findAllBySource(DataSource dataSource);

    record BreakdownSignature(int year, int month) {
    }

    record MonthlyBreakdown(BreakdownSignature _id, int count) {
    }

    @Aggregation(pipeline = {
            """
                      { "$group": { "_id": {"year": { "$year": "$timestamp" }, "month": { "$month": "$timestamp" },},"count": { "$sum": 1 }}}
                    """
    })
    List<MonthlyBreakdown> getMonthlyBreakdown();
}
