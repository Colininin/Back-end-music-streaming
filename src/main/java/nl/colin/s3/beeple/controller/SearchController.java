package nl.colin.s3.beeple.controller;

import nl.colin.s3.beeple.business.SearchService;
import nl.colin.s3.beeple.controller.dto.SearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    public SearchController(SearchService searchService)
    {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> getSearchMatches(@RequestBody SearchRequest searchRequest){
        Map<String, Object> resultMap = new HashMap<>(searchService.getResults(searchRequest));
        return ResponseEntity.ok(resultMap);
    }

}
