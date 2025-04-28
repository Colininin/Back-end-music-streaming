package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.controller.dto.SearchRequest;
import java.util.Map;

public interface SearchService {
    Map<String, Object> getResults(SearchRequest request);
}
