package store.quired.api.model;

import java.util.List;

public record Page<T>(
        List<T> items,
        int currentPage,
        int lastPage,
        int perPage,
        long total,
        String next,
        String previous
) {

    public boolean hasNextPage() {
        return next != null;
    }

    public boolean hasPreviousPage() {
        return previous != null;
    }
}