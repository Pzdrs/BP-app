package cz.pycrs.bp.backend.payload;

public record MonthlyDataPointBreakdown(int month, int year, int count) {
    public MonthlyDataPointBreakdown(cz.pycrs.bp.backend.repository.DataPointRepository.MonthlyBreakdown breakdown) {
        this(breakdown._id().month(), breakdown._id().year(), breakdown.count());
    }
}
