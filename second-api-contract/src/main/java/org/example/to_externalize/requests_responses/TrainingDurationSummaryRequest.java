package org.example.to_externalize.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDurationSummaryRequest {

    private List<Pair<Integer, Integer>> months; //year and month
}
